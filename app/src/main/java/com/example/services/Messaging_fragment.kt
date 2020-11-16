package com.example.services

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.services.messages.ChatActivity
import com.example.services.models.ChatMessage
import com.example.services.shared.currentUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_messaging.*
import kotlinx.android.synthetic.main.latest_msg_tile.view.*

class Messaging_fragment : Fragment() {

    companion object {
        fun newInstance() : Messaging_fragment = Messaging_fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_messaging,container,false);
    }

    private val adaptor = GroupAdapter<ViewHolder>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Logs","Msg called")
        recyclerview_latest_msg.adapter = adaptor
        setDummy()
        loadLatestMsg()
    }

    private fun setDummy(){
//        adaptor.add(LatestMsg())
//        adaptor.add(LatestMsg())
//        adaptor.add(LatestMsg())
//        adaptor.add(LatestMsg())

    }

    private  fun loadLatestMsg(){
        val fromid = currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromid")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d("Logs","${chatMessage?.text}")
                adaptor.add(LatestMsg(chatMessage!!))

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

    class LatestMsg(val chatMessage: ChatMessage): Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.latest_user_msg.text = chatMessage.text
        }

        override fun getLayout(): Int {
            return R.layout.latest_msg_tile
        }
    }

}