package com.pai.pai

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
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

class ProfileActivity:  AppCompatActivity() {

    private var gruposAdaptador: AdaptadorSubgrupos? = null
    private lateinit var rvSubgroups: RecyclerView
    private val subgrupos = mutableListOf<Subgrupo>()

    private val database = FirebaseDatabase.getInstance()
    private var rama = "groups/id"+ GroupObject.getId().toString()+"/Subgrupos"
    private val subRef = database.getReference(rama)
    var StorageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val txtName = findViewById<EditText>(R.id.input_username_P)
        val txtEmail = findViewById<EditText>(R.id.input_email_P)
        val txtPass = findViewById<EditText>(R.id.input_contraseña_P)

        txtName.setText(UserObject.getName())
        txtEmail.setText(UserObject.getEmail())
        txtPass.setText(UserObject.getPass())

        val check = findViewById<CheckBox>(R.id.checkBox)
        val btnAceptar = findViewById<Button>(R.id.btn_edit_P)

        btnAceptar.setOnClickListener{
            if(check.isChecked){
                Toast.makeText(this, "Se encriptarán los mensajes", Toast.LENGTH_SHORT).show()
                UserObject.setEncript(true)
            }
            else{
                Toast.makeText(this, "Sus mensajes no serán encriptados", Toast.LENGTH_SHORT).show()
                UserObject.setEncript(false)
            }
            val intent = Intent(this, DrawerActivity::class.java)
            startActivity(intent)
        }
        val tb = findViewById<Toolbar>(R.id.tb_profile)
        tb.title = "Perfil"
        setSupportActionBar(tb)

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