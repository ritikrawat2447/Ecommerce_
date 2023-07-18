package com.example.android.ecommerce.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ecommerce.Adapter.CategoryProductAdapter
import com.example.android.ecommerce.Adapter.ProductAdapter
import com.example.android.ecommerce.Model.AddProductModel
import com.example.android.ecommerce.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        getCategory(intent.getStringExtra("cat"))

    }

    private fun getCategory(category : String?) {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory",category)
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                val RecyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
               RecyclerView.adapter = CategoryProductAdapter(this,list )
            }.addOnFailureListener{

            }
    }
}