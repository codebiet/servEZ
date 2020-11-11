package com.example.services

import android.content.Intent
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLogin:Button = findViewById(R.id.button_login)
        buttonLogin.setOnClickListener{
            performLogin()
        }

        val string:SpannableString = SpannableString("Don't have an account? Sign Up") ;

        val intent:Intent = Intent(this, SignupActivity::class.java)
        val click : ClickableSpan = object :ClickableSpan() {
            override fun onClick(view : View) {
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText= false

            }
        }
        string.setSpan(StyleSpan(BOLD), 23, 30,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(click, 23, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView6.text = string;
        textView6.setMovementMethod(LinkMovementMethod.getInstance()) ;
    }

    private fun performLogin(){
        val email = findViewById<EditText>(R.id.email_login).text
        val password = findViewById<EditText>(R.id.password_login).text
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(),password.toString())
            .addOnSuccessListener {
                Log.d("Logs","$email\n$password\n${FirebaseAuth.getInstance().currentUser?.uid}")
                email.clear()
                password.clear()
                val intent = Intent(this,HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
                .addOnCanceledListener {
                    Log.d("Logs","failed to login")
                }
    }

}

