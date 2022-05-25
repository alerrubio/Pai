package com.pai.pai.models

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.database.Exclude

class User (
    var id: String = "",
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var first_name: String = "",
    var last_name: String = "",
    var m_last_name: String = "",
    var carrera: String = ""
        ){
    @Exclude
    var tareas : Boolean = java.lang.Boolean.FALSE
    var chatIndividual : Boolean = java.lang.Boolean.FALSE
    lateinit var image: Bitmap
}