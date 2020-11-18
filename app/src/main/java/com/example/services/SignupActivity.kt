package com.example.services

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.services.models.User
import com.example.services.shared.GetCurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity: AppCompatActivity() {

    lateinit var rg_gender:RadioGroup
    lateinit var rb_male:RadioButton
    lateinit var rb_female:RadioButton
    lateinit var rb_other:RadioButton
    lateinit var firstName: Editable
    lateinit var lastName:Editable
    lateinit var gender:Any
    lateinit var email:Editable
    lateinit var city:Editable
    lateinit var address:Editable
    lateinit var phone:Editable
    lateinit var password:Editable
    lateinit var passwordConfirm:Editable

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val signInText:TextView = findViewById(R.id.signin_navigator)
        signInText.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        rg_gender = findViewById(R.id.radio_gender)
        rb_female = findViewById(R.id.radioFemale)
        rb_male = findViewById(R.id.radioMale)
        rb_other = findViewById(R.id.radioTrans)

        val signUpButton:Button = findViewById(R.id.signup_button)
        signUpButton.setOnClickListener{
            firstName = findViewById<EditText>(R.id.edittext_first_name).text
            lastName = findViewById<EditText>(R.id.edittext_last_name).text
            gender = getGender()
            email = findViewById<EditText>(R.id.edittext_email).text
            city = findViewById<EditText>(R.id.edittext_city).text
            address = findViewById<EditText>(R.id.edittext_address).text
            phone = findViewById<EditText>(R.id.edittext_phone).text
            password = findViewById<EditText>(R.id.edittext_password).text
            passwordConfirm = findViewById<EditText>(R.id.edittext_password_confirm).text
            registerUser()
        }

    }
    private fun registerUser(){
        if(firstName.isEmpty()||lastName.isEmpty()||email.isEmpty()||city.isEmpty()||address.isEmpty()||phone.isEmpty()||password.isEmpty()||passwordConfirm.isEmpty()){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return
        }
        if(password.toString()!=passwordConfirm.toString()){
            Toast.makeText(this, "Passwords to not match", Toast.LENGTH_SHORT).show();
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.toString(),password.toString())
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Logs","Account created ${FirebaseAuth.getInstance().currentUser?.uid}")
                    uploadUser()
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Failed to Register: ${it.message}",Toast.LENGTH_SHORT).show()
                }
    }

    private fun uploadUser(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid==null)return
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,firstName.toString(),lastName.toString(),gender.toString(),email.toString(),phone.toString(),city.toString(),address.toString(),"false","NULL")
        ref.setValue(user)
                .addOnSuccessListener {
                    clearFields()
                    GetCurrentUser(this)
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Failed to Register: ${it.message}",Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().currentUser?.delete()
                }
    }

    private  fun getGender():String{
        if(rb_female.isChecked)
            return "female"
        else if(rb_male.isChecked)
            return "male"
        else return "other"
    }
    private  fun clearFields(){
        firstName.clear()
        lastName.clear()
        email.clear()
        city.clear()
        address.clear()
        phone.clear()
        password.clear()
        passwordConfirm.clear()
    }
}