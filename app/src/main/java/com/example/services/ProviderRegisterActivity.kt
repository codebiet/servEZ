package com.example.services

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.view.get

class ProviderRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_register)

        val workSpinner:Spinner = findViewById(R.id.spinner_work_selection)
        val adaptor: ArrayAdapter<String> = ArrayAdapter(this,R.layout.spinner_item,resources.getStringArray(R.array.works))
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        workSpinner.adapter = adaptor

        workSpinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                changePhoto(parent?.selectedItem.toString())
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val experienceSpinner: Spinner = findViewById(R.id.spinner_experience_selection)
        val adapterExp:ArrayAdapter<String> = ArrayAdapter(this,R.layout.spinner_item,resources.getStringArray(R.array.experience))
        adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        experienceSpinner.adapter = adapterExp

        experienceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Logs",parent?.selectedItem.toString())
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    private fun changePhoto(name:String){
        val img = findViewById<ImageView>(R.id.worker_icon)
        if(name=="Mechanic"){
            img.setImageResource(R.drawable.mechanic)
        } else if(name=="Carpenter"){
            img.setImageResource(R.drawable.carpenter)
        }else if(name=="Plumber"){
            img.setImageResource(R.drawable.plumber)
        }else if(name=="Bricklayer"){
            img.setImageResource(R.drawable.brickwork)
        }else if(name=="Electrician"){
            img.setImageResource(R.drawable.electrician)
        }else if(name=="Painter"){
            img.setImageResource(R.drawable.painter)
        }else{
            img.setImageResource(R.drawable.builder)
        }
    }
}