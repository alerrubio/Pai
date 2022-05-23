package com.pai.pai.models

object Miembros {

    private var members: MutableList<String> ?= null

    fun setMember(id: String): Boolean{

        if(this.members!=null){
            for(member in this.members!!){
                if(member == id){
                    return false
                }
            }
        }

        this.members?.add(id)
        return true
    }

}