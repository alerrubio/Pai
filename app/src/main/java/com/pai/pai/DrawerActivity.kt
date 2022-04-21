package com.pai.pai

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pai.pai.adapters.ViewPagerAdapater
import com.pai.pai.fragments.ChatFragment

class DrawerActivity : AppCompatActivity() {

    private lateinit var nombreUsuario: String
    private val adapter by lazy{ ViewPagerAdapater(this) }

    fun cambiarFragmento(fragmentoNuevo: Fragment, tag: String){

        val fragmentoAnterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoAnterior == null){

            supportFragmentManager.beginTransaction().replace(R.id.contenedor, fragmentoNuevo).commit()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

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
                R.id.opc_chats -> {
                    cambiarFragmento(ChatFragment(), "ChatFragment")
                }
                R.id.opc_chat_ind -> {
                    cambiarFragmento(ChatFragment(), "ChatFragment")
                }
                else -> {
                    TODO()
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
                        tab.text =  "Chats"
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
}