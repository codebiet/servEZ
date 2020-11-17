package com.example.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.services.messages.ChatActivity
import com.example.services.models.ChatMessage
import com.example.services.models.User
import com.example.services.shared.currentUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_call_worker.*
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

        recyclerview_latest_msg.adapter = adaptor
        recyclerview_latest_msg.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        loadLatestMsg()
    }

    val latestMsgMap = HashMap<String,ChatMessage>()

    private fun refreshRecyclerView(){
        adaptor.clear()
        latestMsgMap.values.forEach{
            adaptor.add(LatestMsg(it))
        }
        adaptor.notifyDataSetChanged()
    }

    private  fun loadLatestMsg(){
        val fromid = currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromid")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                adaptor.notifyDataSetChanged()
                latestMsgMap[snapshot.key!!]= chatMessage!!
                refreshRecyclerView()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                adaptor.notifyDataSetChanged()
                latestMsgMap[snapshot.key!!]= chatMessage!!
                refreshRecyclerView()
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
            var rcvId:String = chatMessage.toId!!
            if(currentUser!!.uid==chatMessage.toId!!)
                rcvId = chatMessage.fromId!!
            val ref = FirebaseDatabase.getInstance().getReference("users/$rcvId")
            ref.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                     val rcvUser = snapshot.getValue(User::class.java)
                    viewHolder.itemView.latest_user_name.text = rcvUser?.firstName + " " + rcvUser?.lastName
                    if(rcvUser?.profileImgURL=="NULL"||rcvUser?.profileImgURL==null){
                    }else{
                        Picasso.get().load(rcvUser?.profileImgURL).into(viewHolder.itemView.latest_msg_user_pic)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

            viewHolder.itemView.setOnClickListener{
                val intent = Intent(viewHolder.itemView.context,ChatActivity::class.java)
                intent.putExtra("MY_ID", currentUser!!.uid)
                intent.putExtra("RCV_ID", rcvId)
                intent.putExtra("MY_CLASS", currentUser)
                viewHolder.itemView.context.startActivity(intent)
            }
        }

        override fun getLayout(): Int {
            return R.layout.latest_msg_tile
        }
    }
}