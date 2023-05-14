package com.example.bachesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bachesapp.databinding.ActivityMapaBinding

class MapaActivity : AppCompatActivity() {
    lateinit var binding:ActivityMapaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}