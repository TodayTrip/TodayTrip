package com.twoday.todaytrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityMainBinding
import com.twoday.todaytrip.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val model by lazy{
        ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callTourApi()
    }

    private fun callTourApi(){
        //지역 기반 관광지 정보 조회 api 테스트 코드
        //model.callAreaBased()
        //공통 정보 조회 api 테스트 코드
        //model.callCommonDetail()
        //소개 정보 조회 api 테스트 코드
        model.callIntroDetail()
    }
}