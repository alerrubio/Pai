package com.pai.pai.models

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.database.Exclude

class Message(
    var id: String = "",
    var contenido: String = "",
    var from: String = "",
    val timeStamp: Any? = null,
    var usuario: String = ""
) {
    @Exclude
    var esMio: Boolean = false
    var encriptado: Boolean = false
    var imageFile: Boolean = false
    var image: String = ""
}