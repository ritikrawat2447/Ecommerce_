package com.example.android.ecommerce.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.ecommerce.Activity.ProductDetailsActivity
import com.example.android.ecommerce.Model.AddProductModel
import com.example.android.ecommerce.databinding.LayoutProductItemBinding

class ProductAdapter(val context : Context,val list:ArrayList<AddProductModel> )
    :RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    inner class ProductViewHolder(val binding: LayoutProductItemBinding )
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data = list[position]

        Glide.with(context).load(data.productCoverImage).into(holder.binding.imageView)
        holder.binding.textView.text = data.productName
        holder.binding.textView2.text = data.productCategory
        holder.binding.textView3.text = data.productMrp
        holder.binding.button.text = data.productSp

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }

    }
}