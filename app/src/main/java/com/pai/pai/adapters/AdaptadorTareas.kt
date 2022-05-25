package com.pai.pai.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.GroupActivity
import com.pai.pai.R
import com.pai.pai.TareaDetailsActivity
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Grupos
import com.pai.pai.models.Tarea
import com.pai.pai.models.TareaObject

class AdaptadorTareas (private val listaTarea: MutableList<Tarea>, val context: Context):
    RecyclerView.Adapter<AdaptadorTareas.GroupViewHolder>() {

    private lateinit var tvTareaId: TextView
    private lateinit var tvHwName: TextView
    private lateinit var tvHwDescription: TextView
    private lateinit var checkBox: CheckBox
    private var checked = false


    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var pos = 0;

        fun asignarInformacion(tarea: Tarea) {

            tvHwName = itemView.findViewById(R.id.tv_tarea_name)
            tvHwDescription = itemView.findViewById(R.id.tv_tarea_descripcion)
            checkBox = itemView.findViewById(R.id.chb_tarea)

            val contenedorTarea = itemView.findViewById<LinearLayout>(R.id.contenedor_tareas)

            if(checkBox.isChecked){
                checked = true
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

                    TareaObject.setTarea(tvHwName.text.toString(), tvHwDescription.text.toString(), checked)
                    val intent = Intent(context, TareaDetailsActivity::class.java)
                    context.startActivity(intent)

                }
            }
        }
    }

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
