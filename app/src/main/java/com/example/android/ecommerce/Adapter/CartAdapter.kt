package com.example.android.ecommerce.Adapter

import android.content.Context
import android.content.Intent
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.ecommerce.Activity.ProductDetailsActivity
import com.example.android.ecommerce.databinding.LayoutCartItemBinding
import com.example.android.ecommerce.roomdb.AppDatabase
import com.example.android.ecommerce.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context,val list : List<ProductModel>):
    RecyclerView.Adapter<CartAdapter.CartViewHodler>(){
    inner class CartViewHodler(val binding : LayoutCartItemBinding ):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHodler {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CartViewHodler(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHodler, position: Int) {
        Glide.with(context).load(list[position].productImagae).into(holder.binding.imageView3)

        holder.binding.textView8.text = list[position].productName
        holder.binding.textView9.text = list[position].productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }

        val dao = AppDatabase.getInstance(context).prodctDao()
        holder.binding.imageView4.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(
                    ProductModel(
                        list[position].productId,
                        list[position].productName,
                        list[position].productImagae,
                        list[position].productSp
                    )
                )
            }
        }
    }
}