package com.example.services

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdaptor(private val context: Activity,private val title: Array<String> , private val description:Array<String>) : ArrayAdapter<String>(context,R.layout.list_item,title){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflator = context.layoutInflater
        val rowView = inflator.inflate(R.layout.list_item,null,true)
        val titleText = rowView.findViewById<TextView>(R.id.heading) 
        val subtitleText = rowView.findViewById<TextView>(R.id.subtitle)
        
        titleText.text = title[position]
        subtitleText.hint = description[position]
        return  rowView ;
    }
}