package com.example.services.models

class Invitation(val id:String, val type:String, val clientID:String, val workerID:String, var status:String, val time:String, val clientName:String, val workerName:String){
    constructor():this("","","","","","","","",)
}