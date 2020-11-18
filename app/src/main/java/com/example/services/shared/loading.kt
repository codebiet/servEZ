package com.example.services.shared

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import com.example.services.R

class LoadingDialog {

    var activity:Activity
    lateinit var dialog: AlertDialog
    constructor(myActivity:Activity){
        activity = myActivity
    }

    fun startLoadingDialog(){
        Log.d("Logs","Started Loading")
        val builder:AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_layout,null))
        builder.setCancelable(false)
        dialog = builder.create()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

}