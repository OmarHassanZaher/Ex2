package com.example.ex2.domain.usecase

import android.content.Context
import android.util.Log
import com.example.ex2.data.model.UserModel
import com.example.ex2.domain.entites.File0
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class File0UseCase @Inject constructor(@ApplicationContext private val context: Context){
    private var userList: ArrayList<UserModel>? = null

    private fun filterInput0() {
        var transformedItems: ArrayList<File0> = ArrayList()
        userList!!.forEach {
            if (transformedItems.find { e -> e.nameOfProduct == it.nameOfProduct } == null) {
                transformedItems.add(File0(it.nameOfProduct, it.quantity / userList!!.size))
            } else {
                var item = transformedItems.find { e -> e.nameOfProduct == it.nameOfProduct }
                var item2 =
                    File0(item!!.nameOfProduct, item.quantity + (it.quantity / userList!!.size))
                transformedItems.remove(item)
                transformedItems.add(item2)


            }
        }
        Log.d("test", transformedItems.toString())
    }
    fun execute(){
        filterInput0()
    }
}