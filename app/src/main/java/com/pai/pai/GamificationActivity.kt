package com.pai.pai

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pai.pai.adapters.AdaptadorGrupos
import com.pai.pai.adapters.AdaptadorMiembros
import com.pai.pai.adapters.AdaptadorSubgrupos
import com.pai.pai.models.GroupObject
import com.pai.pai.models.Subgrupo
import com.pai.pai.models.User
import com.pai.pai.models.UserObject
import java.io.File

class GamificationActivity:  AppCompatActivity() {

    private var gruposAdaptador: AdaptadorSubgrupos? = null
    private lateinit var rvSubgroups: RecyclerView
    private val subgrupos = mutableListOf<Subgrupo>()

    private val database = FirebaseDatabase.getInstance()
    private var rama = "groups/id"+ GroupObject.getId().toString()+"/Subgrupos"
    private val subRef = database.getReference(rama)
    var StorageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamification)

        val ivChatDisabled = findViewById<ImageView>(R.id.iv_ChatDisabled)
        val ivTareasDisabled = findViewById<ImageView>(R.id.iv_TareaDisabled)



        if (UserObject.getTareas()){
            getImage("1.png",ivTareasDisabled)
        }else{
            getImage("3.png",ivTareasDisabled)
        }

        if (UserObject.getChat()){
            getImage("2.png",ivChatDisabled)
        }else{
            getImage("4.png",ivChatDisabled)
        }


        val toolbar = findViewById<Toolbar>(R.id.tb_gamification)
        toolbar.title = "Logros"
        setSupportActionBar(toolbar)

        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    fun getImage(path: String, iv: ImageView){
        lateinit var pathReference: StorageReference
        var localFile = File.createTempFile("tempImg", "png")

        pathReference = StorageRef.child("images/gamification/" + path)
        pathReference.getFile(localFile).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            iv.setImageBitmap(bitmap)
        }
            .addOnFailureListener{
                Log.e("ERROR ", it.toString())
            }
    }

}