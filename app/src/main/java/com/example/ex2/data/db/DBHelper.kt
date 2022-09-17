package com.example.ex2.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    lateinit var db: SQLiteDatabase
    private val context: Context
    override fun onCreate(db: SQLiteDatabase) {
        this.db = db
        val SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " +
                DatabaseContract.ExcelTable.TABLE_NAME + " ( " +
                DatabaseContract.ExcelTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.ExcelTable.Area + " TEXT, " +
                DatabaseContract.ExcelTable.NameOfProduct + " TEXT,  " +
                DatabaseContract.ExcelTable.Quantity + " TEXT  " +

                ")"
        db.execSQL(SQL_CREATE_STUDENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.ExcelTable.TABLE_NAME)
        onCreate(db)
    }

    fun insertUser(model: UserModel): Long {
        db = writableDatabase
        val cv = ContentValues()
        cv.put(DatabaseContract.ExcelTable.Area, model.area)
        cv.put(DatabaseContract.ExcelTable.NameOfProduct, model.nameOfProduct)
        cv.put(DatabaseContract.ExcelTable.Quantity, model.quantity)
//        cv.put(DatabaseContract.ExcelTable.Brand,model.brand)
        return db.insert(DatabaseContract.ExcelTable.TABLE_NAME, null, cv)
    }

    val allLocalUser: ArrayList<UserModel>

        get() {
            db = readableDatabase
            val list: ArrayList<UserModel> = ArrayList()
            val cursor: Cursor =
                db.rawQuery("SELECT * FROM " + DatabaseContract.ExcelTable.TABLE_NAME, null)
            if (cursor.moveToFirst()) {
                do {
                    val area: String =
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExcelTable.Area))
                    val name_ofProduct: String =
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExcelTable.NameOfProduct))
                    val quantity: String =
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExcelTable.Quantity))
//                    val brand: String =
//                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExcelTable.Brand))
                    list.add(UserModel(area, name_ofProduct, quantity))
                } while (cursor.moveToNext())
            }
            return list
        }

    companion object {
        private const val DATABASE_NAME = "user.db"
        private const val DATABASE_VERSION = 1
    }

    init {
        this.context = context
    }
}