package com.example.bachesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bachesapp.databinding.ActivityNubeBinding

class NubeActivity : AppCompatActivity() {
    lateinit var  binding:ActivityNubeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}