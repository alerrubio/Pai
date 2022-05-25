package com.pai.pai.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pai.pai.GroupActivity
import com.pai.pai.R
import com.pai.pai.TareaDetailsActivity
import com.pai.pai.models.*

class AdaptadorTareas (private val listaTarea: MutableList<Tarea>, val context: Context):
    RecyclerView.Adapter<AdaptadorTareas.GroupViewHolder>() {

    private lateinit var tvTareaId: TextView
    private lateinit var tvHwName: TextView
    private lateinit var tvHwDescription: TextView
    private lateinit var checkBox: CheckBox
    private var checked = false
    private var rama = ""

    private val database = FirebaseDatabase.getInstance()



    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var pos = 0;

        fun asignarInformacion(tarea: Tarea) {

            tvHwName = itemView.findViewById(R.id.tv_tarea_name)
            tvHwDescription = itemView.findViewById(R.id.tv_tarea_descripcion)
            checkBox = itemView.findViewById(R.id.chb_tarea)

            val contenedorTarea = itemView.findViewById<LinearLayout>(R.id.contenedor_tareas)


           //validarEntrega(tarea)

            /*if(TareaObject.getChecked()){
                checkBox.isChecked=true
                TareaObject.setChecked(false)
            }*/

            if(tarea.checked){
                checkBox.isChecked = true
            }


            tvHwName.text = tarea.name
            tvHwDescription.text = tarea.descripcion

            //TODO validar si está completada o no


            val params = contenedorTarea.layoutParams

            val newParams = FrameLayout.LayoutParams(
                params.width,
                params.height
            )
            contenedorTarea.layoutParams = newParams

        }


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when (v!!.id) {
                R.id.frametareas -> {

                    TareaObject.setTarea(listaTarea[pos].id, tvHwName.text.toString(), tvHwDescription.text.toString(), checked, listaTarea[pos].usuarios)
                    val intent = Intent(context, TareaDetailsActivity::class.java)
                    context.startActivity(intent)

                }
            }
        }
    }

    /*private fun validarEntrega(tarea: Tarea){
        rama = ""
        rama = "tareas/"+tarea.id+"/Usuarios"
        val userRef = database.getReference(rama)

        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val validar: String = snap.getValue() as String

                    if(validar == UserObject.getId()){

                        //TareaObject.setChecked(true)
                        checkBox.isChecked = true
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context, "Error al leer los usuarios cumplidos", Toast.LENGTH_SHORT).show()
            }
        })

    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val miView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tareas, parent, false)
        return GroupViewHolder(miView)
    }

    override fun onBindViewHolder(holder: AdaptadorTareas.GroupViewHolder, position: Int) {
        holder.pos = position
        holder.asignarInformacion(listaTarea[position])
    }

    override fun getItemCount(): Int = listaTarea.size
}
