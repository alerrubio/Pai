package com.pai.pai

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pai.pai.adapters.ViewPagerAdapater
import com.pai.pai.fragments.ChatFragment
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Miembros
import com.pai.pai.models.SubgrupoObject
import com.pai.pai.models.UserObject

class DrawerActivity : AppCompatActivity() {

    private lateinit var nombreUsuario: String
    private val adapter by lazy{ ViewPagerAdapater(this) }
    val presenceRef = Firebase.database.getReference("onlineusers/")

    fun cambiarFragmento(fragmentoNuevo: Fragment, tag: String){

        val fragmentoAnterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoAnterior == null){

            supportFragmentManager.beginTransaction().replace(R.id.contenedor, fragmentoNuevo).commit()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        presenceRef.onDisconnect().setValue(UserObject)

        val miNav = findViewById<NavigationView>(R.id.nav)
        val miDrawer = findViewById<DrawerLayout>(R.id.drawer)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, miDrawer, toolbar, R.string.app_name, R.string.app_name)
        miDrawer.addDrawerListener(toggle)
        toggle.syncState()

        nombreUsuario = intent.getStringExtra("username") ?: "sin_nombre"

        miNav.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.opc_perfil -> {
                    cambiarFragmento(ProfileFragment(), "ProfileFragment")
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
                    disconnectUser()
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

    fun connectUser(){
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(TAG, "connected")
                } else {
                    Log.d(TAG, "not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled")
            }
        })
    }

    fun disconnectUser(){
        val firebaseMsg = presenceRef.push()

        presenceRef.onDisconnect().setValue(UserObject)

        firebaseMsg.setValue(UserObject)
    }
}