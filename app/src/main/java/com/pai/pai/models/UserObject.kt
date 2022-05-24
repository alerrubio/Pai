package com.pai.pai.models

import android.text.BoringLayout

object UserObject {

    private var userId : String = ""
    private var username : String ? = ""
    private var email : String = ""
    private var pass : String = ""
    private var encript : Boolean = true

    fun setUser(id: String, name: String?, email: String, pass: String){
        this.userId = id
        this.username = name
        this.email = email
        this.pass = pass
    }

    fun logOut(){
        this.userId = ""
        this.username = ""
        this.email = ""
        this.pass = ""
    }

    fun setEncript(encrypt : Boolean){
        this.encript = encrypt
    }

    fun getId(): String{
        return this.userId
    }

    fun getName(): String?{
        return this.username
    }

    fun getEmail(): String{
        return this.email
    }

    fun getPass(): String{
        return this.pass
    }

    fun getEncrypt(): Boolean{
        return this.encript
    }

}