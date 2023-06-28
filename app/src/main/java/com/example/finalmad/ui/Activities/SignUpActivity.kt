package com.example.finalmad.ui.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalmad.Models.Users
import com.example.finalmad.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        database = FirebaseDatabase.getInstance().getReference("Users")


        binding.siglog.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupsub.setOnClickListener {

            // Write a message to the database

//
//

            if(binding.emailinputsu.text.isEmpty() || binding.pwdinputsu.text.isEmpty()){
                Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.nameinputsu.text.toString()
            val email = binding.emailinputsu.text.toString()
            val password = binding.pwdinputsu.text.toString()
            val favList: ArrayList<String>? = null
            val user = Users(email, favList.toString())
            val uid = auth.currentUser?.uid



            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success")
//                        val user = auth.currentUser
//                        updateUI(user)
                        if(uid != null){
                            database.child(uid).setValue(user)
                                .addOnCompleteListener {
                                    Log.e("dataDONE", "Success")
                                }
                        }
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            baseContext,
                            "Success",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }

        }


    }
}