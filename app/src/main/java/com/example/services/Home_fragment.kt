package com.example.services
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.services.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*

class Home_fragment() : Fragment() {
    companion object {
        fun newInstance() : Home_fragment = Home_fragment()
        var currentUser: User?=null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        return view ;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyIfLoggedIn()
        //getCurrentUser()

        var greet = "Hello! "
        welcome_text.text = greet

        sign_out.setOnClickListener{
          signOut()
        }

        button_worker.setOnClickListener{
            val intent = Intent(activity,ProviderRegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            startActivity(intent)
        }
    }

    private  fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intent =  Intent(activity, MainActivity::class.java )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun verifyIfLoggedIn(){
        var uid = FirebaseAuth.getInstance().uid
        if (uid==null){
            val s: Context? = getContext()
            val intent = Intent(s, MainActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun getCurrentUser(){
        var uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentUser = dataSnapshot.getValue(User::class.java)

                var greet = "Hello! "
                if(currentUser?.gender=="male")
                    greet+="Mr. "
                else greet+="Ms. "
                greet+= currentUser?.firstName
                welcome_text.text = greet

            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        ref.addListenerForSingleValueEvent(postListener)
    }

}