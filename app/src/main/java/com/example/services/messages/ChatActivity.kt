package com.example.services.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.services.R
import com.example.services.models.User

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val myId = intent.getStringExtra("MY_ID")
        val rcvId = intent.getStringArrayExtra("RCV_ID")



    }
}