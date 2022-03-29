package com.pai.pai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.Message

class ChatActivity : AppCompatActivity() {

    private val listaMensajes = mutableListOf<Message>()
    private val chatAdaptador = AdaptadorChat()

    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chats") //crea la rama o tabla de chats.

    private val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
    private val btnEnviar = findViewById<Button>(R.id.btnEnviar_chat)
    private val txtMensaje = findViewById<EditText>(R.id.txtMensaje_chat)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        rvMensajes.adapter = chatAdaptador


        btnEnviar.setOnClickListener {
            val mensaje = txtMensaje.text.toString()
            if(mensaje.isNotEmpty()){
                txtMensaje.text.clear()
                sendMessage(Message("", mensaje, "yomero", ServerValue.TIMESTAMP))
            }
        }

    }

    private fun sendMessage(message: Message){
        val firebaseMsg = chatRef.push()
        message.id = firebaseMsg.key ?: ""

        firebaseMsg.setValue(message)
    }

    private fun getMessage() {

        chatRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listaMensajes.clear()

                for (snap in snapshot.children) {

                    val mensaje: Message = snap.getValue(Message::class.java) as Message

                    if (mensaje.from == "yomero")
                        mensaje.esMio = true

                    listaMensajes.add(mensaje)
                }

                if (listaMensajes.size > 0) {
                    chatAdaptador.notifyDataSetChanged()
                    rvMensajes.smoothScrollToPosition(listaMensajes.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}