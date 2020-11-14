package com.example.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.services.shared.currentUser
import kotlinx.android.synthetic.main.fragment_account.*

class Account_fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_account,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(currentUser != null)
            name_account.text = currentUser?.firstName + currentUser?.lastName
    }

    companion object {
        fun newInstance() : Account_fragment = Account_fragment()
    }
}