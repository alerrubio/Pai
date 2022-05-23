package com.pai.pai

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pai.pai.adapters.AdaptadorContactos
import com.pai.pai.adapters.AdaptadorMiembros
import com.pai.pai.models.*

class CreateSubgroupActivity: AppCompatActivity() {


    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users")
    private val usuarios = mutableListOf<User>()
    private var miembrosAdaptador: AdaptadorMiembros? = null
    private lateinit var rv_contactos: RecyclerView
    private lateinit var nameSub: EditText

    private lateinit var miembros: MutableList<String>

    private var rama = "groups/id"+ GroupObject.getId().toString()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createsubgroups)

        nameSub = findViewById(R.id.et_subgroup_name)
        val btnCreate = findViewById<Button>(R.id.btn_subgroup_crear)
        //val arrayMiembros =

        //Para el toolbar
        val toolbar = findViewById<Toolbar>(R.id.tb_subgroup)
        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        this.miembrosAdaptador = AdaptadorMiembros(usuarios, this)

        rv_contactos = findViewById(R.id.rv_subgroup_contactos)
        rv_contactos.layoutManager =  LinearLayoutManager(this@CreateSubgroupActivity)
        rv_contactos.adapter = miembrosAdaptador


        getContacts()

        btnCreate.setOnClickListener {
            crearSub()
        }


    }

    private fun getContacts() {



        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                usuarios.clear()

                for (snap in snapshot.children) {

                    val usuario: User = snap.getValue(User::class.java) as User

                    if(usuario.id != UserObject.getId()){
                        usuarios.add(usuario)
                    }
                }

                if (usuarios.size > 0) {
                    miembrosAdaptador?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@CreateSubgroupActivity, "Error al leer los contactos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun crearSub(){

        miembros = Miembros.getMembers()
        val name = nameSub.text.toString()

        if(name!=""){
            sendToFireBase(Subgrupo("", name, miembros))
        }
        else{
            Toast.makeText(this@CreateSubgroupActivity, "Escribir un nombre para el subgrupo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendToFireBase(subgrupo: Subgrupo){
        rama = "$rama/${subgrupo.name}"
        val subgroupRef = database.getReference(rama)

        val firebaseMsg = subgroupRef.push()
        subgrupo.id = firebaseMsg.key ?: ""

        firebaseMsg.setValue(subgrupo)


    }

    /*chatref = groups/id1/Nombredelsubgrupo
    private fun createSubgroup(miembros: array){
        val firebaseMsg = chatRef.push()

        foreach {
            subgrupo = (miembro[i])
            setValue subgrupo
        }

        firebaseMsg.setValue(message)
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}