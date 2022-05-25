package com.pai.pai.adapters

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi.encriptacin.CifradoTools
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pai.pai.ChatActivity
import com.pai.pai.ChatIndividualActivity
import com.pai.pai.R
import com.pai.pai.models.ConnectedUsers
import com.pai.pai.models.Message
import com.pai.pai.models.User
import com.pai.pai.models.UserObject

class AdaptadorContactos(private val listaUsuarios: MutableList<User>, val context: Context):
    RecyclerView.Adapter<AdaptadorContactos.ContactViewHolder>() {

    private lateinit var tvUserId: TextView
    private val presenceRef = Firebase.database.getReference("onlineusers/")
    private lateinit var offlineUsers: MutableList<User>
    private val database = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        fun asignarInformacion(usuario: User){
            val tvUsername = itemView.findViewById<TextView>(R.id.tv_nombre_contacto)
            val ivUser = itemView.findViewById<ImageView>(R.id.iv_contacto)
            val contenedorContacto = itemView.findViewById<LinearLayout>(R.id.contenedor_contacto)
            val statusIcon = itemView.findViewById<ImageButton>(R.id.btn_status)
            val btnCorreo = itemView.findViewById<ImageButton>(R.id.btn_correo)

            tvUserId = itemView.findViewById<TextView>(R.id.tv_userid)

            userConnection(usuario.id,statusIcon)

            tvUsername.text = usuario.username
            tvUserId.text = usuario.id
            //TODO agregar imagen del usuario ivUser = blalvlavla


            val params = contenedorContacto.layoutParams

            val newParams = FrameLayout.LayoutParams(
                params.width,
                params.height
            )
            contenedorContacto.layoutParams = newParams

            btnCorreo.setOnClickListener{
                sendEmail(usuario.email, user!!.email!! )
            }

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

    fun userConnection(userid: String, btnStatus: ImageButton){

        presenceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ConnectedUsers.deleteMembers()

                for (snap in snapshot.children) {

                    val uid: String = snap.child("uid").getValue(String::class.java) as String

                    ConnectedUsers.setconnectedUsers(uid)

                    if (ConnectedUsers.getMembers().contains(userid)){
                        btnStatus.setColorFilter(Color.parseColor("#FF8BC34A"));
                        //btnStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8BC34A"))
                    }else{
                        btnStatus.setColorFilter(Color.parseColor("#3E3C3C"));
                        //btnStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d0b616"))
                    }

                }

                if (ConnectedUsers.getMembers().isEmpty()){
                    btnStatus.setColorFilter(Color.parseColor("#3E3C3C"));
                    //btnStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d0b616"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Listener was cancelled at .info/connected")
            }
        })
    }

    fun sendEmail(to: String, from: String){
        val intentCorreo = Intent(Intent.ACTION_SENDTO)
        intentCorreo.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intentCorreo.data = Uri.parse("mailto:")

        var intentCorreo2 = Intent.createChooser(intentCorreo, null)
        context.startActivity(intentCorreo2)
    }

}