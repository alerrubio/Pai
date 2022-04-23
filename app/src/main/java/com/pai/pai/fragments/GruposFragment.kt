package com.pai.pai.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pai.pai.R
import com.pai.pai.adapters.AdaptadorContactos
import com.pai.pai.adapters.AdaptadorGrupos
import com.pai.pai.models.Grupos
import com.pai.pai.models.User

class GruposFragment: Fragment() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    private var context2: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context2 = context
    }


    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("groups")
    private val grupos = mutableListOf<Grupos>()
    private var gruposAdaptador: AdaptadorGrupos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_grupos, container, false)


        this.gruposAdaptador = AdaptadorGrupos(grupos, this.context2!!)
        getGroups(view)

        return view
    }

    private fun getGroups(view: View) {

        val rv_grupos = view.findViewById<RecyclerView>(R.id.rv_grupos)
        rv_grupos.adapter = gruposAdaptador

        userRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                grupos.clear()

                for (snap in snapshot.children) {

                    val grupo: Grupos = snap.getValue(Grupos::class.java) as Grupos
                    //Solo lo agrego para verificar
                    grupos.add(grupo)

                    //TODO("Verificar que el usuario esté en el grupo")?
                    /*if(grupo.id != user!!.uid){
                        grupos.add(grupo)
                    }*/
                }

                if (grupos.size > 0) {
                    gruposAdaptador?.notifyDataSetChanged()
                    rv_grupos.smoothScrollToPosition(grupos.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context, "Error al leer los contactos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}