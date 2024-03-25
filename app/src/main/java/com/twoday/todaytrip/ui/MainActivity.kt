package com.twoday.todaytrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.twoday.todaytrip.databinding.ActivityMainBinding
import androidx.navigation.NavController
import com.twoday.todaytrip.R
import com.twoday.todaytrip.ui.place_list.RandomBottomSheetDialog
import com.twoday.todaytrip.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val model by viewModels<MainViewModel>()

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
            val randomBottomSheet = RandomBottomSheetDialog()
            randomBottomSheet.show(supportFragmentManager, randomBottomSheet.tag)
        }
    }

    private fun setNavigation() {
        val navHomeFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        val navController = navHomeFragment.navController
        setupNavControllerListener(navController)

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    // 홈 화면이 아닌 다른 화면에서의 가운데 FloatingActionButton 가시성 조절
    private fun setupNavControllerListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_place_list -> {
                    binding.fabBottomRandom.visibility = View.VISIBLE
                    model.initTourItemList()
                }
                R.id.navigation_map -> binding.fabBottomRandom.visibility = View.GONE
                R.id.navigation_route -> binding.fabBottomRandom.visibility = View.GONE
                R.id.navigation_record -> binding.fabBottomRandom.visibility = View.GONE
            }
        }
    }

    fun hideButtonNavi(state: Boolean){
        if (state) binding.bottomAppBar.visibility = View.GONE
        else binding.bottomAppBar.visibility = View.VISIBLE

    }

    fun moveToRouteFragment() {
        binding.bottomNavigationView.selectedItemId = R.id.navigation_route
    }
}