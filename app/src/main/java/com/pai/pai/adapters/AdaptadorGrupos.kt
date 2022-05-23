package com.pai.pai.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.R
import com.pai.pai.GroupActivity
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Grupos

class AdaptadorGrupos (private val listaGrupos: MutableList<Grupos>, val context: Context):
    RecyclerView.Adapter<AdaptadorGrupos.GroupViewHolder>() {

    private lateinit var tvGroupId: TextView


    inner class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var pos = 0;

        fun asignarInformacion(grupo: Grupos){

            val tvGroupName = itemView.findViewById<TextView>(R.id.tv_nombre_grupo)
            val ivGroup = itemView.findViewById<ImageView>(R.id.iv_grupo)
            val contenedorGrupo = itemView.findViewById<LinearLayout>(R.id.contenedor_grupos)
            tvGroupId = itemView.findViewById(R.id.tv_groupid)



            tvGroupName.text = grupo.group_name
            tvGroupId.text = grupo.id.toString()
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

                    GroupObject.setGroup(listaGrupos[pos].id, listaGrupos[pos].group_name)
                    //TODO("Lanzar el activity de las opciones del grupo")
                    val intent = Intent(context, GroupActivity::class.java)
                    context.startActivity(intent)

                    //Toast.makeText(context, this.itemView.findViewById<TextView>(R.id.tv_nombre_grupo).text, Toast.LENGTH_SHORT).show()
                    //Lanzamos el intent para abrir el detall
                    /*val  activityIntent =  Intent(context, ChatIndividualActivity::class.java)
                    activityIntent.putExtra("idUsuario", this.itemView.findViewById<TextView>(R.id.tv_userid).text)
                    activityIntent.putExtra("nombreUsuario", this.itemView.findViewById<TextView>(R.id.tv_nombre_contacto).text)
                    context.startActivity(activityIntent)*/
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val miView = LayoutInflater.from(parent.context).inflate(R.layout.item_grupos, parent, false)
        return GroupViewHolder(miView)
    }

    override fun onBindViewHolder(holder: AdaptadorGrupos.GroupViewHolder, position: Int) {
        holder.pos = position
        holder.asignarInformacion(listaGrupos[position])

    }

    override fun getItemCount(): Int = listaGrupos.size

}