package com.example.services.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(rootMessage: RemoteMessage) {
       Log.d("tag", "recieved") ;

    }



}