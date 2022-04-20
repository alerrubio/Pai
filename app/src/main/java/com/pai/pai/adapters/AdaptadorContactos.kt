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
import com.pai.pai.ChatActivity
import com.pai.pai.ChatIndividualActivity
import com.pai.pai.R
import com.pai.pai.models.User

class AdaptadorContactos(private val listaUsuarios: MutableList<User>, val context: Context):
    RecyclerView.Adapter<AdaptadorContactos.ContactViewHolder>() {

    private lateinit var tvUserId: TextView


    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        fun asignarInformacion(usuario: User){

            val tvUsername = itemView.findViewById<TextView>(R.id.tv_nombre_contacto)
            val ivUser = itemView.findViewById<ImageView>(R.id.iv_contacto)
            val contenedorContacto = itemView.findViewById<LinearLayout>(R.id.contenedor_contacto)
            tvUserId = itemView.findViewById<TextView>(R.id.tv_userid)



            tvUsername.text = usuario.username
            tvUserId.text = usuario.id
            //TODO agregar imagen del usuario ivUser = blalvlavla


            val params = contenedorContacto.layoutParams

            val newParams = FrameLayout.LayoutParams(
                params.width,
                params.height
            )
            contenedorContacto.layoutParams = newParams

        }


        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

            when(v!!.id){
                R.id.Frame->{
                    //Lanzamos el intent para abrir el detall
                    val  activityIntent =  Intent(context, ChatIndividualActivity::class.java)
                    activityIntent.putExtra("idUsuario", this.itemView.findViewById<TextView>(R.id.tv_userid).text)
                    activityIntent.putExtra("nombreUsuario", this.itemView.findViewById<TextView>(R.id.tv_nombre_contacto).text)
                    context.startActivity(activityIntent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val miView = LayoutInflater.from(parent.context).inflate(R.layout.item_contacto, parent, false)
        return ContactViewHolder(miView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.asignarInformacion(listaUsuarios[position])

    }

    override fun getItemCount(): Int = listaUsuarios.size

}