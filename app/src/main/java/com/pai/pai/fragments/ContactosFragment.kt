package com.pai.pai.fragments

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.pai.pai.R
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.adapters.AdaptadorContactos
import com.pai.pai.models.ConnectedUsers
import com.pai.pai.models.Message
import com.pai.pai.models.User

class ContactosFragment : Fragment() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    val presenceRef = Firebase.database.getReference("onlineusers/")
    private var context2: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context2 = context
    }


    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users")
    private val usuarios = mutableListOf<User>()
    private var contactosAdaptador:AdaptadorContactos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userConnection()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_contactos, container, false)


        this.contactosAdaptador = AdaptadorContactos(usuarios, this.context2!!)
        getContacts(view)

        return view
    }

    private fun getContacts(view: View) {

        val rv_contactos = view.findViewById<RecyclerView>(R.id.rv_contactos)
        rv_contactos.adapter = contactosAdaptador

        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                usuarios.clear()

                for (snap in snapshot.children) {

                    val usuario: User = snap.getValue(User::class.java) as User

                    if(usuario.id != user!!.uid){
                        usuarios.add(usuario)
                    }
                }

                if (usuarios.size > 0) {
                    contactosAdaptador?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context, "Error al leer los contactos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun userConnection(){
        val connectedRef = database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ConnectedUsers.deleteMembers()
                val connected = snapshot.getValue<Boolean>() ?: false
                if (connected) {
                    val con = presenceRef.push()

                    // When this device disconnects, remove it
                    con.onDisconnect().removeValue()

                    // Add this device to my connections list
                    con.child("uid").setValue(user!!.uid)
                }

                for (snap in snapshot.children) {

                    val uid: String = snap.child("uid").getValue(String::class.java) as String

                    ConnectedUsers.setconnectedUsers(uid)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Listener was cancelled at .info/connected")
            }
        })
    }
}