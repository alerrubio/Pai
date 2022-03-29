package com.pai.pai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.R

class AdaptadorChat: RecyclerView.Adapter<AdaptadorChat.ChatViewHolder>() {
    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val miView = LayoutInflater.from(parent.context).inflate(R.layout.messages_chat, parent, false)

        return ChatViewHolder(miView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val tvNombre = holder.itemView.findViewById<TextView>(R.id.tv_User)
        val tvMensaje = holder.itemView.findViewById<TextView>(R.id.tv_Message)
        val tvHora = holder.itemView.findViewById<TextView>(R.id.tv_Hour)

        tvNombre.text = when(position){
            0 -> "A"
            1 -> "B"
            2 -> "C"
            3 -> "D"
            else -> "Zzzz"
        }

    }

    override fun getItemCount(): Int {
        return 10;
    }
}