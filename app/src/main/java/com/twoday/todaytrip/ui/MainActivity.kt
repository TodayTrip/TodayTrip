package com.twoday.todaytrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.twoday.todaytrip.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import com.twoday.todaytrip.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val model by lazy {
        ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setNavigation()
    }

    private fun setNavigation() {
        val navHomeFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        val navController = navHomeFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}