package com.pai.pai.models

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.FrameLayout
import com.pai.pai.R

object Miembros {

    private var members: MutableList<String> = mutableListOf()

    fun setMember(id: String){
        this.members?.add(id)

    }

    fun deleteMember(id: String){
        this.members!!.remove(id)
    }

    fun getMembers():MutableList<String>{
        return this.members
    }

}