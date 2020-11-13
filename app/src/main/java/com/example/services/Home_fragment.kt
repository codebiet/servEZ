package com.example.services

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

class Home_fragment() : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        //verifyIfLoggedIn()
        /*sign_out.setOnClickListener{
          signout()

        }*/
        return view ;
    }


    companion object {
        fun newInstance() : Home_fragment = Home_fragment()
    }
    private  fun signout(){
        FirebaseAuth.getInstance().signOut()

        //val intent =  Intent(this, MainActivity::class.java )
        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        //startActivity(intent)
    }
    private fun verifyIfLoggedIn(){
        var uid = FirebaseAuth.getInstance().uid
        if (uid==null){
            val s: Context? = getContext()
            //val intent = Intent(s, MainActivity::class.java )
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
          //  startActivity(intent)
        }
    }
}