package com.example.android.ecommerce.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.android.ecommerce.Activity.AddressActivity
import com.example.android.ecommerce.Activity.CategoryActivity
import com.example.android.ecommerce.Adapter.CartAdapter
import com.example.android.ecommerce.R
import com.example.android.ecommerce.databinding.FragmentCartBinding
import com.example.android.ecommerce.roomdb.AppDatabase
import com.example.android.ecommerce.roomdb.ProductModel

class CartFragment : Fragment() {

    private lateinit var binding : FragmentCartBinding
    private lateinit var list : ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).prodctDao()

        list = ArrayList()

        dao.getAllProduct().observe(requireActivity()){
            binding.cardRecycler.adapter = CartAdapter(requireContext(),it)
            list.clear()
            for ( data in it ){
                list.add(data.productId)
            }
            totatCost(it)
        }

        return binding.root
    }

    private fun totatCost(data: List<ProductModel>?) {
        var total = 0
        for ( item in data!! ){
            total += item.productSp!!.toInt()

        }

        binding.textView9.text = "Total item in Cart is ${data.size}"
        binding.textView10.text = "Total Cost ${total}"

        binding.checkout.setOnClickListener{
            val intent = Intent(context, AddressActivity::class.java)
            val bundle = Bundle()

            bundle.putStringArrayList("productId",list)
            bundle.putString("totalCost",total.toString())
            intent.putExtras(bundle)

            startActivity(intent)
        }
    }
}