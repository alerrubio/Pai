package com.pai.pai.models

object ConnectedUsers {

    private var connectedUsers: MutableList<String> = mutableListOf()

    fun setconnectedUsers(id: String){
        this.connectedUsers?.add(id)
    }

    fun deleteMember(id: String){
        this.connectedUsers!!.remove(id)
    }

    fun getMembers():MutableList<String>{
        return this.connectedUsers
    }

    fun deleteMembers(){
        this.connectedUsers = mutableListOf()
    }
}