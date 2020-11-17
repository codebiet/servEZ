package com.example.services

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.services.models.User
import com.example.services.shared.currentUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.signup.view.*
import kotlin.jvm.internal.MagicApiIntrinsics

class Edit_profile:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        first_name_edit_profile.setText(currentUser?.firstName)
        last_name_edit_profile.setText(currentUser?.lastName)
        city_edit_profile.setText(currentUser?.city)
        address_edit_profile.setText(currentUser?.address)
        Log.d("Gender" , currentUser?.gender.toString())
        val g : String = currentUser?.gender.toString()
        if(g == "male")
            radio_gender.check(R.id.radioMale)
        else if (g == "female")
            radio_gender.check(R.id.radioFemale)
        else
            radio_gender.check(R.id.radioTrans)

        back.setOnClickListener{
            finish()
        }

        save_changes.setOnClickListener {
            //TODO update current user details
            Log.d("name", first_name_edit_profile.text.toString())
            val ref = FirebaseDatabase.getInstance().getReference("users/${currentUser?.uid}")

            val myMap : Map<String,String> = mapOf<String, String>("firstName" to first_name_edit_profile.text.toString(),
                "lastName" to last_name_edit_profile.text.toString(),
                    "gender" to g,
                    "city" to  city_edit_profile.text.toString(),
                    "address" to address_edit_profile.text.toString(),
                    )
            ref.updateChildren(myMap)
        }
    }

}