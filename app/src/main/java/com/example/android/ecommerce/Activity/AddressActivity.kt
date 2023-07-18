package com.example.android.ecommerce.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.android.ecommerce.R
import com.example.android.ecommerce.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddressBinding
    private lateinit var preference : SharedPreferences
    private lateinit var totalCost : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = this.getSharedPreferences("user", MODE_PRIVATE)
        totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()

        binding.proceed.setOnClickListener{
            validateData(
                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.userPinCode.text.toString(),
                binding.userCity.text.toString(),
                binding.userState.text.toString(),
                binding.userVillage.text.toString(),
            )
        }
    }

    private fun validateData(number: String, name: String, pincode: String, city : String, state: String, village: String) {
        if ( number.isEmpty() || state.isEmpty() || name.isEmpty()  ){
            Toast.makeText(this,"Please fill all fields ",Toast.LENGTH_SHORT).show()
        }else{
            storeData(pincode,city,village,state)
        }
    }

    private fun storeData(pincode: String, city: String, village: String, state: String) {
        val map = hashMapOf<String,Any>()
        map["village"] = village
        map["state"] = state
        map["pincode"] = pincode
        map["city"] = city

        Firebase.firestore.collection("Users")
            .document(preference.getString("number","")!!)
            .update(map).addOnSuccessListener {

                val bundle = Bundle()

                bundle.putStringArrayList("productId",intent.getStringArrayListExtra("productId"))
                bundle.putString("totalCost", totalCost )

                val intent = Intent(this, CheckOutActivity::class.java)

                intent.putExtras(bundle)

                startActivity(intent)

            }
            .addOnFailureListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {
        Firebase.firestore.collection("Users")
            .document(preference.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.userVillage.setText(it.getString("village"))
                binding.userCity.setText(it.getString("city"))
                binding.userPinCode.setText(it.getString("pincode"))
                binding.userState.setText(it.getString("state"))
            }
            .addOnFailureListener{

                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }
}