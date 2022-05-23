package com.pai.pai.models

object SubgrupoObject {
    private var groupId : String = ""
    private var groupName : String = ""

    fun setGroup(id: String, name: String){
        this.groupId = id
        this.groupName = name
    }

    fun getId(): String{
        return this.groupId
    }

    fun getName(): String{
        return this.groupName
    }
}