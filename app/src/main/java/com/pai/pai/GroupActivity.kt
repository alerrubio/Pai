package com.pai.pai

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pai.pai.adapters.AdaptadorGrupos
import com.pai.pai.adapters.AdaptadorMiembros
import com.pai.pai.adapters.AdaptadorSubgrupos
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Subgrupo
import com.pai.pai.models.User
import com.pai.pai.models.UserObject

class GroupActivity:  AppCompatActivity() {

    private var gruposAdaptador: AdaptadorSubgrupos? = null
    private lateinit var rvSubgroups: RecyclerView
    private val subgrupos = mutableListOf<Subgrupo>()


    private val database = FirebaseDatabase.getInstance()
    private var rama = "groups/id"+ GroupObject.getId().toString()+"/Subgrupos"
    private val subRef = database.getReference(rama)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groupdetails)

        val txtName = findViewById<TextView>(R.id.txt_groupname)
        val btnChat = findViewById<Button>(R.id.btn_groupchat)
        rvSubgroups = findViewById<RecyclerView>(R.id.rv_groupsub)
        val fab = findViewById<FloatingActionButton>(R.id.fab_group_creates)


        txtName.text = GroupObject.getName()


        val toolbar = findViewById<Toolbar>(R.id.tb_group)
        setSupportActionBar(toolbar)


        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        this.gruposAdaptador = AdaptadorSubgrupos(subgrupos, this)


        rvSubgroups.layoutManager =  LinearLayoutManager(this@GroupActivity)
        rvSubgroups.adapter = gruposAdaptador

        getSubgroups()

        fab.setOnClickListener {
            if(UserObject.getCarrera() == GroupObject.getName()){

                val intent = Intent(this@GroupActivity, CreateSubgroupActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@GroupActivity, "Usted no pertecene a esta carrera y no puede crear equipos aquí", Toast.LENGTH_SHORT).show()
            }
        }

        btnChat.setOnClickListener {
            val intent = Intent(this@GroupActivity, ChatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getSubgroups() {



        subRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                subgrupos.clear()

                for (snap in snapshot.children) {

                    val subgrupo: Subgrupo = snap.getValue(Subgrupo::class.java) as Subgrupo

                    if(subgrupo.miembros.contains(UserObject.getId())){
                        subgrupos.add(subgrupo)
                    }
                }
                if (subgrupos.size > 0) {
                    gruposAdaptador?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@GroupActivity, "Error al leer los contactos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}