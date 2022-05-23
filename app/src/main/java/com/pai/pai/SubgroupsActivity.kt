package com.pai.pai

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.pai.pai.models.GroupObject

class SubgroupsActivity:  AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groupdetails)

        val txtName = findViewById<TextView>(R.id.txt_groupname)
        val btnChat = findViewById<Button>(R.id.btn_groupchat)
        val rvSubgroups = findViewById<RecyclerView>(R.id.rv_groupsub)

        txtName.text = GroupObject.getName()


        val toolbar = findViewById<Toolbar>(R.id.tb_group)
        setSupportActionBar(toolbar)


        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        btnChat.setOnClickListener {
            val intent = Intent(this@SubgroupsActivity, ChatActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}