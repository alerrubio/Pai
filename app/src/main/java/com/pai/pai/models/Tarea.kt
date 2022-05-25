package com.pai.pai.models

import com.google.firebase.database.Exclude

class Tarea(
    var id: String = "",
    var name: String = "",
    var descripcion: String = "",
    var carrera: String = ""
) {
    @Exclude
    var checked: Boolean = false

}