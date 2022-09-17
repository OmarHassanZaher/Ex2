package com.example.ex2.presentation

import androidx.lifecycle.ViewModel
import com.example.ex2.domain.usecase.File0UseCase
import com.example.ex2.domain.usecase.File1UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
private val file0UseCase: File0UseCase,
private val file1UseCase: File1UseCase
): ViewModel(){

fun getFile0(){
file0UseCase.execute()
}
    fun getFile1(){
        file1UseCase.execute()
    }
}