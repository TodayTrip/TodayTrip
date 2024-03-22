package com.twoday.todaytrip.ui.random

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomResultOneBinding
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.SelectRegionPrefUtil
import com.twoday.todaytrip.viewModel.RandomResultViewModel

class RandomResultOneFragment : Fragment() {

    private val TAG = "RandomResultOneFragment"

    private var _binding: FragmentRandomResultOneBinding? = null
    private val binding get() = _binding!!

    private val model: RandomResultViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRandomResultOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        initView()
        initModelObserver()
    }

    private fun initView() {
        Glide.with(this)
            .load(R.drawable.gif_loading)
            .into(binding.ivResultOneImage)
    }

    @SuppressLint("SetTextI18n")
    private fun initModelObserver() {
        model.isTouristAttractionListReady.observe(viewLifecycleOwner, Observer { isReady ->
            if (isReady) {
                binding.tvResultOneTitle.text =
                    DestinationPrefUtil.loadDestination().toString() + "(으)로 떠나볼까요?"
                val resultImg = when (DestinationPrefUtil.loadDestination().toString()) {
                    "서울" -> {
                        R.drawable.img_map_seoul
                    }

                    "인천" -> {
                        R.drawable.img_map_incheon
                    }

                    "전북" -> {
                        R.drawable.img_map_jeonbuk
                    }

                    "전남" -> {
                        R.drawable.img_map_jeonnam
                    }

                    "경북" -> {
                        R.drawable.img_map_gyeongbuk
                    }

                    "경남" -> {
                        R.drawable.img_map_gyeongnam
                    }

                    "충북" -> {
                        R.drawable.img_map_chungbuk
                    }

                    "충남" -> {
                        R.drawable.img_map_chungnam
                    }

                    "강원" -> {
                        R.drawable.img_map_gangwon
                    }

                    "대구" -> {
                        R.drawable.img_map_daegu
                    }

                    "부산" -> {
                        R.drawable.img_map_busan
                    }

                    "대전" -> {
                        R.drawable.img_map_daejeon
                    }

                    "제주" -> {
                        R.drawable.img_map_jeju
                    }

                    "경기" -> {
                        R.drawable.img_map_gyeonggi
                    }

                    "광주" -> {
                        R.drawable.img_map_gwangju
                    }

                    "울산" -> {
                        R.drawable.img_map_ulsan
                    }

                    else -> 0
                }

                binding.ivResultOneImage.setImageResource(resultImg)

                Log.d(TAG, "tourist attraction list ready! start main activity")
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(
                        activity,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }, 3000)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}