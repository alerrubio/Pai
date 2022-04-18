package com.pai.pai.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pai.pai.R
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.adapters.AdaptadorContactos
import com.pai.pai.models.Message
import com.pai.pai.models.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactosFragment : Fragment() {


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

                    usuarios.add(usuario)
                }

                if (usuarios.size > 0) {
                    contactosAdaptador?.notifyDataSetChanged()
                    rv_contactos.smoothScrollToPosition(usuarios.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context, "Error al leer los contactos", Toast.LENGTH_SHORT).show()
            }
        })
    }

}