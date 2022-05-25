package com.pai.pai

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
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
    private var pickImage = 100
    private val database = FirebaseDatabase.getInstance()
    private var rama = "groups/id"+ GroupObject.getId().toString()+"/Subgrupos"
    private val subRef = database.getReference(rama)
    var StorageRef = FirebaseStorage.getInstance().reference
    var fileGallery: File? = null
    var filePath: String? = ""
    private lateinit var ivPP : ImageView
    private var imageUri: Uri? = null

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val txtName = findViewById<EditText>(R.id.input_username_P)
        val txtEmail = findViewById<EditText>(R.id.input_email_P)
        val txtPass = findViewById<EditText>(R.id.input_contraseña_P)
        ivPP = findViewById(R.id.profile_pic_P)
        var btnFiles = findViewById<Button>(R.id.btn_new_pp)

        //getImage(ivPP)
        txtName.setText(UserObject.getName())
        txtEmail.setText(UserObject.getEmail())
        txtPass.setText(UserObject.getPass())

        val check = findViewById<CheckBox>(R.id.checkBox)
        val btnAceptar = findViewById<Button>(R.id.btn_edit_P)

        if (UserObject.hasImage())
            ivPP.setImageBitmap(UserObject.getUri())
        

        btnFiles.setOnClickListener{
            selectGalleryImage(ImageProvider.GALLERY)

        }

        btnAceptar.setOnClickListener{
            if(check.isChecked){
                Toast.makeText(this, "Se encriptarán los mensajes", Toast.LENGTH_SHORT).show()
                UserObject.setEncript(true)
            }
            else{
                Toast.makeText(this, "Sus mensajes no serán encriptados", Toast.LENGTH_SHORT).show()
                UserObject.setEncript(false)
            }
            if (fileGallery != null){
                subirImagen(fileGallery!!, ivPP)
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

    /*fun getImage(iv: ImageView){
        lateinit var pathReference: StorageReference
        var localFile = File.createTempFile("tempImg", "png")

        pathReference = StorageRef.child("images/users/" + UserObject.getUri())
        pathReference.getFile(localFile).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            iv.setImageBitmap(bitmap)
        }
            .addOnFailureListener{
                Log.e("ERROR ", it.toString())
            }
    }*/

    fun subirImagen(file: File, iv: ImageView) {
        var uriFile = Uri.fromFile(file)

        //UserObject.setUri(uriFile.lastPathSegment!!.toUri())
        val imageRef = StorageRef.child("images/users/${uriFile.lastPathSegment}")
        imageRef.putFile(uriFile)
            .addOnSuccessListener { snap ->

                imageRef.downloadUrl.addOnSuccessListener {


                    //UserObject.setUri(uriFile.lastPathSegment!!.toUri())
                }
                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {

                Toast.makeText(this, "No se pudo subir tu imagen", Toast.LENGTH_SHORT).show()
            }
    }

    private fun selectGalleryImage(provider: ImageProvider){
        val intentGaleria = ImagePicker.with(this)
            .galleryOnly() //User can only select image from Gallery
            .provider(provider)
            .createIntent() //Default Request Code is ImagePicker.REQUEST_CODE

        startActivityForResult(intentGaleria, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == pickImage){
            imageUri = data?.data
            ivPP.setImageURI(imageUri)

            var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)


            UserObject.setUri(bitmap!!)

            fileGallery = ImagePicker.getFile(data)!!
            filePath = ImagePicker.getFilePath(data)!!

            //subirImagen(fileGallery!!, ivPP)
        }

    }

}