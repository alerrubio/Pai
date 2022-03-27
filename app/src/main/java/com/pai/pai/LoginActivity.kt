package com.pai.pai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegister = findViewById<TextView>(R.id.btn_Registro)

        btnRegister.setOnClickListener{

            val  activityIntent =  Intent(this,RegisterActivity::class.java)
            startActivity(activityIntent)

        }

    }
}