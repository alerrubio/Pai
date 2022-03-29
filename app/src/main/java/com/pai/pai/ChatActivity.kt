package com.pai.pai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.Message

class ChatActivity : AppCompatActivity() {

    private val chatAdaptador = AdaptadorChat()

    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chats") //crea la rama o tabla de chats.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        rvMensajes.adapter = chatAdaptador
    }

    private fun sendMessage(message: Message){
        val firebaseMsg = chatRef.push()
        message.id = firebaseMsg.key ?: ""

        firebaseMsg.setValue(message)
    }
}