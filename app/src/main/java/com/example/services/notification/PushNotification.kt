package com.example.services.notification

data class PushNotification(
        val data: NotificationData,
        val to: String
)