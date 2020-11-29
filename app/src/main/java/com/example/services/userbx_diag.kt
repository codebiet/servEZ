package com.example.services

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.Window
import com.example.services.messages.ChatActivity
import com.example.services.models.User
import com.example.services.shared.Utils
import com.example.services.shared.Utils.Companion.startActivity
import com.example.services.shared.currentUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.userbox_layout.*
import kotlinx.android.synthetic.main.worker_tile.view.*


class UserBox{
    var activity: Activity
    var user:User
    constructor(myActivity: Activity, myUser:User){
        activity = myActivity
        user = myUser
    }

    fun startDialog() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.userbox_layout)

        dialog.user_name.text = user.firstName + " " + user.lastName
        dialog.user_city.text = user.city
        dialog.user_phone.text = user.phone
        dialog.show()
        Picasso.get().load(user.profileImgURL).into(dialog.user_pic)
        dialog.user_phone.setOnClickListener{
            makeCall(user.phone)
        }
        dialog.call_user.setOnClickListener{
            makeCall(user.phone)
        }
        dialog.user_message.setOnClickListener{
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra("MY_ID", currentUser!!.uid)
            intent.putExtra("RCV_ID", user.uid)
            intent.putExtra("MY_CLASS", currentUser)
            activity.startActivity(intent)
        }
    }

    private  fun makeCall(mobileNumber:String){
        val call = Intent(Intent.ACTION_DIAL);
        call.data = (Uri.parse("tel:$mobileNumber"))
        activity.startActivity(call)
    }

}