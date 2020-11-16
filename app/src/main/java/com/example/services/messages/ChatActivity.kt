package com.example.services.messages

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.services.R
import com.example.services.models.ChatMessage
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recyclerview_chatlog.adapter = adapter
        loadMessages()
        send_btn.setOnClickListener{
            Log.d("Chat", "Attempt to send")
            performSendMessage()
            hideKeyboard(this)
        }

    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    private fun loadMessages(){
        val fromid = intent.getStringExtra("MY_ID")
        val toId = intent.getStringExtra("RCV_ID")

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromid/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d("Chat", "${chatMessage?.text}")
                    if (chatMessage.fromId == fromid) {
                        adapter.add(ChatToItem(chatMessage.text))
                    } else if (chatMessage.fromId == toId) {
                        adapter.add(ChatFromItem(chatMessage.text))
                    }
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

    private fun performSendMessage(){

        val fromid = intent.getStringExtra("MY_ID")
        val toId = intent.getStringExtra("RCV_ID")

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromid/$toId").push()
        val toref = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromid").push()
        val chatLog:String = edittext_msg.text.toString()
        if(fromid == null || toId == null)return
        val chatMessage = ChatMessage(ref.key.toString(), chatLog, fromid, toId, System.currentTimeMillis() / 1000)

        ref.setValue(chatMessage)
                .addOnSuccessListener {
                    edittext_msg.text.clear()
                    val recycleView:androidx.recyclerview.widget.RecyclerView = findViewById(R.id.recyclerview_chatlog)
                    recycleView.scrollToPosition(adapter.itemCount - 1)
                }
        toref.setValue(chatMessage)

        val latestFrom = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromid/$toId")
        val latestTo = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromid")

        latestFrom.setValue(chatMessage)
        latestTo.setValue(chatMessage)

    }


    class ChatFromItem(val text: String): Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.textview_from_row).text = text
        }
        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }
    }

    class ChatToItem(val text: String): Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            Log.d("Msg", "Called")
            viewHolder.itemView.findViewById<TextView>(R.id.textview_to_row).text = text

        }
        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }
    }

}