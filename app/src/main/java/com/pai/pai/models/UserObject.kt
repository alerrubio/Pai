package com.pai.pai.models

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.text.BoringLayout
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream

object UserObject {

    private var userId : String = ""
    private var username : String ? = ""
    private var email : String = ""
    private var pass : String = ""
    private var encript : Boolean = true
    private var carrera : String = ""
    private var tareas : Boolean = java.lang.Boolean.FALSE
    private var chatIndividual : Boolean = java.lang.Boolean.FALSE
    private var image: Bitmap ? = null

    fun setUser(id: String, name: String?, email: String, pass: String,  carrera: String){
        this.userId = id
        this.username = name
        this.email = email
        this.pass = pass
        this.carrera = carrera
    }

    fun logOut(){
        this.userId = ""
        this.username = ""
        this.email = ""
        this.pass = ""
        this.carrera = ""
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

    fun getCarrera(): String{
        return this.carrera
    }

    fun setTareas(tareas: Boolean){
        this.tareas = tareas
    }

    fun getTareas(): Boolean{
        return this.tareas
    }

    fun setChat(chat: Boolean){
        this.chatIndividual = chat
    }

    fun getChat(): Boolean{
        return this.chatIndividual
    }

    fun getUsername(): String{
        return this.username!!
    }

    fun getUri(): Bitmap?{


        var bytes = ByteArrayOutputStream()

        this.image?.compress(Bitmap.CompressFormat.PNG, 0, bytes)

        //var byteImage = bytes.toByteArray()

        return this.image
    }

    fun setUri(image: Bitmap){
        this.image = image
    }

    fun hasImage(): Boolean{
        if (this.image != null)
            return true
        else
            return false
    }
}