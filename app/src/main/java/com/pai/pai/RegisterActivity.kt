package com.pai.pai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.pai.pai.models.User

class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var spinnerSelected: String ="LMAD"
    var spinner: Spinner? = null
    var categoria = ""


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        spinner = findViewById(R.id.spinner_carreras)
        spinner?.onItemSelectedListener = this



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

        when(spinnerSelected){
            "LMAD" -> {
                categoria = "LMAD"
            }
            "LCC" -> {
                categoria = "LCC"
            }
            "LSTI" -> {
                categoria = "LSTI"
            }
            "LF" -> {
                categoria = "LF"
            }
            "LM" -> {
                categoria = "LM"
            }
            "LA" -> {
                categoria = "LA"
            }
            else -> {

            }
        }


        if (email.isEmpty() || pwd.isEmpty() || confPwd.isEmpty() ||
            username.isEmpty() || name.isEmpty() || lName.isEmpty() || mLName.isEmpty()){
            Toast.makeText(this, "Introduzca sus datos completos", Toast.LENGTH_SHORT).show()
        }else{
            if(checkPwd(pwd, confPwd)){
                auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { respueta ->
                    crearUsuarioBD(User("", username, pwd, email, name, lName, mLName, categoria), respueta)
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

                val intentChat = Intent(this, LoginActivity::class.java)

                startActivity(intentChat)
            }
        }
    }

    private fun crearUsuarioBD(usuario: User, respuesta: Task<AuthResult>){

        if (respuesta.isSuccessful){
            usuario.id = auth.currentUser?.uid.toString()
            val userRef = database.getReference("users/")
            val firebaseUser = userRef.push()

            firebaseUser.setValue(usuario)
        }else{
            Toast.makeText(this, "No se pudo registrar al usuario", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            spinnerSelected = parent.getItemAtPosition(position).toString()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}