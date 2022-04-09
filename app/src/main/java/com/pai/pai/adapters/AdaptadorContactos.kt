package com.pai.pai.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.R
import com.pai.pai.models.Message
import com.pai.pai.models.User
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class AdaptadorContactos(private val listaUsuarios: MutableList<User>):
    RecyclerView.Adapter<AdaptadorContactos.ContactViewHolder>() {

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun asignarInformacion(usuario: User){

            val tvUsername = itemView.findViewById<TextView>(R.id.tv_nombre_contacto)
            val ivUser = itemView.findViewById<ImageView>(R.id.iv_contacto)
            val contenedorContacto = itemView.findViewById<LinearLayout>(R.id.contenedor_contacto)

            tvUsername.text = usuario.username
            //TODO agregar imagen del usuario ivUser = blalvlavla


            val params = contenedorContacto.layoutParams

            val newParams = FrameLayout.LayoutParams(
                params.width,
                params.height
            )
            contenedorContacto.layoutParams = newParams
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