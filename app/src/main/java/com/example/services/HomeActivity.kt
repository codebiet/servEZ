package com.example.services

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var count1 : Int = 0
    private var count2 : Int = 0
    private var count3 : Int = 0
    private var doubleBackToExitPressedOnce = false
    private val mOnBottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.action_home -> {
                Log.d("aaja","ffaf")
                doubleBackToExitPressedOnce = false
                count1 ++
                count2 = 0
                count3 = 0
                if(count1 == 1) {
                    val account_ragment = Home_fragment.newInstance()
                    openFragment(account_ragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_messaging ->{
                doubleBackToExitPressedOnce = false
                count2++
                count1 = 0
                count3 = 0
                if(count2 == 1) {
                    val account_ragment = Messaging_fragment.newInstance()
                    openFragment(account_ragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_notification-> {
                doubleBackToExitPressedOnce = false
                 val account_ragment = Notification_fragment.newInstance()
                 openFragment(account_ragment)

                return@OnNavigationItemSelectedListener true
            }
            R.id.action_account -> {
                doubleBackToExitPressedOnce = false
                count3 ++
                count1 = 0
                count2 = 0
                if(count3 == 1) {
                    val account_ragment = Account_fragment.newInstance()
                    openFragment(account_ragment)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottom_navigaion_view.setOnNavigationItemSelectedListener(mOnBottomNavigationListener)

        if(savedInstanceState == null) {
            bottom_navigaion_view.selectedItemId = R.id.action_home
            count1++
            count2 = 0
            count3 = 0
        }

    }


    private fun openFragment(fragment : Fragment) {


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)

        transaction.commit()
    }

    override fun onBackPressed() {
        if(doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this,"Please click once more to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}