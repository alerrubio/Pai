package com.pai.pai.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.R
import com.pai.pai.models.Message
import java.text.SimpleDateFormat
import java.util.*

class AdaptadorChat(private val listaMensajes: MutableList<Message>):
    RecyclerView.Adapter<AdaptadorChat.ChatViewHolder>() {

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun asignarInformacion(mensaje: Message){

            val tvUser = itemView.findViewById<TextView>(R.id.tv_User)
            val tvMensaje = itemView.findViewById<TextView>(R.id.tv_Message)
            val tvFecha = itemView.findViewById<TextView>(R.id.tv_Hour)
            val contenedorMensaje = itemView.findViewById<LinearLayout>(R.id.contenedorMensaje)

            tvUser.text = mensaje.from
            tvMensaje.text = mensaje.contenido

            val dateFormater = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
            val fecha = dateFormater.format(Date(mensaje.timeStamp as Long))

            tvFecha.text = fecha

            val params = contenedorMensaje.layoutParams

            if (mensaje.esMio) {

                val newParams = FrameLayout.LayoutParams(
                    params.width,
                    params.height,
                    Gravity.END
                )
                contenedorMensaje.layoutParams = newParams

            } else {

                val newParams = FrameLayout.LayoutParams(
                    params.width,
                    params.height,
                    Gravity.START
                )
                contenedorMensaje.layoutParams = newParams
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val miView = LayoutInflater.from(parent.context).inflate(R.layout.messages_chat, parent, false)
        return ChatViewHolder(miView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.asignarInformacion(listaMensajes[position])


    }

    override fun getItemCount(): Int = listaMensajes.size
}