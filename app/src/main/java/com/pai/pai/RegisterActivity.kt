package com.pai.pai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.pai.pai.models.Message
import com.pai.pai.models.User

class RegisterActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var btnRegistrarse = findViewById<Button>(R.id.btn_edit_R)

        btnRegistrarse.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario(){
        var email = findViewById<EditText>(R.id.input_email_R).text.toString()
        var pwd = findViewById<EditText>(R.id.input_password_R).text.toString()
        var confPwd = findViewById<EditText>(R.id.input_contraseña_R).text.toString()
        var username = findViewById<EditText>(R.id.input_username_R).text.toString()
        var name = findViewById<EditText>(R.id.input_nombre_R).text.toString()
        var lName = findViewById<EditText>(R.id.input_AP_R).text.toString()
        var mLName = findViewById<EditText>(R.id.input_AM_R).text.toString()

        if (email.isEmpty() || pwd.isEmpty() || confPwd.isEmpty() ||
            username.isEmpty() || name.isEmpty() || lName.isEmpty() || mLName.isEmpty()){
            Toast.makeText(this, "Introduzca sus datos completos", Toast.LENGTH_SHORT).show()
        }else{
            if(checkPwd(pwd, confPwd)){
                auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { respueta ->
                    crearUsuarioBD(User("", username, pwd, email, name, lName, mLName), respueta)
                    evaluarRegistro(username, respueta)
                }
            }
        }
    }

    private fun checkPwd(pwd: String, conf: String): Boolean{
        var response = false

        if (pwd == conf){
            response = true
        }

        return response
    }

    private fun evaluarRegistro(username: String, respuesta: Task<AuthResult>) {
        if (respuesta.isSuccessful) {

            val request = userProfileChangeRequest {
                displayName = username
            }

            auth.currentUser?.updateProfile(request)?.addOnCompleteListener { respuestaCambioDeNombre ->

                val intentChat = Intent(this, DrawerActivity::class.java)
                intentChat.putExtra("username", auth.currentUser?.displayName)

                startActivity(intentChat)
            }
        }
    }

    private fun crearUsuarioBD(usuario: User, respuesta: Task<AuthResult>){

        if (respuesta.isSuccessful){
            usuario.id = auth.currentUser?.uid.toString()
            val userRef = database.getReference("users/" + usuario.id)
            val firebaseUser = userRef.push()

            firebaseUser.setValue(usuario)
        }else{
            Toast.makeText(this, "No se pudo registrar al usuario", Toast.LENGTH_SHORT).show()
        }

    }


}