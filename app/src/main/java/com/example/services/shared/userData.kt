package com.example.services.shared

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.services.HomeActivity
import com.example.services.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


var currentUser: User?=null

fun GetCurrentUser(context: Context){
    val activity = context as Activity
    val loadingDialog = LoadingDialog(activity)

    Log.d("Logs","Start Loading")
    loadingDialog.startLoadingDialog()

    var uid = FirebaseAuth.getInstance().uid
    var ref:DatabaseReference?=null
    if(ref==null){
        ref = FirebaseDatabase.getInstance().getReference("users/$uid")
    }
    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            loadingDialog.dismissDialog()
            Log.d("Logs","Stop Loading")
            currentUser = dataSnapshot.getValue(User::class.java)
            Utils.startActivity(context, HomeActivity::class.java)
        }
        override fun onCancelled(databaseError: DatabaseError) {
        }
    }
    ref.addListenerForSingleValueEvent(postListener)
}

class Utils {

    companion object {
        fun startActivity(context: Context, clazz: Class<*>) {
            val intent = Intent(context, clazz)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

}