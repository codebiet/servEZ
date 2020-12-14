package com.example.services.account

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.services.signInRegister.SignInActivity
import com.example.services.ProviderRegisterActivity
import com.example.services.R
import com.example.services.shared.GetCurrentUser
import com.example.services.shared.currentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*

class Account_fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_account,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(currentUser != null) {
            name_account.text = currentUser?.firstName + " " + currentUser?.lastName
            name_account.setTextColor(Color.parseColor("#233B5D"))
        }

        if(currentUser?.profileImgURL!=null&&currentUser?.profileImgURL!="NULL"){
            Picasso.get().load(currentUser?.profileImgURL).into(select_photo_image_view_register)
            upload_img_btn.alpha=0.2f
        }

        upload_img_btn.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        edit_profile_account.setOnClickListener {
            val intent =  Intent(activity, Edit_profile::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            startActivity(intent)
        }

        log_out_account.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent =  Intent(activity, SignInActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        provide_services_account.setOnClickListener {
            val intent = Intent(activity, ProviderRegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            startActivity(intent)
        }

        contact_us_account.setOnClickListener {
            val intent = Intent(activity, ContactUsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            startActivity(intent)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            selectedPhotoUri = data.data
            Log.d("Logs","Image selected $selectedPhotoUri")
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,selectedPhotoUri)
            select_photo_image_view_register.setImageBitmap(bitmap)
            upload_img_btn.alpha = 0f
        }
        uploadImageToFirebase()
    }

    private fun uploadImageToFirebase(){
        if(selectedPhotoUri==null|| currentUser==null)return
        var filename = currentUser?.uid
        var ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Logs","successful upload  ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Logs","File Location $it")
                        FirebaseDatabase.getInstance().getReference("users/${currentUser?.uid}").child("profileImgURL").setValue(it.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(context,"Successfully Uploaded Profile Picture", Toast.LENGTH_SHORT).show()
                                    GetCurrentUser(context!!)
                                }
                    }
                }
                .addOnFailureListener{
                    Log.d("Logs","${it.message}")
                }
    }

    companion object {
        fun newInstance() : Account_fragment = Account_fragment()
    }
}