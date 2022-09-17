package com.example.ex2.data.model

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
data class UserModel(
    var area: String,
    var nameOfProduct: String,
    var quantity: Double,
    var brand: String,
)