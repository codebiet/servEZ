package com.example.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.example.services.models.Worker
import com.example.services.shared.currentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_provider_register.*

class ProviderRegisterActivity : AppCompatActivity() {

    lateinit var workerUID:String
    lateinit var serviceType:String
    lateinit var experience:String
    lateinit var description:Editable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_register)

        workerUID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val workSpinner:Spinner = findViewById(R.id.spinner_work_selection)
        val adaptor: ArrayAdapter<String> = ArrayAdapter(this,R.layout.spinner_item,resources.getStringArray(R.array.works))
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        workSpinner.adapter = adaptor

        workSpinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                serviceType = parent?.selectedItem.toString()
                changePhoto(parent?.selectedItem.toString())
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val experienceSpinner: Spinner = findViewById(R.id.spinner_experience_selection)
        val adapterExp:ArrayAdapter<String> = ArrayAdapter(this,R.layout.spinner_item,resources.getStringArray(R.array.experience))
        adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        experienceSpinner.adapter = adapterExp

        experienceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                experience = parent?.selectedItem.toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        button_register.setOnClickListener{
            description = findViewById<EditText>(R.id.edittext_description).text
            uploadWorker()
        }
    }

    private fun uploadWorker(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid==null|| currentUser==null)return

        Log.d("Logs","Provider message ${currentUser!!.providesService}")

        if(currentUser!!.providesService!="false"){
            Toast.makeText(this,"you are already Registered",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("/workers/$serviceType/$uid")
        val worker = Worker(uid.toString(),serviceType,experience,description.toString(),"false",0,0,50)
        ref.setValue(worker)
                .addOnSuccessListener {
                    Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
                    val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
                    userRef.child("providesService").setValue(serviceType)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener{
                Toast.makeText(this,"Failed to Register: ${it.message}",Toast.LENGTH_SHORT).show()
                Log.d("Logs","Failed: ${it.message}")
             }
    }

    private fun changePhoto(name:String){
        val img = findViewById<ImageView>(R.id.worker_icon)
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
}