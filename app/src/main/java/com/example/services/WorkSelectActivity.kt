package com.example.services

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isEmpty
import com.example.services.models.User
import com.example.services.models.Worker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_call_worker.*
import kotlinx.android.synthetic.main.activity_work_select.*
import kotlinx.android.synthetic.main.worker_tile.view.*

class WorkSelectActivity : AppCompatActivity() {
    val activeAdapter = GroupAdapter<ViewHolder>()
    val inactiveAdapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_select)

        val activeWorkerView:androidx.recyclerview.widget.RecyclerView = findViewById(R.id.active_worker_view)
        val inactiveWorkerView:androidx.recyclerview.widget.RecyclerView = findViewById(R.id.inactive_worker_view)
        activeWorkerView.adapter = activeAdapter
        inactiveWorkerView.adapter = inactiveAdapter

        loadWorkers()

        if(activeAdapter.itemCount==0){
            activeWorkerView.visibility = View.GONE;
            empty_view.visibility = View.VISIBLE;
        }else{
            activeWorkerView.visibility = View.VISIBLE;
            empty_view.visibility = View.GONE;
        }

        if(inactiveAdapter.itemCount==0){
            inactiveWorkerView.visibility = View.GONE;
            empty_view_bottom.visibility = View.VISIBLE;
        }else{
            inactiveWorkerView.visibility = View.VISIBLE;
            empty_view_bottom.visibility = View.GONE;
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("Logs","${newConfig.orientation}")
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Logs","Landscape")
            //Toast.makeText(baseContext, "Landscape Mode", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Logs","Portrait")
            //Toast.makeText(baseContext, "Portrait Mode", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadWorkers(){

        val bundle = intent.extras
        val workerType = bundle!!.getString("workerType")
        val ref = FirebaseDatabase.getInstance().getReference("/workers/$workerType")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val worker = it.getValue(Worker::class.java)
                    loadWorkerData(worker!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadWorkerData(worker: Worker){

        val activeWorkerView:androidx.recyclerview.widget.RecyclerView = findViewById(R.id.active_worker_view)
        val inactiveWorkerView:androidx.recyclerview.widget.RecyclerView = findViewById(R.id.inactive_worker_view)
        val ref = FirebaseDatabase.getInstance().getReference("users/${worker.workerUID}")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if(user!=null){
                    if(worker.available=="true")
                        activeAdapter.add(WorkerTile(worker, user))
                    else
                        inactiveAdapter.add(WorkerTile(worker, user))

                    if(activeAdapter.itemCount==0){
                        activeWorkerView.visibility = View.GONE;
                        empty_view.visibility = View.VISIBLE;
                    }else{
                        activeWorkerView.visibility = View.VISIBLE;
                        empty_view.visibility = View.GONE;
                    }

                    if(inactiveAdapter.itemCount==0){
                        inactiveWorkerView.visibility = View.GONE;
                        empty_view_bottom.visibility = View.VISIBLE;
                    }else{
                        inactiveWorkerView.visibility = View.VISIBLE;
                        empty_view_bottom.visibility = View.GONE;
                    }

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        ref.addListenerForSingleValueEvent(postListener)
    }


    class WorkerTile(private val worker: Worker, val user: User): Item<ViewHolder>() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.worker_name.text = user.firstName+" " +user.lastName
            viewHolder.itemView.worker_experience.text = worker.experience
            viewHolder.itemView.worker_description.text = worker.description
            viewHolder.itemView.worker_job_done_count.text ="Jobs Done: "+ worker.jobDoneCount.toString()
            viewHolder.itemView.worker_rating.text = worker.rating.toString()+"%"

            when(worker.experience){
                "Beginner" -> viewHolder.itemView.worker_experience.setTextColor(Color.parseColor("#697778"))
                "Specialist" -> viewHolder.itemView.worker_experience.setTextColor(Color.parseColor("#09a0ab"))
                "Expert" -> viewHolder.itemView.worker_experience.setTextColor(Color.parseColor("#076adb"))
            }
            if(worker.rating<=20){
                viewHolder.itemView.worker_rating.setTextColor(Color.parseColor("#c70000"))
            } else if(worker.rating in 21..49){
                viewHolder.itemView.worker_rating.setTextColor(Color.parseColor("#e37a1e"))
            }else if(worker.rating in 50..60){
                viewHolder.itemView.worker_rating.setTextColor(Color.parseColor("#e3d31e"))
            }else if(worker.rating in 61..80){
                viewHolder.itemView.worker_rating.setTextColor(Color.parseColor("#a1bd24"))
            }else{
                viewHolder.itemView.worker_rating.setTextColor(Color.parseColor("#47a326"))
            }

            if(user?.profileImgURL=="NULL"||user?.profileImgURL==null){
                var name = worker.serviceType
                val img = viewHolder.itemView.findViewById<ImageView>(R.id.worker_profile_img)
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
            }else{
                Picasso.get().load(user.profileImgURL).into(viewHolder.itemView.worker_profile_img)
            }

            viewHolder.itemView.setOnClickListener{
                val intent = Intent(viewHolder.itemView.context, CallWorkerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("UserKey", user)
                intent.putExtra("WorkerKey", worker)
                viewHolder.itemView.context.startActivity(intent)
            }

        }

        override fun getLayout(): Int {
            return R.layout.worker_tile
        }
    }

}