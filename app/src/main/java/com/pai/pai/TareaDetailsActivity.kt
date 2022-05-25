package com.pai.pai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi.encriptacin.CifradoTools
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.pai.pai.adapters.AdaptadorSubgrupos
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Subgrupo
import com.pai.pai.models.TareaObject
import com.pai.pai.models.UserObject

class TareaDetailsActivity: AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private var rama = "tareas/"+ TareaObject.getId()+"/Usuarios/"
    private val tareaRef = database.getReference(rama)



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
            val firebaseMsg = tareaRef.push()

            val entregado = listOf(UserObject.getId(), true)

            firebaseMsg.setValue(entregado)
            finish()
        }
    }
}