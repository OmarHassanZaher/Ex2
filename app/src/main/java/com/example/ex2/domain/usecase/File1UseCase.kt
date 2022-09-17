package com.example.ex2.domain.usecase

import android.content.Context
import android.util.Log
import com.example.ex2.data.model.UserModel
import com.example.ex2.domain.entites.File1
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class File1UseCase @Inject constructor(@ApplicationContext private val context: Context){
    private var userList: ArrayList<UserModel>? = null

    private fun filterInput1() {
        var transformedItems: ArrayList<File1> = ArrayList()
        userList!!.forEach {
            if (transformedItems.find { e -> e.nameOfProduct == it.nameOfProduct && e.brand == it.brand } == null) {
                transformedItems.add(File1(it.nameOfProduct, it.brand, 1))
            } else {
                var item =
                    transformedItems.find { e -> e.nameOfProduct == it.nameOfProduct && e.brand == it.brand }
                var item2 = File1(item!!.nameOfProduct, item.brand, item.number + 1)
                transformedItems.remove(item)
                transformedItems.add(item2)
            }
        }
        Log.d("test2", getBrand(transformedItems).toString())
    }

    private fun getBrand(list :ArrayList<File1>) :List<File1> {
        var transformedItems: ArrayList<File1> = ArrayList()

        list.forEach{
            if(transformedItems.find { e -> e.nameOfProduct == it.nameOfProduct  } == null ){
                transformedItems.add(File1(it.nameOfProduct,it.brand,it.number))
            }else{
                var item = transformedItems.find { e -> e.nameOfProduct == it.nameOfProduct }
                if ( it.number > item!!.number ){
                    transformedItems.remove(item)
                    transformedItems.add(it)
                }
            }
        }
        return transformedItems
    }
    fun execute(){
        filterInput1()
    }
}