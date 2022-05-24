package com.pai.pai.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.GroupActivity
import com.pai.pai.R
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Grupos
import com.pai.pai.models.Tarea

class AdaptadorTareas (private val listaTarea: MutableList<Tarea>, val context: Context):
    RecyclerView.Adapter<AdaptadorTareas.GroupViewHolder>() {

    private lateinit var tvTareaId: TextView


    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var pos = 0;

        fun asignarInformacion(tarea: Tarea) {

            val tvHwName = itemView.findViewById<TextView>(R.id.tv_tarea_name)
            val tvHwDescription = itemView.findViewById<TextView>(R.id.tv_tarea_descripcion)
            val contenedorTarea = itemView.findViewById<LinearLayout>(R.id.contenedor_tareas)


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
