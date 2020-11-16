package com.example.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.Button
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(rootMessage: RemoteMessage) {
       Log.d("tag", "recieved") ;

    }



}