package com.pai.pai

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.FirebaseDatabase
import com.pai.pai.models.*

class CreateTareasActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private val database = FirebaseDatabase.getInstance()

    private lateinit var nameTarea: EditText
    private lateinit var descripcionTarea: EditText

    private var spinnerSelected: String ="LMAD"
    var spinner: Spinner? = null
    var carrera = ""

    private var rama = "tareas"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createtareas)

        nameTarea = findViewById(R.id.et_tareas_name)
        descripcionTarea = findViewById(R.id.et_tareas_descripcion)

        val btnCreate = findViewById<Button>(R.id.btn_create_tareas)

        //Para el toolbar
        val toolbar = findViewById<Toolbar>(R.id.tb_create_tareas)
        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        spinner = findViewById(R.id.spinner_tareas_carreras)
        spinner?.onItemSelectedListener = this

        btnCreate.setOnClickListener {
            crearTarea()
        }

    }

    private fun crearTarea(){

        val name = nameTarea.text.toString()
        val descripcion = descripcionTarea.text.toString()

        when(spinnerSelected){
            "LMAD" -> {
                carrera = "LMAD"
            }
            "LCC" -> {
                carrera = "LCC"
            }
            "LSTI" -> {
                carrera = "LSTI"
            }
            "LF" -> {
                carrera = "LF"
            }
            "LM" -> {
                carrera = "LM"
            }
            "LA" -> {
                carrera = "LA"
            }
            else -> {

            }
        }

        if(name!="" && descripcion!=""){
            sendToFireBase(Tarea("", name, descripcion, carrera))
        }
        else{
            Toast.makeText(this@CreateTareasActivity, "Llenar todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendToFireBase(tarea: Tarea){
        val subgroupRef = database.getReference(rama)

        val firebaseMsg = subgroupRef.push()
        tarea.id = firebaseMsg.key ?: ""

        firebaseMsg.setValue(tarea)
        finish()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
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