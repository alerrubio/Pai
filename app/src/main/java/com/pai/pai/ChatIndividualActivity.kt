package com.pai.pai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi.encriptacin.CifradoTools
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.Chat
import com.pai.pai.models.Message
import com.pai.pai.models.UserObject
import javax.security.auth.callback.Callback
import kotlin.system.exitProcess

class ChatIndividualActivity : AppCompatActivity() {

    private val listMessages = mutableListOf<Message>()
    private val chatAdaptador = AdaptadorChat(listMessages)

    private var textRef = ""

    private val database = FirebaseDatabase.getInstance()
    private lateinit var chatRef: DatabaseReference
    private lateinit var msgRef: DatabaseReference
    private lateinit var nombreUsuario: String
    private lateinit var idUsuarioDestino: String

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val messageSenderID = user?.uid.toString()

    private var messageRecieverID = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_individual)

        idUsuarioDestino = intent.getStringExtra("idUsuario") ?: "sin id"




        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin nombre"

        findViewById<TextView>(R.id.tv_UserChat).text = nombreUsuario

        //chatRef = database.getReference("chats/chatsIndividuales") //crea la rama o tabla de chats.
        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar_chat)
        val txtMensaje = findViewById<EditText>(R.id.txtMensaje_chat)
        val btnReturn = findViewById<ImageView>(R.id.btnRegresar_chatInd)
        val btnUbicacion = findViewById<ImageButton>(R.id.btn_ubicacion)
        rvMensajes.adapter = chatAdaptador



        createOrFindChat(Chat("", idUsuarioDestino, user!!.uid), idUsuarioDestino, object: Callbacks{
            override fun assingRef(reference: String) {
                chatRef = database.getReference(reference)


                getMessage()
            }
        })

        btnEnviar.setOnClickListener {
            val mensaje = txtMensaje.text.toString()
            if(mensaje.isNotEmpty()){
                txtMensaje.text.clear()
                sendMessage(Message("", mensaje, user!!.uid, ServerValue.TIMESTAMP, UserObject.getName().toString()))
            }
        }

        btnUbicacion.setOnClickListener{
            revisarPermisos()
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun createOrFindChat(chat: Chat, idUsuarioDestino: String, callback: Callbacks){

        chatRef = database.getReference("chats/chatsIndividuales") //crea la rama o tabla de chats.

        chatRef.addValueEventListener(object: ValueEventListener {

            var flag = false

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val chat2: Chat = snap.getValue(Chat::class.java) as Chat

                    if((idUsuarioDestino == chat2.user1 || idUsuarioDestino == chat2.user2) && (user!!.uid == chat2.user1 || user!!.uid == chat2.user2)){
                        chatRef = database.getReference("chats/chatsIndividuales/"+chat2.id).child("Mensajes")
                        textRef = "chats/chatsIndividuales/"+chat2.id+"/Mensajes"
                        callback.assingRef(textRef)
                        Toast.makeText(this@ChatIndividualActivity, "Sí existe el chat", Toast.LENGTH_SHORT).show()
                        flag = true
                        break
                    }

                }

                if(!flag){
                    val firebaseMsg = chatRef.push()
                    chat.id = firebaseMsg.key ?: ""
                    Toast.makeText(this@ChatIndividualActivity, "Ya se creó el chat", Toast.LENGTH_SHORT).show()
                    firebaseMsg.setValue(chat)
                    chatRef = database.getReference("chats/chatsIndividuales/"+chat.id+"/Mensajes")
                    textRef = "chats/chatsIndividuales/"+chat.id+"/Mensajes"
                }

            }



            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatIndividualActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(message: Message){
        val firebaseMsg = chatRef.push()

        message.id = firebaseMsg.key ?: ""

        if(UserObject.getEncrypt()){
            message.encriptado = true
            message.contenido = CifradoTools.cifrar(message.contenido, "mensajeEncrypted")
        }

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

                    if (mensaje.from == user!!.uid){
                        mensaje.esMio = true
                        mensaje.from = user.displayName.toString()
                    }
                    else {
                        mensaje.from = nombreUsuario
                        mensaje.esMio = false
                    }

                    if(mensaje.encriptado){
                        mensaje.contenido = CifradoTools.descifrar(mensaje.contenido, "mensajeEncrypted")
                    }


                    listMessages.add(mensaje)
                }

                if (listMessages.size > 0) {
                    chatAdaptador.notifyDataSetChanged()
                    rvMensajes.smoothScrollToPosition(listMessages.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatIndividualActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
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

    interface Callbacks{

        fun assingRef(reference: String)
    }
}