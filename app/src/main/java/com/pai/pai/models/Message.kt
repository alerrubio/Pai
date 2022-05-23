package com.pai.pai.models

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

}