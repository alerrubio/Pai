package com.pai.pai.models

object UserObject {

    private var userId : String = ""
    private var username : String ? = ""

    fun setUser(id: String, name: String?){
        this.userId = id
        this.username = name
    }

    fun getId(): String{
        return this.userId
    }

    fun getName(): String?{
        return this.username
    }


}