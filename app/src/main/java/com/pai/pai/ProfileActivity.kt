package com.pai.pai

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var input_user_email = findViewById<EditText>(R.id.input_email_R)
        var input_user_name = findViewById<EditText>(R.id.input_username_PE)
    }


}