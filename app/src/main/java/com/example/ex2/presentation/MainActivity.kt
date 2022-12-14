package com.example.ex2.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ex2.data.db.DBHelper
import com.example.ex2.data.model.UserModel
import com.example.ex2.databinding.ActivityMainBinding
import com.example.ex2.domain.entites.File0
import com.example.ex2.domain.entites.File1
import dagger.hilt.android.AndroidEntryPoint
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var areaET: EditText? = null
    private var name_ofProductET: EditText? = null
    private var quantityET: EditText? = null
    private var brandET: EditText? = null
    private var exportTV0: TextView? = null
    private var exportTV1: TextView? = null

    private var userList: ArrayList<UserModel>? = null
    private var dbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        init()
        onClick()
    }


    private fun init() {
        userList = ArrayList()
        areaET = binding!!.etArea
        name_ofProductET = binding!!.etNameOfProduct
        quantityET = binding!!.etQuantity
        brandET = binding!!.etBrand
        exportTV0 = binding!!.tvImport0
        exportTV1 = binding!!.tvImport1
        dbHelper = DBHelper(this@MainActivity)
    }



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

    private fun onClick() {
        binding!!.btnSave.setOnClickListener {

            val area = areaET!!.text.toString()
            val name_of_product = name_ofProductET!!.text.toString()
            val quantity = quantityET!!.text.toString()
            val brand = brandET!!.text.toString()

            //check edit text data
            if (TextUtils.isEmpty(area)) {
                areaET!!.error = "required"
                areaET!!.requestFocus()
            }
            if (TextUtils.isEmpty(name_of_product)) {
                name_ofProductET!!.error = "required"
                name_ofProductET!!.requestFocus()

            }
            if (TextUtils.isEmpty(quantity)) {
                quantityET!!.error = "required"
                quantityET!!.requestFocus()
            }
            if (TextUtils.isEmpty(brand)) {
                brandET!!.error = "required"
                brandET!!.requestFocus()

            }
            val userModel = UserModel(area, name_of_product, quantity.toDouble(), brand)
            val id = dbHelper!!.insertUser(userModel)
            if (id > 0) {
                Toast.makeText(this@MainActivity, "Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Fail", Toast.LENGTH_SHORT).show()
            }

        }


        exportTV0!!.setOnClickListener {
            filterInput0()
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (applicationContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions =
                        arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, 1)
                } else {
                    importData()
                }
            } else {
                importData()
            }
        }
        exportTV1!!.setOnClickListener {
            filterInput1()
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (applicationContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions =
                        arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, 1)
                } else {
                    importData()
                }
            } else {
                importData()
            }

        }
    }

    private fun importData() {

        userList = dbHelper!!.allLocalUser
        if (userList!!.size > 0) {
            createXlFile()
        } else {
            Toast.makeText(this, "list are empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createXlFile() {


        val wb: Workbook = HSSFWorkbook()

        var cell: Cell?
        var sheet: Sheet?
        sheet = wb.createSheet("Demo Excel Sheet")
        //Now column and row
        val row: Row? = sheet.createRow(0)

        cell = row!!.createCell(0)
        cell.setCellValue("area")

        cell = row.createCell(1)
        cell.setCellValue("name")

        cell = row.createCell(2)
        cell.setCellValue("quantity")

        cell = row!!.createCell(3)
        cell.setCellValue("brand")

        //column width
        sheet.setColumnWidth(0, 20 * 200)
        sheet.setColumnWidth(1, 30 * 200)
        sheet.setColumnWidth(2, 30 * 200)
        sheet.setColumnWidth(3, 30 * 200)
        for (i in userList!!.indices) {

            val row1: Row? = sheet.createRow(i + 1)

            cell = row1!!.createCell(0)
            cell.setCellValue(userList!![i].area)

            cell = row1.createCell(1)
            cell.setCellValue(userList!![i].nameOfProduct)

            //  cell.setCellStyle(cellStyle);
            cell = row1.createCell(2)
            cell.setCellValue(userList!![i].quantity)

            cell = row1.createCell(3)
            cell.setCellValue(userList!![i].brand)

            sheet.setColumnWidth(0, 20 * 200)
            sheet.setColumnWidth(1, 30 * 200)
            sheet.setColumnWidth(2, 30 * 200)
            sheet.setColumnWidth(3, 30 * 200)
        }
        val fileName = "ImportExcel" + System.currentTimeMillis() + ".xls"
        val path = Environment.getExternalStorageDirectory()
            .toString() + File.separator + "ImportExcel" + File.separator + fileName

        val file = File(
            Environment.getExternalStorageDirectory().toString() + File.separator + "ImportExcel"
        )
        if (!file.exists()) {
            file.mkdirs()
        }

        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(path)
            wb.write(outputStream)
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(applicationContext, "Excel Created in $path", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Not OK", Toast.LENGTH_LONG).show()
            try {
                outputStream!!.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            importData()
        } else {
            Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}