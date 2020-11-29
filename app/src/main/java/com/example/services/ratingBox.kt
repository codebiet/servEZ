package com.example.services

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.services.models.Invitation
import com.example.services.models.Worker
import com.example.services.shared.Constants
import com.example.services.shared.currentUser
import com.google.firebase.database.*
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.notification_tile_client.view.*
import kotlinx.android.synthetic.main.rating_layout.*

class RatingBox {
    var activity: Activity
    var jobRef: DatabaseReference
    var finalInvitation:Invitation
    var viewHolder:ViewHolder
    constructor(myActivity: Activity,jobReference: DatabaseReference,invitation: Invitation,VH:ViewHolder){
        jobRef = jobReference
        finalInvitation = invitation
        activity = myActivity
        viewHolder = VH
    }

    fun startDialog() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.rating_layout)
        dialog.show()
        dialog.rate_like.setOnClickListener{
            incrementSuccessJob(dialog)
            updateInvitation(dialog)
            dialog.dismiss()
            updateUI()
        }
        dialog.rate_dislike.setOnClickListener{
            updateInvitation(dialog)
            dialog.dismiss()
            updateUI()
        }
    }

    private fun updateUI(){
        viewHolder.itemView.notif_btn.setBackgroundResource(android.R.drawable.btn_default)
        viewHolder.itemView.notif_btn.isEnabled = false
        viewHolder.itemView.notif_btn.text = finalInvitation.status
    }

    private fun incrementSuccessJob(dialog: Dialog){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var worker = dataSnapshot.getValue(Worker::class.java)
                var successJobs: Int? = worker?.successJobCount
                successJobs= successJobs?.plus(1)
                jobRef.child("successJobCount").setValue(successJobs)

                var rating: Int? = worker?.rating
                if (successJobs != null) {
                    rating = (successJobs*100/(worker?.jobDoneCount!!)).toInt()
                }
                jobRef.child("rating").setValue(rating)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        jobRef.addListenerForSingleValueEvent(postListener)
    }

    private fun updateInvitation(dialog: Dialog){
        val invitationFromRef = FirebaseDatabase.getInstance().getReference("/invitations/${finalInvitation.clientID}/${finalInvitation.workerID}/${finalInvitation.id}")
        val invitationToRef = FirebaseDatabase.getInstance().getReference("/invitations/${finalInvitation.workerID}/${finalInvitation.clientID}/${finalInvitation.id}")
        finalInvitation.status = "Rated"
        invitationFromRef.setValue(finalInvitation)
                .addOnSuccessListener {
                    invitationToRef.setValue(finalInvitation)
                            .addOnSuccessListener {
                                Toast.makeText(activity, "Rated", Toast.LENGTH_SHORT).show()
                            }
                }
    }

}