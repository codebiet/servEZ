package com.example.services.models

class Worker(val workerUID:String,val firstName: String,val lastName:String,val serviceType:String,val experience:String,
             val description:String,val isAvailable:String,val jobDoneCount:Int,val successJobCount:Int,val rating:Int) {
    constructor():this("","","","","","","",0,0,0)
}