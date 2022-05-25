package com.pai.pai.models

object TareaObject {

    private var id : String = ""
    private var name : String = ""
    private var description : String = ""
    private var checked : Boolean = false


    fun setTarea(id:String, name: String, description: String, checked: Boolean){
        this.id = id
        this.name = name
        this.description = description
        this.checked = checked
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

}