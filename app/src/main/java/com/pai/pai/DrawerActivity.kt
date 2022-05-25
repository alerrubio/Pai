package com.pai.pai

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fcfm.poi.encriptacin.CifradoTools
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pai.pai.adapters.ViewPagerAdapater
import com.pai.pai.fragments.ChatFragment
import com.pai.pai.models.*
import java.io.File

class DrawerActivity : AppCompatActivity() {

    private lateinit var nombreUsuario: String
    private val adapter by lazy{ ViewPagerAdapater(this) }
    val presenceRef = Firebase.database.getReference("onlineusers/")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val database = FirebaseDatabase.getInstance()
    var StorageRef = FirebaseStorage.getInstance().reference

    private lateinit var picdrawer: ImageView

    fun cambiarFragmento(fragmentoNuevo: Fragment, tag: String){

        val fragmentoAnterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoAnterior == null){

            supportFragmentManager.beginTransaction().replace(R.id.contenedor, fragmentoNuevo).commit()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)
        presenceRef.onDisconnect().setValue(user!!.uid)

        userConnection()
        getImage()

        val miNav = findViewById<NavigationView>(R.id.nav)
        val miDrawer = findViewById<DrawerLayout>(R.id.drawer)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, miDrawer, toolbar, R.string.app_name, R.string.app_name)
        miDrawer.addDrawerListener(toggle)
        toggle.syncState()

        val header = miNav.getHeaderView(0)
        picdrawer = header.findViewById(R.id.iv_header_pp)
        val namedrawer = header.findViewById<TextView>(R.id.tv_header_username)

        namedrawer.text = UserObject.getUsername()
        picdrawer.setImageBitmap(UserObject.getUri())


        nombreUsuario = intent.getStringExtra("username") ?: "sin_nombre"

        miNav.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.opc_perfil -> {
                    val intent = Intent(this@DrawerActivity, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.opc_logros -> {
                    val intent = Intent(this@DrawerActivity, GamificationActivity::class.java)
                    startActivity(intent)
                }
                R.id.opc_logout -> {
                    UserObject.logOut()
                    GroupObject.logOut()
                    SubgrupoObject.logOut()
                    Miembros.logOut()
                    //disconnectUser()
                    val intent = Intent(this@DrawerActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            miDrawer.closeDrawer(GravityCompat.START)

            true
        }

        //nav tablelayout
        val pagerMain =  findViewById<ViewPager2>(R.id.pager)
        pagerMain.adapter =  this.adapter

        val tab_layoutMain =  findViewById<TabLayout>(R.id.tab_layout)

        //Aqui ya sabe quien es nuestro tab, y quien nuestro pager
        val tabLayoutMediator =  TabLayoutMediator(tab_layoutMain,pagerMain
            , TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0-> {
                        tab.text =  "Grupos"
                        tab.setIcon(R.drawable.ic_chat_bubble_24)
                    }
                    1-> {
                        tab.text =  "Contactos"
                        tab.setIcon(R.drawable.ic_person_24)
                    }
                    2-> {
                        tab.text =  "Tareas"
                        tab.setIcon(R.drawable.ic_event_note_24)
                    }
                }
            })
        tabLayoutMediator.attach()

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
                Log.w(TAG, "Listener was cancelled at .info/connected")
            }
        })
    }

    fun disconnect(){

        presenceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ConnectedUsers.deleteMembers()

                for (snap in snapshot.children) {
                    val uid: String = snap.child("uid").getValue(String::class.java) as String

                    if (uid == user!!.uid){
                        snap.ref.removeValue()
                    }
                    
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled at .info/connected")
            }
        })
    }

    fun getImage(){
        lateinit var pathReference: StorageReference
        var localFile = File.createTempFile("tempImg", "png")

        pathReference = StorageRef.child("images/users/" + UserObject.getUri())
        pathReference.getFile(localFile).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            UserObject.setUri(bitmap)
        }
        .addOnFailureListener{
            Log.e("ERROR ", it.toString())
        }
    }
}