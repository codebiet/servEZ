package com.example.services

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import kotlinx.android.synthetic.main.activity_main.textView6
import kotlinx.android.synthetic.main.signup.*

class SignupActivity : AppCompatActivity() {

    val tile = arrayOf<String>("First name ", "Last Name", "Gender","Email", "Phone", "City", "Address" , "Password")
    val description = arrayOf<String>("Enter your first Name" ,
            "Enter your last Name",
            "Enter your Gender",
            "Enter your email address",
            "Enter your phone number",
            "Enter your city",
            "Enter your address",
            "Set your password")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        val string: SpannableString = SpannableString("Already have an account? Log In") ;

        val intent: Intent = Intent(this, MainActivity::class.java)
        val click : ClickableSpan = object : ClickableSpan() {
            override fun onClick(view : View) {
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText= false

            }
        }
        string.setSpan(StyleSpan(Typeface.BOLD), 25, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(click, 25, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView6.text = string;
        textView6.setMovementMethod(LinkMovementMethod.getInstance()) ;

        val myListAdaptor = MyListAdaptor(this,tile,description)
        listview.adapter = myListAdaptor

        Log.d("Logs","$listview")

        listview.setOnItemClickListener() {
            adapterView, view, position,id->
                val itemAtPos = adapterView.getItemAtPosition(position)
                val itemIdAtPos = adapterView.getItemIdAtPosition(position)
                Log.d("Logs","$itemIdAtPos,$itemAtPos")

        }
        val signup_button : Button = Button(this)
        signup_button.text = "SignUp"
        signup_button.letterSpacing=0.5F
        signup_button.setBackgroundColor(Color.parseColor("#233B5D"))
        listview.addFooterView(signup_button)
        signup_button.setTextColor(Color.parseColor("#FFFFFF"))
        signup_button.setOnClickListener {
            Log.d("Logs","registered ${signup_button.id}")
        }

    }


}