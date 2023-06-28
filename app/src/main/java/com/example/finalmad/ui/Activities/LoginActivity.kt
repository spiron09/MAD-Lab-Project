package com.example.finalmad.ui.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalmad.Models.Users
import com.example.finalmad.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.siglog.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginsublog.setOnClickListener{
            if(binding.emailinputlog.text.isEmpty() || binding.pwdinputlog.text.isEmpty()){
                Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val uid = auth.currentUser?.uid

            val emailent = binding.emailinputlog.text.toString()
            val passwordent = binding.pwdinputlog.text.toString()
            var favList: ArrayList<String>? = ArrayList()

            auth.signInWithEmailAndPassword(emailent, passwordent)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        Toast.makeText(
                            baseContext,
                            "Success",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, MainPage::class.java)
                        startActivity(intent)
                        finish()


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