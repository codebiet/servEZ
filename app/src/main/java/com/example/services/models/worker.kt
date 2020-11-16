package com.example.services.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Worker(val workerUID:String,val serviceType:String,val experience:String,val description:String,val available:String,
             val jobDoneCount:Int,val successJobCount:Int,val rating:Int): Parcelable {
    constructor():this("","","","","",0,0,0)
}