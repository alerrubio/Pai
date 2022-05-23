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
import com.pai.pai.models.Subgrupo

class AdaptadorSubgrupos (private val listaGrupos: MutableList<Subgrupo>, val context: Context):
    RecyclerView.Adapter<AdaptadorSubgrupos.GroupViewHolder>() {

    private lateinit var tvGroupId: TextView


    inner class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var pos = 0;

        fun asignarInformacion(grupo: Subgrupo){

            val tvGroupName = itemView.findViewById<TextView>(R.id.tv_nombre_grupo)
            val ivGroup = itemView.findViewById<ImageView>(R.id.iv_grupo)
            val contenedorGrupo = itemView.findViewById<LinearLayout>(R.id.contenedor_grupos)
            tvGroupId = itemView.findViewById(R.id.tv_groupid)



            tvGroupName.text = grupo.name
            tvGroupId.text = grupo.id
            //TODO agregar imagen del usuario ivUser = blalvlavla


            val params = contenedorGrupo.layoutParams

            val newParams = FrameLayout.LayoutParams(
                params.width,
                params.height
            )
            contenedorGrupo.layoutParams = newParams

        }


        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

            when(v!!.id){
                R.id.framegroup->{

                    //GroupObject.setGroup(listaGrupos[pos].id, listaGrupos[pos].name)
                    //TODO("Lanzar el chat del subgrupo")

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val miView = LayoutInflater.from(parent.context).inflate(R.layout.item_grupos, parent, false)
        return GroupViewHolder(miView)
    }

    override fun onBindViewHolder(holder: AdaptadorSubgrupos.GroupViewHolder, position: Int) {
        holder.pos = position
        holder.asignarInformacion(listaGrupos[position])

    }

    override fun getItemCount(): Int = listaGrupos.size

}