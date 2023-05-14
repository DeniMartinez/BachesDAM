package com.example.bachesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bachesapp.databinding.ActivityExportarBinding

class ExportarActivity : AppCompatActivity() {
    lateinit var  binding:ActivityExportarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}