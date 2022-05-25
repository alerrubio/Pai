package com.pai.pai.models

object TareaObject {

    private var id : String = ""
    private var name : String = ""
    private var description : String = ""
    private var checked : Boolean = false
    private var usuarios : MutableList<String> = mutableListOf()


    fun setTarea(id:String, name: String, description: String, checked: Boolean, usuarios:MutableList<String>){
        this.id = id
        this.name = name
        this.description = description
        this.checked = checked
        this.usuarios = usuarios
    }

    fun logOut(){
        this.name = ""
        this.description = ""
        this.checked = false
    }

    fun setChecked(checked : Boolean){
        this.checked = checked
    }

    fun getId(): String{
        return this.id
    }

    fun getName(): String?{
        return this.name
    }

    fun getDescription(): String{
        return this.description
    }

    fun getChecked(): Boolean{
        return this.checked
    }

    fun getUsuarios(): MutableList<String>{
        return this.usuarios
    }

}