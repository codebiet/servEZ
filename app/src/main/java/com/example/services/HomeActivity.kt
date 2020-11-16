package com.example.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val mOnBottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_home -> {
                Log.d("aaja","ffaf")
                val account_ragment = Home_fragment.newInstance()
                openFragment(account_ragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_messaging ->{
                val account_ragment = Messaging_fragment.newInstance()
                openFragment(account_ragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_notification-> {
                val account_ragment = Notification_fragment.newInstance()
                openFragment(account_ragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_account -> {

                val account_ragment = Account_fragment.newInstance()
                openFragment(account_ragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottom_navigaion_view.setOnNavigationItemSelectedListener(mOnBottomNavigationListener)

        if(savedInstanceState == null)
            bottom_navigaion_view.selectedItemId = R.id.action_home

    }


    private fun openFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}