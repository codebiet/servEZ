package com.example.services

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.models.Invitation
import com.example.services.models.User
import com.example.services.models.Worker
import com.example.services.shared.currentUser
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.notification_tile_client.view.*


class Contract_fragment : Fragment() {
    companion object {
        fun newInstance() : Contract_fragment = Contract_fragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_notification, container, false)

    private val adaptor = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager= LinearLayoutManager(context);
        layoutManager.orientation = LinearLayoutManager.VERTICAL;
        notification_recyclerview.layoutManager = layoutManager;
        notification_recyclerview.adapter = adaptor

        loadNotifications(notification_recyclerview)
    }

    private fun loadNotifications(notification_recyclerview:RecyclerView){
        val ref = FirebaseDatabase.getInstance().getReference("/invitations/${currentUser!!.uid}")
        ref.keepSynced(true)
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val ref2 = ref.child(snapshot.key.toString())
                ref2.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val invitation = snapshot.getValue(Invitation::class.java)
                        adaptor.add(LatestNotice(invitation!!,ref2,snapshot.key.toString()))
                        if(adaptor.itemCount>1){
                            notification_recyclerview.scrollToPosition(adaptor.itemCount - 1)
                        }
                    }
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                    }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    class LatestNotice(private val invitation: Invitation, var ref:DatabaseReference, private val snapshotKey:String): Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {

            ref=ref.child(snapshotKey)

            viewHolder.itemView.notif_type.text = "Type: "+invitation.type
            viewHolder.itemView.notif_time.text = invitation.time
            viewHolder.itemView.notif_btn.text = invitation.status
            if(invitation.clientID== currentUser?.uid){
                if(invitation.status!="Accepted"){
                    viewHolder.itemView.notif_btn.isEnabled = false
                }else {
                    viewHolder.itemView.notif_btn.text = "Rate"
                    viewHolder.itemView.notif_btn.setBackgroundColor(Color.parseColor("#1fb529"))
                }
                viewHolder.itemView.notif_title.text = "You invited "+invitation.workerName
                viewHolder.itemView.notif_reject_btn.isEnabled = false
                viewHolder.itemView.notif_reject_btn.visibility = View.GONE

            }else{
                viewHolder.itemView.notif_title.text = invitation.clientName+" invited you"
                if(invitation.status=="Pending"){
                    viewHolder.itemView.notif_reject_btn.setBackgroundColor(Color.parseColor("#d61313"))
                    viewHolder.itemView.notif_btn.text = "Accept"
                    viewHolder.itemView.notif_btn.setBackgroundColor(Color.parseColor("#1fb529"))
                }else{
                    viewHolder.itemView.notif_btn.setBackgroundResource(android.R.drawable.btn_default)
                    viewHolder.itemView.notif_reject_btn.isEnabled = false
                    viewHolder.itemView.notif_reject_btn.visibility = View.GONE
                    viewHolder.itemView.notif_btn.isEnabled = false
                }
            }
            when(invitation.status){
                "Pending"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#dbefff"))
                "Accepted"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#dbffdb"))
                "Rated"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#dbffdb"))
                "Rejected"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffdbdb"))
            }
            val ref2 = FirebaseDatabase.getInstance().getReference("/invitations/${invitation.clientID}/${currentUser!!.uid}/$snapshotKey")

            viewHolder.itemView.notif_reject_btn.setOnClickListener{
                val temp = invitation
                temp.status = "Rejected"
                ref2.setValue(temp)
                ref.setValue(temp)
                        .addOnSuccessListener {
                            Toast.makeText(viewHolder.itemView.context, "Rejected Invitation",Toast.LENGTH_SHORT).show()
                            updateUI(invitation.status,viewHolder)
                        }
            }

            viewHolder.itemView.notif_btn.setOnClickListener{
                if(invitation.clientID== currentUser?.uid){
                    val tempRef = FirebaseDatabase.getInstance().getReference("workers/${invitation.type}/${invitation.workerID}")
                    val ratingDialog = RatingBox(viewHolder.itemView.context as Activity,tempRef,invitation,viewHolder)
                    ratingDialog.startDialog()
                }else{
                    val temp = invitation
                    temp.status = "Accepted"
                    ref2.setValue(temp)
                            .addOnSuccessListener {
                                ref.setValue(temp)
                                        .addOnSuccessListener {
                                            Toast.makeText(viewHolder.itemView.context, "Accepted Invitation",Toast.LENGTH_SHORT).show()
                                            updateUI(invitation.status,viewHolder)
                                            updateWorkerJobCount(invitation.workerID)
                                        }
                            }
                }
            }

            viewHolder.itemView.setOnClickListener{
               var ref:DatabaseReference
                if(invitation.clientID== currentUser?.uid){
                    ref = FirebaseDatabase.getInstance().getReference("users/${invitation.workerID}")
                }else{
                    ref = FirebaseDatabase.getInstance().getReference("users/${invitation.clientID}")
                }
                openDiagOnGetUser(ref,viewHolder)
            }

        }

        private fun openDiagOnGetUser(ref:DatabaseReference,viewHolder: ViewHolder){
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var tempUser = dataSnapshot.getValue(User::class.java)
                    val userBox = tempUser?.let { UserBox(viewHolder.itemView.context as Activity, it) }
                    userBox?.startDialog()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            ref.addListenerForSingleValueEvent(postListener)
        }

        private fun updateWorkerJobCount(workerID:String){
            val ref = FirebaseDatabase.getInstance().getReference("workers/${invitation.type}/$workerID")
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var worker = dataSnapshot.getValue(Worker::class.java)
                    var jobs: Int? = worker?.jobDoneCount
                    jobs = jobs?.plus(1)
                    ref.child("jobDoneCount").setValue(jobs)

                    var successJobs = worker?.successJobCount
                    var rating: Int? = worker?.rating
                    if (successJobs != null) {
                        rating = (successJobs*100/(jobs!!)).toInt()
                    }
                    ref.child("rating").setValue(rating)

                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            ref.addListenerForSingleValueEvent(postListener)
        }

        private fun updateUI(status:String, viewHolder: ViewHolder){
            viewHolder.itemView.notif_reject_btn.isEnabled = false
            viewHolder.itemView.notif_reject_btn.visibility = View.GONE
            viewHolder.itemView.notif_btn.setBackgroundResource(android.R.drawable.btn_default)
            viewHolder.itemView.notif_btn.isEnabled = false
            viewHolder.itemView.notif_btn.text = status

            when(status){
                "Pending"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#dbefff"))
                "Accepted"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#dbffdb"))
                "Rejected"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffdbdb"))
                "Rated"->viewHolder.itemView.setBackgroundColor(Color.parseColor("#dbffdb"))
            }
        }

        override fun getLayout(): Int {
            return R.layout.notification_tile_client
        }
    }
}