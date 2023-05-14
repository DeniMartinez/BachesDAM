package com.example.bachesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bachesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var  binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.ButtonExportar.setOnClickListener{
        goToExpo()
    }

    binding.ButtonNube.setOnClickListener{
        gotoNube()
    }
    }


    fun goToExpo(){
        val intent = Intent(this,ExportarActivity::class.java)
        startActivity(intent)
    }

    fun gotoNube(){
        val intent = Intent(this, NubeActivity::class.java)
        startActivity(intent)
    }
}