package com.pai.pai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Subgrupo
import com.pai.pai.models.User
import com.pai.pai.models.UserObject


class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private var rama = "users"
    private val subRef = database.getReference(rama)
    private val presenceRef = Firebase.database.getReference("onlineusers/")
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

        if (email.isEmpty() || pwd.isEmpty()) {

            Toast.makeText(this, "Introduzca sus credenciales", Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { respueta ->
                evaluarInicioSesion(email, pwd, respueta)
            }
        }
    }

    private fun evaluarInicioSesion(email: String, pass: String, respuesta: Task<AuthResult>) {
        if (respuesta.isSuccessful) {

            //TODO("Sacar la info del usuario de la BD")

            val request = userProfileChangeRequest {
                displayName = auth.currentUser!!.displayName
            }

            auth.currentUser?.updateProfile(request)?.addOnCompleteListener { respuestaCambioDeNombre ->

                val intentChat = Intent(this, DrawerActivity::class.java)
                intentChat.putExtra("username", auth.currentUser?.displayName)

                subRef.addValueEventListener(object: ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (snap in snapshot.children) {

                            val usuariologged: User = snap.getValue(User::class.java) as User

                            if(usuariologged.id == auth.currentUser!!.uid){
                                UserObject.setUser(auth.currentUser!!.uid, auth.currentUser?.displayName, usuariologged.email, usuariologged.password, usuariologged.carrera)
                                UserObject.setChat(usuariologged.chatIndividual)
                                UserObject.setTareas(usuariologged.tareas)
                                presenceRef.child("")
                                startActivity(intentChat)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                        Toast.makeText(this@LoginActivity, "Error al leer los contactos", Toast.LENGTH_SHORT).show()
                    }
                })




               // UserObject.setUser(auth.currentUser!!.uid, auth.currentUser?.displayName, email, pass)

                //startActivity(intentChat)
            }
        } else {

            val errorCode = (respuesta.exception as FirebaseAuthException).errorCode
            when(errorCode) {

                "ERROR_WRONG_PASSWORD" -> {
                    Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show()
                }
                "ERROR_INVALID_EMAIL" -> {
                    Toast.makeText(this, "Este correo no se puede usar o tiene formato incorrecto", Toast.LENGTH_SHORT).show()
                }

                "ERROR_USER_NOT_FOUND" -> {
                    Toast.makeText(this, "Esta cuenta no está registrada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}