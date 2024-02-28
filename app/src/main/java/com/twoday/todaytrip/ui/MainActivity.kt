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
        callTourApi()
    }

    private fun setNavigation() {
        val navHomeFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        val navController = navHomeFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun callTourApi() {
        //지역 기반 관광지 정보 조회 api 테스트 코드
        //model.callAreaBased()
        //공통 정보 조회 api 테스트 코드
        //model.callCommonDetail()
        //소개 정보 조회 api 테스트 코드
        model.callIntroDetail()
    }
}