package com.twoday.todaytrip.ui.random

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.databinding.ActivityRandomBinding
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.viewModel.RandomViewModel

class RandomActivity : AppCompatActivity() {
    private val binding: ActivityRandomBinding by lazy {
        ActivityRandomBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this@RandomActivity)[RandomViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savedDestination = viewModel.loadDestinationSharedPref()
        if (savedDestination.isNotBlank()) {
            navigateToMainActivity()
        }
        setContentView(binding.root)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        finish()
    }
}