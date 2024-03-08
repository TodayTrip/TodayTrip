package com.twoday.todaytrip.ui.random

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twoday.todaytrip.databinding.ActivityRandomBinding

class RandomActivity : AppCompatActivity() {
    private val binding: ActivityRandomBinding by lazy {
        ActivityRandomBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}