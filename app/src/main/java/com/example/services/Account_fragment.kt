package com.example.services

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.services.shared.currentUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.*

class Account_fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_account,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(currentUser != null) {
            name_account.text = currentUser?.firstName + " " + currentUser?.lastName
            name_account.setTextColor(Color.parseColor("#233B5D"))
        }

        log_out_account.setOnClickListener{

            FirebaseAuth.getInstance().signOut()
            val intent =  Intent(activity, MainActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        provide_services_account.setOnClickListener {
            val intent = Intent(activity,ProviderRegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() : Account_fragment = Account_fragment()
    }
}