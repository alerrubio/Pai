package com.pai.pai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.Chat
import com.pai.pai.models.Message
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
                sendMessage(Message("", mensaje, user!!.uid, ServerValue.TIMESTAMP))
            }
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

    interface Callbacks{

        fun assingRef(reference: String)
    }
}