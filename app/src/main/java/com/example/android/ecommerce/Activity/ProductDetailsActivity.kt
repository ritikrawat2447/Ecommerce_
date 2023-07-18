package com.example.android.ecommerce.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent.DispatcherState
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.android.ecommerce.MainActivity
import com.example.android.ecommerce.R
import com.example.android.ecommerce.databinding.ActivityProductDetailsBinding
import com.example.android.ecommerce.databinding.FragmentHomeBinding
import com.example.android.ecommerce.roomdb.AppDatabase
import com.example.android.ecommerce.roomdb.ProductDao
import com.example.android.ecommerce.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))

        setContentView(binding.root)

    }

    private fun getProductDetails(proId: String?) {
        Firebase.firestore.collection("products").document(proId!!)
            .get().addOnSuccessListener {
                val list = it.get("productImage")!! as ArrayList<String>
                val name = it.getString("productName")
                val productSp = it.getString("productSp")
                val productDesc = it.getString("productDescription")
                binding.textView4.text = it.getString("productName")
                binding.textView5.text = productSp
                binding.textView6.text = productDesc

                val slideList = ArrayList<SlideModel>()
                for ( data in list ){
                    slideList.add(SlideModel(data,ScaleTypes.CENTER_CROP))
                }

                cartAction(proId,name,productSp,it.getString("productCoverImage"))

                binding.imageSlider.setImageList(slideList)

            }
            .addOnFailureListener{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(proId: String, name: String?, productSp: String?, coverImg: String?) {

        val productDao = AppDatabase.getInstance(this).prodctDao()
        if ( productDao.isExit(proId) != null ) {
            binding.textView7.text = "GO TO CART"
        }else{
            binding.textView7.text = "ADD TO CART"
        }

        binding.textView7.setOnClickListener{
            if ( productDao.isExit(proId) != null ) {
                openCart()
            }else{
                addToCart(productDao,proId,name,productSp,coverImg)
            }
        }

    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun addToCart(productDao: ProductDao, proId: String, name: String?, productSp: String?, coverImg: String?) {
        val data = ProductModel(proId,name,coverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO){
                productDao.insertProduct(data)
                binding.textView7.text = "GO TO CART"
        }
    }
}