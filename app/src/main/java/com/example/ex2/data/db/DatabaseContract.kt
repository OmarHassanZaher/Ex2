package com.example.ex2.data.db

import android.provider.BaseColumns


class DatabaseContract {
    object ExcelTable : BaseColumns {
        const val TABLE_NAME = "excel_table"
        const val ID = "id"
        const val Area  = "area"
        const val NameOfProduct = "name_ofProduct"
        const val Quantity = "quantity"
        const val Brand = "brand"
    }
}