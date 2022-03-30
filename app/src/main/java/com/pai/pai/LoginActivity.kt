package com.pai.pai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    //TODO: Checar qué rollo con el FireBaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegister = findViewById<TextView>(R.id.btn_Registro)
        val btnLogIn = findViewById<Button>(R.id.btn_login)


        btnLogIn.setOnClickListener{
            //TODO: Agregar las funciones correspondientes para el registro y validación
            // de inicio de sesión
            val chatIntent = Intent(this, ChatActivity::class.java)
            val username = findViewById<EditText>(R.id.input_username).text.toString()
            if(username.isEmpty()){
                Toast.makeText(this, "Falta usuario", Toast.LENGTH_SHORT).show()
            }
            else{
                chatIntent.putExtra("username", username)
                startActivity(chatIntent)
            }
        }



        btnRegister.setOnClickListener{

            val  activityIntent =  Intent(this,RegisterActivity::class.java)
            startActivity(activityIntent)

        }

    }
}