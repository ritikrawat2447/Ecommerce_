package com.example.android.ecommerce.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.android.ecommerce.Model.UserModel
import com.example.android.ecommerce.R
import com.example.android.ecommerce.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button4.setOnClickListener{
            openLogin()
        }
        binding.button3.setOnClickListener{
            validateUser()
        }

    }
    private fun validateUser(){
        if ( binding.name1.text!!.isEmpty() || binding.phoneNumber1.text!!.isEmpty() ){
            Toast.makeText(this,"PLease fill all fields",Toast.LENGTH_SHORT).show()
        }else{
            storeData()
        }
    }
    private fun storeData(){
        val builder = AlertDialog.Builder(this)
            .setTitle("Loaing.....")
            .setMessage("PLease Wait")
            .setCancelable(false)
            .create()
        builder.show()

        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preference.edit()

        editor.putString("number",binding.phoneNumber1.text.toString())
        editor.putString("name",binding.name1.text.toString())
        editor.apply()


        val data = UserModel(userName = binding.name1.text.toString() , userPhoneNumber = binding.phoneNumber1.text.toString() )

        Firebase.firestore.collection("Users").document(binding.phoneNumber1.text.toString())
            .set(data).addOnSuccessListener {
                Toast.makeText(this, "User Register", Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLogin()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }

    private fun openLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}