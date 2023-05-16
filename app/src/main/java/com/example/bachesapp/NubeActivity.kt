package com.example.bachesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bachesapp.clases.FirebaseData
import com.example.bachesapp.databinding.ActivityNubeBinding
import com.google.firebase.database.FirebaseDatabase

class NubeActivity : AppCompatActivity() {
    lateinit var  binding:ActivityNubeBinding
    internal  var dbHelper = DB_Helper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNubeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubir.setOnClickListener{
            uploadDataToFirebase()
        }
    }

    private fun uploadDataToFirebase() {
        // Configurar la instancia de Firebase Realtime Database
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.reference.child("datos")

        // Leer los datos de tu base de datos local
        val res = dbHelper.allData
        if (res.count != 0) {
            while (res.moveToNext()) {
                val latitude = res.getDouble(0)
                val longitude = res.getDouble(1)

                // Crear un objeto de datos para subir a Firebase
                val data = FirebaseData(latitude, longitude)

                // Subir los datos a Firebase
                databaseReference.push().setValue(data)
            }
            Toast.makeText(this, "Se a subido correctamente", Toast.LENGTH_SHORT).show()
        }
    }
}