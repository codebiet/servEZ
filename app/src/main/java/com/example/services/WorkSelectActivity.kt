package com.example.services

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.services.models.Worker
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class WorkSelectActivity : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_select)

        val activeWorkerView:androidx.recyclerview.widget.RecyclerView = findViewById(R.id.active_worker_view)
        activeWorkerView.adapter = adapter
        loadWorkers()
    }

    private fun loadWorkers(){

        val bundle = intent.extras
        val workerType = bundle!!.getString("workerType")
        val ref = FirebaseDatabase.getInstance().getReference("/workers/$workerType")
        Log.d("Logs","$workerType")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val worker = snapshot.getValue(Worker::class.java)
                Log.d("Logs", "worker found with id ${worker?.firstName}")
                if (worker != null) {
                    Log.d("Logs", "Worker is not null")
                    adapter.add(WorkerTile(worker))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    class WorkerTile(val worker: Worker): Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.worker_name).text = worker.firstName+" " +worker.lastName
            viewHolder.itemView.findViewById<TextView>(R.id.worker_experience).text = worker.experience
            viewHolder.itemView.findViewById<TextView>(R.id.worker_description).text = worker.description
            viewHolder.itemView.findViewById<TextView>(R.id.worker_job_done_count).text ="Jobs Done "+ worker.jobDoneCount.toString()
            viewHolder.itemView.findViewById<TextView>(R.id.worker_rating).text = worker.rating.toString()+"%"
            var name = worker.serviceType
            val img = viewHolder.itemView.findViewById<ImageView>(R.id.woker_profile_img)
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

        override fun getLayout(): Int {
            return R.layout.worker_tile
        }
    }

}