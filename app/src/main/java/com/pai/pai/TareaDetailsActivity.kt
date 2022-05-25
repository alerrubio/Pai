package com.pai.pai

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pai.pai.models.TareaObject
import com.pai.pai.models.User
import com.pai.pai.models.UserObject


class TareaDetailsActivity: AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private var rama = "tareas/"+ TareaObject.getId()+"/usuarios"
    private val tareaRef = database.getReference(rama)
    private lateinit var gamRef: DatabaseReference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareadetails)

        val txtName = findViewById<TextView>(R.id.tv_tareadetail_name)
        val txtDesc = findViewById<TextView>(R.id.tv_tareadetail_info)
        val btnEntregar = findViewById<Button>(R.id.btn_tareadetail_entregar)

        txtName.text = TareaObject.getName()
        txtDesc.text = TareaObject.getDescription()


        val toolbar = findViewById<Toolbar>(R.id.tb_tareadetails)
        setSupportActionBar(toolbar)


        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }


        btnEntregar.setOnClickListener {
            //val firebaseMsg = tareaRef.push()

            var usuarios: MutableList<String> = mutableListOf()
            usuarios = TareaObject.getUsuarios()
            usuarios.add(UserObject.getId())

            tareaRef.ref.setValue(usuarios)

            //firebaseMsg.setValue(usuarios)
            val dialogClick = { dialog: DialogInterface, which: Int ->

            }
            if (!UserObject.getTareas()){
                updateGamifications("tareas")
                UserObject.setTareas(true)
                val builder = AlertDialog.Builder(this)
                builder.setNeutralButton("Ok",dialogClick)
                builder.setTitle("Logro de primer tarea entregada desbloqueado!")
                builder.show()
            }
            Handler(Looper.getMainLooper()).postDelayed(
                { finish() },
                2000
            )


        }
    }

    fun updateGamifications(gamToUpdate: String){
        gamRef = database.getReference("users/")

        gamRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val dbuser: User = snap.getValue(User::class.java) as User

                    if (dbuser.id == user!!.uid){
                        when(gamToUpdate){
                            "chat" -> {
                                var auxId  = snap.key
                                var db = database.getReference("users/"+auxId+"/chat");
                                db.ref.setValue(true)
                            }
                            "tareas" -> {
                                var auxId  = snap.key
                                var db = database.getReference("users/"+auxId+"/tareas");
                                db.ref.setValue(true)

                            }
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@TareaDetailsActivity, "Error al actualizar los logros", Toast.LENGTH_SHORT).show()
            }
        })
    }
}