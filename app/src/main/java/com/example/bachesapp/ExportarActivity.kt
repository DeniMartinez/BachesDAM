package com.example.bachesapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bachesapp.clases.Punto
import com.example.bachesapp.databinding.ActivityExportarBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ExportarActivity : AppCompatActivity() {
    internal  var dbHelper = DB_Helper(this)
    lateinit var  binding:ActivityExportarBinding
    private val puntosList: ArrayList<LatLng> = ArrayList()

    private val REQUEST_PERMISSIONS = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCsv.setOnClickListener{
            if (checkPermissions()){

            }else{
                exportToCSV()
            }
        }

        binding.btnCompartir.setOnClickListener{
            if (checkPermissions()){

            }else{
                shareCSVText()
            }
        }

        binding.btnJson.setOnClickListener{

            if (checkPermissions()){

            }else{
                val res =  dbHelper.allData
                if (res.count != 0){
                    while (res.moveToNext()){
                        val latLng = LatLng(res.getDouble(0),res.getDouble(1))
                        val markerOptions = MarkerOptions().position(latLng)
                        puntosList.add(latLng)
                    }
                }
                val listaPutnos = mutableListOf<Punto>()
                for(punto in puntosList){
                    val puntoObj = Punto(punto.latitude,punto.longitude)
                    listaPutnos.add(puntoObj)
                }

                val gson = Gson()
                val json = gson.toJson(listaPutnos)

                val file = File(applicationContext.getExternalFilesDir(null),"puntos.json")
                file.writeText(json)

                Toast.makeText(this,"Archivo exportado correctamente revisa tus archivos", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun checkPermissions(): Boolean {
        val permissionWriteStorage = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionManageStorage = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )
        return permissionWriteStorage == PackageManager.PERMISSION_GRANTED &&
                permissionManageStorage == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ),
            REQUEST_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, puedes continuar con la operación

            } else {
                // Permiso denegado, muestra un mensaje o toma alguna acción adecuada
            }
        }
    }

    private fun exportToCSV() {
        val data = dbHelper.allData

        val csvFile = File(getExternalFilesDir(null), "datos.csv")
        val writer = CSVWriter(FileWriter(csvFile))

        try {
            // Escribe el encabezado del archivo CSV
            val header = arrayOf("Altitud", "Longitud") // Reemplaza con los nombres de tus columnas
            writer.writeNext(header)

            // Lee los datos del cursor y escribe cada fila en el archivo CSV
            if (data.moveToFirst()) {
                do {
                    val column1Value = data.getDouble(0)
                    val column2Value = data.getDouble(1)

                    val row = arrayOf(column1Value.toString(), column2Value.toString())
                    writer.writeNext(row)
                } while (data.moveToNext())
            }

            writer.close()
            Toast.makeText(this, "Archivo CSV exportado correctamente", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al exportar el archivo CSV", Toast.LENGTH_SHORT).show()
        } finally {
            data.close() // Cierra el cursor después de utilizarlo
        }
    }

    private fun shareCSVText() {
        val csvFile = File(getExternalFilesDir(null), "datos.csv")
        val csvContent = csvFile.readText()

        // Crear un intent para compartir el texto
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, csvContent)

        // Verificar si hay alguna aplicación disponible para compartir texto
        val shareIntentChooser = Intent.createChooser(shareIntent, "Compartir archivo CSV como texto")

        // Verificar si hay alguna aplicación disponible para manejar el intent
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(shareIntentChooser)
        } else {
            Toast.makeText(this, "No se encontró ninguna aplicación para compartir texto", Toast.LENGTH_SHORT).show()
        }
    }


}