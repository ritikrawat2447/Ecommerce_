package com.example.android.ecommerce.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.ecommerce.Activity.ProductDetailsActivity
import com.example.android.ecommerce.Model.AddProductModel
import com.example.android.ecommerce.Model.CategoryModel
import com.example.android.ecommerce.R
import com.example.android.ecommerce.databinding.ItemCategoryProductLayoutBinding
import com.example.android.ecommerce.databinding.LayoutProductItemBinding

class CategoryProductAdapter(var context : Context, var list : ArrayList<AddProductModel> ) : RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>(){

    inner class CategoryProductViewHolder(val binding : ItemCategoryProductLayoutBinding )
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding = ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return CategoryProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        holder.binding.textView2.text =  list[position].productSp
        holder.binding.textView1.text =  list[position].productName
        Glide.with(context).load(list[position].productCoverImage).into(holder.binding.imageView2)

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}