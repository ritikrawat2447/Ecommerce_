package com.example.android.ecommerce.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product : ProductModel )

    @Delete
    suspend fun deleteProduct(product : ProductModel )

    @Query("Select * FROM products")
    fun getAllProduct() : LiveData<List<ProductModel>>

    @Query("Select * FROM products where productId = :id")
    fun isExit(id: String) : ProductModel
}