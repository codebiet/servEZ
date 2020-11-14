package com.example.services.shared

import com.example.services.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var currentUser: User?=null

fun GetCurrentUser(){

    var uid = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            currentUser = dataSnapshot.getValue(User::class.java)
        }
        override fun onCancelled(databaseError: DatabaseError) {
        }
    }
    ref.addListenerForSingleValueEvent(postListener)
}