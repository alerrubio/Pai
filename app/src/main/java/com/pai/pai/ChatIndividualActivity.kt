package com.pai.pai

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi.encriptacin.CifradoTools
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.pai.pai.adapters.AdaptadorChat
import com.pai.pai.models.Chat
import com.pai.pai.models.Message
import com.pai.pai.models.User
import com.pai.pai.models.UserObject
import java.io.File
import javax.security.auth.callback.Callback
import kotlin.system.exitProcess

class ChatIndividualActivity : AppCompatActivity() {

    private val listMessages = mutableListOf<Message>()
    private val chatAdaptador = AdaptadorChat(listMessages)

    private var textRef = ""



    private var StorageRef = FirebaseStorage.getInstance().reference
    private val database = FirebaseDatabase.getInstance()
    private lateinit var chatRef: DatabaseReference
    private lateinit var msgRef: DatabaseReference
    private lateinit var gamRef: DatabaseReference
    private lateinit var nombreUsuario: String
    private lateinit var idUsuarioDestino: String

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val messageSenderID = user?.uid.toString()

    private var messageRecieverID = ""
    private var pickImage = 100
    private var loadImage = 150
    private var getLocation = 200

    var fileGallery: File? = null
    var fileCamera: File? = null
    var filePath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_individual)

        val dialogClick = { dialog: DialogInterface, which: Int ->
            //Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show()
        }

        if (!UserObject.getChat()){
            updateGamifications("chat")
            UserObject.setChat(true)
            val builder = AlertDialog.Builder(this)
            builder.setNeutralButton("Ok",dialogClick)
            builder.setTitle("Logro de primer chat individual desbloqueado!")
            builder.show()
        }


        idUsuarioDestino = intent.getStringExtra("idUsuario") ?: "sin id"

        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "sin nombre"

        findViewById<TextView>(R.id.tv_UserChat).text = nombreUsuario

        //chatRef = database.getReference("chats/chatsIndividuales") //crea la rama o tabla de chats.
        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar_chat)
        val txtMensaje = findViewById<EditText>(R.id.txtMensaje_chat)
        val btnReturn = findViewById<ImageView>(R.id.btnRegresar_chatInd)
        val btnUbicacion = findViewById<ImageButton>(R.id.btn_ubicacion)
        val btnFiles = findViewById<ImageButton>(R.id.btn_addfile)
        val btnCamera = findViewById<ImageButton>(R.id.btn_add_picture)
        rvMensajes.adapter = chatAdaptador

        createOrFindChat(Chat("", idUsuarioDestino, user!!.uid), idUsuarioDestino, object: Callbacks{
            override fun assingRef(reference: String) {
                chatRef = database.getReference(reference)


                getMessage()
            }
        })
        btnCamera.setOnClickListener{
            selectCameraImage(ImageProvider.CAMERA)
        }
        btnFiles.setOnClickListener{
            selectGalleryImage(ImageProvider.GALLERY)
        }
        btnEnviar.setOnClickListener {
            val mensaje = txtMensaje.text.toString()
            if(mensaje.isNotEmpty()){
                sendMessage(Message("", mensaje, user!!.uid, ServerValue.TIMESTAMP, UserObject.getName().toString()))
                txtMensaje.text.clear()
            }
            if (fileCamera != null) {
                subirImagen(fileCamera!!)
                var uri = Uri.fromFile(fileCamera)
                var msg = Message("", uri.toString(), user!!.uid, ServerValue.TIMESTAMP, UserObject.getName().toString())
                msg.imageFile = true
                sendMessage(msg)
                fileCamera = null
                filePath = ""
                btnCamera.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                /*subirImagen(fileCamera!!)
                var urlString = "gs://paiapp-ce0a6.appspot.com/images/"
                var uri = Uri.fromFile(fileCamera)
                urlString += uri.lastPathSegment
                val pathReference: StorageReference = StorageRef.child("images/"+ uri.lastPathSegment)
                sendMessage(Message("", "Se envió una imagen: " + pathReference.downloadUrl.toString(), user!!.uid, ServerValue.TIMESTAMP, UserObject.getName().toString()))

                fileCamera = null
                filePath = ""
                btnCamera.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))*/
            }
            if (fileGallery != null) {
                subirImagen(fileGallery!!)
                var uri = Uri.fromFile(fileGallery)
                var msg = Message("", uri.toString(), user!!.uid, ServerValue.TIMESTAMP, UserObject.getName().toString())
                msg.imageFile = true
                sendMessage(msg)
                fileGallery = null
                filePath = ""
                btnFiles.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                /*subirImagen(fileGallery!!)
                var urlString = "gs://paiapp-ce0a6.appspot.com/images/"
                var uri = Uri.fromFile(fileGallery)
                urlString += uri.lastPathSegment
                val pathReference: StorageReference = StorageRef.child(urlString + uri.lastPathSegment)
                pathReference.downloadUrl.addOnSuccessListener{
                    sendMessage(Message("", "Se envió una imagen: " + it.toString(), user!!.uid, ServerValue.TIMESTAMP, UserObject.getName().toString()))
                }
                fileGallery = null
                filePath = ""
                btnFiles.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))*/
            }
        }

        btnUbicacion.setOnClickListener{
            revisarPermisos()
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun selectGalleryImage(provider: ImageProvider){
        val intentGaleria = ImagePicker.with(this)
            .galleryOnly() //User can only select image from Gallery
            .provider(provider)
            .createIntent() //Default Request Code is ImagePicker.REQUEST_CODE

        startActivityForResult(intentGaleria, pickImage)
    }
    private fun selectCameraImage(provider: ImageProvider){
        val intentGaleria = ImagePicker.with(this)
             //User can only select image from Gallery
            .cameraOnly()
            .provider(provider)
            .createIntent()

        startActivityForResult(intentGaleria, loadImage)
    }
    private fun createOrFindChat(chat: Chat, idUsuarioDestino: String, callback: Callbacks){

        chatRef = database.getReference("chats/chatsIndividuales") //crea la rama o tabla de chats.

        chatRef.addValueEventListener(object: ValueEventListener {

            var flag = false

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val chat2: Chat = snap.getValue(Chat::class.java) as Chat

                    if((idUsuarioDestino == chat2.user1 || idUsuarioDestino == chat2.user2) && (user!!.uid == chat2.user1 || user!!.uid == chat2.user2)){
                        chatRef = database.getReference("chats/chatsIndividuales/"+chat2.id).child("Mensajes")
                        textRef = "chats/chatsIndividuales/"+chat2.id+"/Mensajes"
                        callback.assingRef(textRef)
                        Toast.makeText(this@ChatIndividualActivity, "Sí existe el chat", Toast.LENGTH_SHORT).show()
                        flag = true
                        break
                    }

                }

                if(!flag){
                    val firebaseMsg = chatRef.push()
                    chat.id = firebaseMsg.key ?: ""
                    Toast.makeText(this@ChatIndividualActivity, "Ya se creó el chat", Toast.LENGTH_SHORT).show()
                    firebaseMsg.setValue(chat)
                    chatRef = database.getReference("chats/chatsIndividuales/"+chat.id+"/Mensajes")
                    textRef = "chats/chatsIndividuales/"+chat.id+"/Mensajes"
                }

            }



            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatIndividualActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(message: Message){
        val firebaseMsg = chatRef.push()

        message.id = firebaseMsg.key ?: ""

        if(UserObject.getEncrypt()){
            message.encriptado = true
            message.contenido = CifradoTools.cifrar(message.contenido, "mensajeEncrypted")
        }

        firebaseMsg.setValue(message)
    }

    private fun getMessage() {

        val rvMensajes = findViewById<RecyclerView>(R.id.rv_Messages)
        rvMensajes.adapter = chatAdaptador


        chatRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listMessages.clear()

                for (snap in snapshot.children) {

                    val mensaje: Message = snap.getValue(Message::class.java) as Message

                    if (mensaje.from == user!!.uid){
                        mensaje.esMio = true
                        mensaje.from = user.displayName.toString()
                    }
                    else {
                        mensaje.from = nombreUsuario
                        mensaje.esMio = false
                    }

                    if(mensaje.encriptado){
                        mensaje.contenido = CifradoTools.descifrar(mensaje.contenido, "mensajeEncrypted")
                    }


                    listMessages.add(mensaje)
                }

                if (listMessages.size > 0) {
                    chatAdaptador.notifyDataSetChanged()
                    rvMensajes.smoothScrollToPosition(listMessages.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatIndividualActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun revisarPermisos() {
        // Apartir de Android 6.0+ necesitamos pedir el permiso de ubicacion
        // directamente en tiempo de ejecucion de la app
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tenemos permiso para la ubicacion
            // Solicitamos permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            // Ya se han concedido los permisos anteriormente
            abrirMapa()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Se requiere aceptar el permiso", Toast.LENGTH_SHORT).show()
                revisarPermisos()
            } else {
                Toast.makeText(this, "Permisio concedido", Toast.LENGTH_SHORT).show()
                abrirMapa()
            }
        }
    }

    private fun abrirMapa() {

        startActivityForResult(Intent(this, MapsActivity::class.java), getLocation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == getLocation) {

            findViewById<TextView>(R.id.txtMensaje_chat).text = data?.getStringExtra("ubicacion") ?: ""

        } else if (resultCode == RESULT_OK && requestCode == pickImage){

            val fileUri = data?.data
            fileGallery = ImagePicker.getFile(data)!!
            filePath = ImagePicker.getFilePath(data)!!
            val btnFiles = findViewById<ImageButton>(R.id.btn_addfile)

            btnFiles.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d0b616"))
            //subirImagen(fileGallery!!)

        }
        else if (resultCode == RESULT_OK && requestCode == loadImage){

            val fileUri = data?.data
            fileCamera = ImagePicker.getFile(data)!!
            filePath = ImagePicker.getFilePath(data)!!
            val btnCamera = findViewById<ImageButton>(R.id.btn_add_picture)

            btnCamera.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d0b616"))
            //subirImagen(fileCamera!!)

        }
        else if (resultCode == RESULT_CANCELED && requestCode == getLocation){

            Toast.makeText(this, "No seleccionaste una dirección", Toast.LENGTH_SHORT).show()

        }else if (resultCode == RESULT_CANCELED && (requestCode == loadImage || requestCode == pickImage)){

            Toast.makeText(this, "No se pudo guardar la imagen", Toast.LENGTH_SHORT).show()

        }
    }

    interface Callbacks{

        fun assingRef(reference: String)
    }

    fun subirImagen(file: File) {
        var uriFile = Uri.fromFile(file)

        val imageRef = StorageRef.child("images/${uriFile.lastPathSegment}")
        imageRef.putFile(uriFile)
            .addOnSuccessListener { snap ->

                imageRef.downloadUrl.addOnSuccessListener {

                    // Guardo en shared la url de la imagen
                    getSharedPreferences("MiArchiv", MODE_PRIVATE)
                        .edit().putString("URL_FOTO", it.toString())
                        .apply()
                }

                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {

                Toast.makeText(this, "No se pudo subir tu imagen", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateGamifications(gamToUpdate: String){
        gamRef = database.getReference("users/")

        gamRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val dbuser: User = snap.getValue(User::class.java) as User

                    if (dbuser.id == user!!.uid){
                        when(gamToUpdate){
                            "chat" -> {
                                var auxId  = snap.key
                                var db = database.getReference("users/"+auxId+"/tareas");
                                db.ref.setValue(true)
                            }
                            "tareas" -> {
                                var auxId  = snap.key
                                var db = database.getReference("users/"+auxId+"/tareas");
                                db.ref.setValue(true)
                            }
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatIndividualActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}