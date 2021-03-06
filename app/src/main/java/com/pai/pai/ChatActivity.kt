package com.pai.pai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.Message

class ChatActivity : AppCompatActivity() {

    private val listMessages = mutableListOf<Message>()
    private val chatAdaptador = AdaptadorChat(listMessages)

    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chats") //crea la rama o tabla de chats.
    private lateinit var nombreUsuario: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        nombreUsuario = intent.getStringExtra("username") ?: "sin nombre"


         val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
         val btnEnviar = findViewById<Button>(R.id.btnEnviar_chat)
         val txtMensaje = findViewById<EditText>(R.id.txtMensaje_chat)
        val btnReturn = findViewById<ImageView>(R.id.btnRegresar_chatGrupal)
        rvMensajes.adapter = chatAdaptador

        btnEnviar.setOnClickListener {
            val mensaje = txtMensaje.text.toString()
            if(mensaje.isNotEmpty()){
                txtMensaje.text.clear()
                sendMessage(Message("", mensaje, nombreUsuario, ServerValue.TIMESTAMP))
            }
        }

        getMessage()

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

                    if (mensaje.from == nombreUsuario)
                        mensaje.esMio = true

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
}