package com.example.services

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.services.models.User
import com.example.services.models.Worker
import kotlinx.android.synthetic.main.activity_call_worker.*
import kotlinx.android.synthetic.main.worker_tile.view.*

class CallWorkerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_worker)
        val user = intent.getParcelableExtra<User>("UserKey")
        val worker = intent.getParcelableExtra<Worker>("WorkerKey")

        Log.d("Logs","Opened ${user?.lastName}")

        worker_name_call.text = user?.firstName+" "+user?.lastName
        worker_experience_call.text = worker?.experience
        worker_description_call.text = worker?.description
        worker_rating_call.text = worker?.rating.toString()+"%"
        worker_jobsdone_call.text = "Jobs Done: "+ worker?.jobDoneCount.toString()
        worker_phone_call.text = user?.phone
        worker_city_call.text = user?.city

        when(worker?.experience){
            "Beginner"->worker_experience_call.setTextColor(Color.parseColor("#697778"))
            "Specialist"->worker_experience_call.setTextColor(Color.parseColor("#09a0ab"))
            "Expert"->worker_experience_call.setTextColor(Color.parseColor("#076adb"))
        }

        if(worker?.rating in 0..20){
            worker_rating_call.setTextColor(Color.parseColor("#c70000"))
        } else if(worker?.rating in 21..49){
            worker_rating_call.setTextColor(Color.parseColor("#e37a1e"))
        }else if(worker?.rating in 50..60){
            worker_rating_call.setTextColor(Color.parseColor("#e3d31e"))
        }else if(worker?.rating in 61..80){
            worker_rating_call.setTextColor(Color.parseColor("#a1bd24"))
        }else{
            worker_rating_call.setTextColor(Color.parseColor("#47a326"))
        }

    }


}