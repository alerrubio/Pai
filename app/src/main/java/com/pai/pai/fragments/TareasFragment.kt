package com.pai.pai.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pai.pai.CreateSubgroupActivity
import com.pai.pai.CreateTareasActivity
import com.pai.pai.R
import com.pai.pai.adapters.AdaptadorGrupos
import com.pai.pai.adapters.AdaptadorTareas
import com.pai.pai.models.*

class TareasFragment: Fragment() {

    private var context2: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context2 = context
    }


    private val database = FirebaseDatabase.getInstance()
    private var rama = "tareas"
    private val userRef = database.getReference(rama)
    private val tareas = mutableListOf<Tarea>()
    private var tareasAdaptador: AdaptadorTareas? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tareas, container, false)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_tareas)

        this.tareasAdaptador = AdaptadorTareas(tareas, this.context2!!)
        getTareas(view)

        fab.setOnClickListener {


            val intent = Intent(context2, CreateTareasActivity::class.java)
            startActivity(intent)

        }

        return view
    }

    private fun getTareas(view: View) {

        val rv_tareas = view.findViewById<RecyclerView>(R.id.rv_tareas)
        rv_tareas.adapter = tareasAdaptador

        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                tareas.clear()

                for (snap in snapshot.children) {

                    val tarea: Tarea = snap.getValue(Tarea::class.java) as Tarea

                    if(tarea.carrera == UserObject.getCarrera()){

                        //validarEntrega(tarea)
                            if(tarea.usuarios.contains(UserObject.getId())){
                                tarea.checked=true
                            }

                        tareas.add(tarea)
                    }
                }

                if (tareas.size > 0) {
                    tareasAdaptador?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context, "Error al leer las tareas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validarEntrega(tarea: Tarea){
        var rama2 = ""
        rama2 = "tareas/"+ tarea.id + "/Usuarios"
        val userRef = database.getReference(rama2)

        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val validar: String = snap.getValue() as String

                    if(validar == UserObject.getId()){

                        tarea.checked = true
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context, "Error al leer los usuarios cumplidos", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
