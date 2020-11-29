package com.example.services.signInRegister

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface.BOLD
import android.location.LocationManager
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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.services.R
import com.example.services.shared.GetCurrentUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(FirebaseAuth.getInstance().currentUser?.uid != null) {
            GetCurrentUser(this)
        }
        val buttonLogin:Button = findViewById(R.id.button_login)
        buttonLogin.setOnClickListener{
            performLogin()
        }

        val string:SpannableString = SpannableString("Don't have an account? Sign Up") ;

        val intent = Intent(this, SignupActivity::class.java)
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
        textView6.movementMethod = LinkMovementMethod.getInstance();
        checkPermission()
    }
    var Accesslocation=0
    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),Accesslocation)
                return
            }
        }
        getuserlocation()


    }
    fun getuserlocation(){
        Toast.makeText(this,"PermissionGranted", Toast.LENGTH_LONG).show()



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            Accesslocation->{
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    getuserlocation()
                }
                else{
                    Toast.makeText(this,"User Location Access On", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun performLogin(){
        val email = findViewById<EditText>(R.id.email_login).text
        val password = findViewById<EditText>(R.id.password_login).text
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(),password.toString())
            .addOnSuccessListener {
                email.clear()
                password.clear()
                GetCurrentUser(this)
            }
                .addOnFailureListener{
                    Log.d("Logs","failed to login ${it.message}")
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
                }
    }
}

