package com.example.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        verifyIfLoggedIn()
        val signoutButton: TextView = findViewById(R.id.sign_out)
        signoutButton.setOnClickListener{
          signout()
        }

    }
    private fun verifyIfLoggedIn(){
        var uid = FirebaseAuth.getInstance().uid
        if (uid==null){
            val intent = Intent(this, MainActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    private  fun signout(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, MainActivity::class.java )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}