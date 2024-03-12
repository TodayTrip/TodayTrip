package com.twoday.todaytrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.twoday.todaytrip.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.twoday.todaytrip.R
import com.twoday.todaytrip.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initOnClickListener()
    }

    private fun initView() {
        setNavigation()
    }

    private fun initOnClickListener() {
        binding.fabBottomRandom.setOnClickListener {

        }
    }
    private fun setNavigation() {
        val navHomeFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        val navController = navHomeFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        setupFabVisibility(navController)

    }

    // 홈 화면이 아닌 다른 화면에서의 가운데 FloatingActionButton 가시성 조절
    private fun setupFabVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_map -> binding.fabBottomRandom.visibility = View.GONE
                R.id.navigation_route -> binding.fabBottomRandom.visibility = View.GONE
                R.id.navigation_record -> binding.fabBottomRandom.visibility = View.GONE
                else -> binding.fabBottomRandom.visibility = View.VISIBLE
            }
        }
    }
}