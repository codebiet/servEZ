package com.example.services.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid:String,val firstName:String,val lastName:String, val gender:String,val email:String,val phone:String, val city:String,val address:String,
val providesService:String,val profileImgURL:String):Parcelable{
    constructor():this("","","","","","","","","","")
}