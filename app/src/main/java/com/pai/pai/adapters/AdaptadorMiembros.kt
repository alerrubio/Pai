package com.pai.pai.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.ChatIndividualActivity
import com.pai.pai.CreateSubgroupActivity
import com.pai.pai.DrawerActivity
import com.pai.pai.R
import com.pai.pai.models.Miembros
import com.pai.pai.models.User
import com.pai.pai.models.UserObject

class AdaptadorMiembros(private val listaUsuarios: MutableList<User>, val context: Context):
    RecyclerView.Adapter<AdaptadorMiembros.ContactViewHolder>() {

    private lateinit var tvUserId: TextView
    private lateinit var contenedorContacto:LinearLayout
    private var members = mutableListOf<String>()



    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        fun asignarInformacion(usuario: User){

            val tvUsername = itemView.findViewById<TextView>(R.id.tv_nombre_contacto)
            val ivUser = itemView.findViewById<ImageView>(R.id.iv_contacto)
            contenedorContacto = itemView.findViewById(R.id.contenedor_contacto)
            tvUserId = itemView.findViewById(R.id.tv_userid)

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
                    val id=this.itemView.findViewById<TextView>(R.id.tv_userid).text.toString()
                    var registro = true
                    var i = 0

                    if(members.isNotEmpty()){

                        if(members.contains(id)){
                            this.itemView.findViewById<FrameLayout>(R.id.Frame).backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d0b616"))
                            members.remove(id)
                            Miembros.deleteMember(id)
                        }
                        else{
                            members.add(id)
                            Miembros.setMember(id)
                            this.itemView.findViewById<FrameLayout>(R.id.Frame).backgroundTintList = ColorStateList.valueOf(Color.parseColor("#a3dabc"))
                        }
                    }
                    else{
                        members.add(id)
                        Miembros.setMember(id)
                        this.itemView.findViewById<FrameLayout>(R.id.Frame).backgroundTintList = ColorStateList.valueOf(Color.parseColor("#a3dabc"))
                    }
                    val intentChat = Intent(context, CreateSubgroupActivity::class.java)
                    intentChat.putExtra("miembros", members.toTypedArray())
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorMiembros.ContactViewHolder {
        val miView = LayoutInflater.from(parent.context).inflate(R.layout.item_contacto, parent, false)
        return ContactViewHolder(miView)
    }

    override fun onBindViewHolder(holder: AdaptadorMiembros.ContactViewHolder, position: Int) {
        holder.asignarInformacion(listaUsuarios[position])
    }

    override fun getItemCount(): Int = listaUsuarios.size

}