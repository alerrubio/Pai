package com.pai.pai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //TODO: Checar qué rollo con el FireBaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegister = findViewById<TextView>(R.id.btn_Registro)
        val btnLogIn = findViewById<Button>(R.id.btn_login)


        btnLogIn.setOnClickListener{
            login()
        }



        btnRegister.setOnClickListener{

            val  activityIntent =  Intent(this,RegisterActivity::class.java)
            startActivity(activityIntent)

        }

    }

    private fun login(){
        val email = findViewById<EditText>(R.id.input_email).text.toString()
        val pwd = findViewById<EditText>(R.id.input_password).text.toString()

        if (email.isEmpty() && pwd.isEmpty() ) {

            Toast.makeText(this, "Introduzca sus credenciales", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { respueta ->
                evaluarInicioSesion(email, respueta)
            }
        }
    }

    private fun evaluarInicioSesion(email: String, respuesta: Task<AuthResult>) {
        if (respuesta.isSuccessful) {

            //TODO("Sacar la info del usuario de la BD")

            val request = userProfileChangeRequest {
                displayName = "El Draken"
            }

            auth.currentUser?.updateProfile(request)?.addOnCompleteListener { respuestaCambioDeNombre ->

                val intentChat = Intent(this, DrawerActivity::class.java)
                intentChat.putExtra("username", auth.currentUser?.displayName)

                startActivity(intentChat)
            }
        } else {

            val errorCode = (respuesta.exception as FirebaseAuthInvalidCredentialsException).errorCode
            when(errorCode) {
                "ERROR_INVALID_EMAIL" -> {
                    Toast.makeText(this, "Este correo no se puede usar o tiene formato incorrecto", Toast.LENGTH_SHORT).show()
                }
                "ERROR_WEAK_PASSWORD" -> {
                    Toast.makeText(this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}