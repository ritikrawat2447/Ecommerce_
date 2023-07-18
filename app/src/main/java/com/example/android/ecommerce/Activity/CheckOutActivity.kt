package com.example.android.ecommerce.Activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.android.ecommerce.MainActivity
import com.example.android.ecommerce.R
import com.example.android.ecommerce.roomdb.AppDatabase
import com.example.android.ecommerce.roomdb.ProductModel
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class CheckOutActivity : AppCompatActivity() , PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_IbU3mffdS8ePsm");

        val price = intent.getStringExtra("totalCost")


        try {
            val options = JSONObject()
            options.put("name", "Ecommerece")
            options.put("description", "Ecommerece App")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("order_id", "order_DBJOWzybf0sJbb") //from response of step 3.
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", (price!!.toInt()*100) ) //pass amount in currency subunits
            options.put("prefill.email", "ritikrawat2448@example.com")
            options.put("prefill.contact", "9988776655")
            checkout.open(this, options)

        } catch (e: Exception) {
            Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()
        uploaddData()
    }

    private fun uploaddData() {
        val id = intent.getStringArrayListExtra("productId")
        for ( currentId in id!! ){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId : String?) {
        val dao = AppDatabase.getInstance(this).prodctDao()
        Firebase.firestore.collection("products")
            .document(productId!!).get()
            .addOnSuccessListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(ProductModel(productId))
                }
                saveData(it.getString("productName"),
                it.getString("productSp"),productId)
            }
            .addOnFailureListener{

            }
    }

    private fun saveData(name: String?, price: String?, productId: String) {
        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String,Any>()
        data["name"] = name!!
        data["price"] = price!!
        data["productId"] = productId!!
        data["status"] = "Ordered"!!
        data["userID"] = preference.getString("number","")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["OrderId"] = key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this,"Order Place",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }.addOnFailureListener{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Error",Toast.LENGTH_SHORT).show()
    }
}