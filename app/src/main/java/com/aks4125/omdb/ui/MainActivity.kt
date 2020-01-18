package com.aks4125.omdb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks4125.omdb.R

class MainActivity : AppCompatActivity() {


    companion object {
        internal const val LOGGER = "MoviesApp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
