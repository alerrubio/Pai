package com.pai.pai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ServerValue
import com.pai.pai.models.Message

class ChatSelectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_chat)

        val btnIndividual = findViewById<Button>(R.id.btn_individual)
        val btnGrupal = findViewById<Button>(R.id.btn_grupal)
        var username = intent.getStringExtra("username").toString()

        btnGrupal.setOnClickListener {
            val  activityIntent =  Intent(this, ChatActivity::class.java)


            activityIntent.putExtra("username", username)

            startActivity(activityIntent)
        }

        btnIndividual.setOnClickListener {
            val  activityIntent =  Intent(this,ChatIndividualActivity::class.java)
            activityIntent.putExtra("username", username)
            startActivity(activityIntent)
        }


    }
}