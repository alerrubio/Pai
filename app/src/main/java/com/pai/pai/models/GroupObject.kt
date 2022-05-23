package com.pai.pai.models

object GroupObject {
    private var groupId : Long = 0
    private var groupName : String = ""

    fun setGroup(id: Long, name: String){
        this.groupId = id
        this.groupName = name
    }

    fun getId(): Long{
        return this.groupId
    }

    fun getName(): String{
        return this.groupName
    }



}