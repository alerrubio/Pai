package com.pai.pai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.Chat
import com.pai.pai.models.Message
import kotlin.system.exitProcess

class ChatIndividualActivity : AppCompatActivity() {




    private val listMessages = mutableListOf<Message>()
    private val chatAdaptador = AdaptadorChat(listMessages)

    private val database = FirebaseDatabase.getInstance()
<<<<<<< Updated upstream
    private var chatRef = database.getReference("")
=======
    private lateinit var chatRef: DatabaseReference
    private lateinit var msgRef: DatabaseReference
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        //Toast.makeText(this, idUsuarioDestino, Toast.LENGTH_SHORT).show()
=======

>>>>>>> Stashed changes



        nombreUsuario = intent.getStringExtra("username") ?: "sin nombre"

<<<<<<< Updated upstream
        chatRef = database.getReference("chats/chatsIndividuales") //crea la rama o tabla de chats.
=======
        //chatRef = database.getReference("chats/chatsIndividuales") //crea la rama o tabla de chats.
>>>>>>> Stashed changes
        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar_chat)
        val txtMensaje = findViewById<EditText>(R.id.txtMensaje_chat)
        val btnReturn = findViewById<ImageView>(R.id.btnRegresar_chatInd)
        rvMensajes.adapter = chatAdaptador

<<<<<<< Updated upstream
        createOrFindChat(Chat("", idUsuarioDestino, user!!.uid), idUsuarioDestino)
=======
        chatRef = createOrFindChat(Chat("", idUsuarioDestino, user!!.uid), idUsuarioDestino)
>>>>>>> Stashed changes


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

<<<<<<< Updated upstream
    private fun createOrFindChat(chat: Chat, idUsuarioDestino: String){



        chatRef.addValueEventListener(object: ValueEventListener {
=======
    private fun createOrFindChat(chat: Chat, idUsuarioDestino: String): DatabaseReference{

        msgRef = database.getReference("chats/chatsIndividuales") //crea la rama o tabla de chats.

        msgRef.addValueEventListener(object: ValueEventListener {
>>>>>>> Stashed changes

            var flag = false

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val chat2: Chat = snap.getValue(Chat::class.java) as Chat

                    if((idUsuarioDestino == chat2.user1 || idUsuarioDestino == chat2.user2) && (user!!.uid == chat2.user1 || user!!.uid == chat2.user2)){
<<<<<<< Updated upstream
                        chatRef = database.getReference("chats/chatsIndividuales/"+chat2.id)
=======
                        msgRef = database.getReference("chats/chatsIndividuales/"+chat2.id).child("Mensajes")
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
                    msgRef = database.getReference("chats/chatsIndividuales/"+chat.id+"/Mensajes")
>>>>>>> Stashed changes
                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatIndividualActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
<<<<<<< Updated upstream
    }

    private fun sendMessage(message: Message){
=======

        return msgRef
    }

    private fun sendMessage(message: Message){
        chatRef = msgRef
>>>>>>> Stashed changes
        val firebaseMsg = chatRef.push()
        message.id = firebaseMsg.key ?: ""

        firebaseMsg.setValue(message)
    }

    private fun getMessage() {

        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        rvMensajes.adapter = chatAdaptador

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
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

                Toast.makeText(this@ChatIndividualActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}