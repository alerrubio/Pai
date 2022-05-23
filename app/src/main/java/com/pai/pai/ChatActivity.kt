package com.pai.pai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Message
import com.pai.pai.models.UserObject

class ChatActivity : AppCompatActivity() {

    private val listMessages = mutableListOf<Message>()
    private val chatAdaptador = AdaptadorChat(listMessages)

    private val database = FirebaseDatabase.getInstance()

    private val rama = "groups/id"+GroupObject.getId().toString()+"/mensajes"
    private val chatRef = database.getReference(rama) //crea la rama o tabla de chats.
    private lateinit var nombreUsuario: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_individual)

        nombreUsuario = intent.getStringExtra("username") ?: "sin nombre"



        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar_chat)
        val txtMensaje = findViewById<EditText>(R.id.txtMensaje_chat)
        val btnReturn = findViewById<ImageView>(R.id.btnRegresar_chatInd)
        val namechat = findViewById<TextView>(R.id.tv_UserChat)
        val btnUbicacion = findViewById<ImageButton>(R.id.btn_ubicacion)

        rvMensajes.adapter = chatAdaptador
        namechat.text = GroupObject.getName()

        btnEnviar.setOnClickListener {
            val mensaje = txtMensaje.text.toString()
            if(mensaje.isNotEmpty()){
                txtMensaje.text.clear()
                sendMessage(Message("", mensaje, UserObject.getId(), ServerValue.TIMESTAMP, UserObject.getName().toString()))
            }
        }


        getMessage()

        btnUbicacion.setOnClickListener{
            revisarPermisos()
        }

        btnReturn.setOnClickListener {
            finish()
        }

    }

    private fun sendMessage(message: Message){
        val firebaseMsg = chatRef.push()
        message.id = firebaseMsg.key ?: ""

        firebaseMsg.setValue(message)
    }

    private fun getMessage() {

        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        rvMensajes.adapter = chatAdaptador

        chatRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listMessages.clear()

                for (snap in snapshot.children) {

                    val mensaje: Message = snap.getValue(Message::class.java) as Message

                    if (mensaje.from == UserObject.getId()){
                        mensaje.esMio = true
                        //mensaje.from = UserObject.getName().toString()
                    }
                    else {
                        //mensaje.from = nombreUsuario
                        mensaje.esMio = false
                    }

                    listMessages.add(mensaje)
                }

                if (listMessages.size > 0) {
                    chatAdaptador.notifyDataSetChanged()
                    rvMensajes.smoothScrollToPosition(listMessages.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun revisarPermisos() {
        // Apartir de Android 6.0+ necesitamos pedir el permiso de ubicacion
        // directamente en tiempo de ejecucion de la app
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tenemos permiso para la ubicacion
            // Solicitamos permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            // Ya se han concedido los permisos anteriormente
            abrirMapa()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Se requiere aceptar el permiso", Toast.LENGTH_SHORT).show()
                revisarPermisos()
            } else {
                Toast.makeText(this, "Permisio concedido", Toast.LENGTH_SHORT).show()
                abrirMapa()
            }
        }
    }

    private fun abrirMapa() {

        startActivityForResult(Intent(this, MapsActivity::class.java), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            findViewById<TextView>(R.id.txtMensaje_chat).text = data?.getStringExtra("ubicacion") ?: ""
        } else {

            findViewById<TextView>(R.id.txtMensaje_chat).text = "Error o no seleccionaste una direccion"
        }
    }

}