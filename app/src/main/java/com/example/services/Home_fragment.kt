package com.example.services
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.services.models.User
import com.example.services.shared.GetCurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*

class Home_fragment() : Fragment() {
    companion object {
        fun newInstance() : Home_fragment = Home_fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        return view ;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyIfLoggedIn()
        GetCurrentUser()

        sign_out.setOnClickListener{
          signOut()
        }

        work_mechanic.setOnClickListener{
            val intent = Intent(activity,WorkSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            intent.putExtra("workerType","Mechanic")
            startActivity(intent)
        }
        work_carpentar.setOnClickListener{
            val intent = Intent(activity,WorkSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            intent.putExtra("workerType","Carpenter")
            startActivity(intent)
        }
        work_bricklayer.setOnClickListener{
            val intent = Intent(activity,WorkSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            intent.putExtra("workerType","Plumber")
            startActivity(intent)
        }
        work_plumber.setOnClickListener{
            val intent = Intent(activity,WorkSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            intent.putExtra("workerType","Bricklayer")
            startActivity(intent)
        }
        work_painter.setOnClickListener{
            val intent = Intent(activity,WorkSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            intent.putExtra("workerType","Painter")
            startActivity(intent)
        }
        work_electrician.setOnClickListener{
            val intent = Intent(activity,WorkSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            intent.putExtra("workerType","Electrician")
            startActivity(intent)
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


}