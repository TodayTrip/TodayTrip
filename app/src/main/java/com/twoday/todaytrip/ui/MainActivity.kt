package com.twoday.todaytrip.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.twoday.todaytrip.databinding.ActivityMainBinding
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import com.twoday.todaytrip.ui.route.RouteFragment
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
        setContentView(R.layout.activity_main)

        callTourApi()
        routeFragment()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        val navHomeFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        val navController = navHomeFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun routeFragment() {
        binding.btnRouteFragment.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_main_fragment, RouteFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun callTourApi() {
        //지역 기반 관광지 정보 조회 api 테스트 코드
        //model.callAreaBased()
        //공통 정보 조회 api 테스트 코드
        //model.callCommonDetail()
        //소개 정보 조회 api 테스트 코드
        model.callIntroDetail()
        setContentView(binding.root)
    }
}