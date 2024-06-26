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
import androidx.transition.TransitionInflater
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomResultOneBinding
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.pref_utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.glide
import com.twoday.todaytrip.viewModel.RandomResultViewModel

class RandomResultOneFragment : Fragment() {

    private val TAG = "RandomResultOneFragment"

    private var _binding: FragmentRandomResultOneBinding? = null
    private val binding get() = _binding!!

    private val model: RandomResultViewModel by activityViewModels()


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
        binding.ivResultOneImage.glide(R.drawable.gif_loading_hour_glass)
    }

    @SuppressLint("SetTextI18n")
    private fun initModelObserver() {
        model.isTouristAttractionListReady.observe(viewLifecycleOwner, Observer { isReady ->
            if (isReady) {
                val someRegions = listOf("서울", "대구", "제주", "경기", "광주")
                if (DestinationPrefUtil.loadDestination() in someRegions) {
                    binding.tvResultOneTitle.text =
                        "\n" + DestinationPrefUtil.loadDestination().toString() + "로 \n떠나볼까요?"
                } else {
                    binding.tvResultOneTitle.text =
                        "\n" + DestinationPrefUtil.loadDestination().toString() + "으로 \n떠나볼까요?"
                }
                binding.tvResultOneTitle.textSize = 36F

                val resultImg = model.regionToMap[DestinationPrefUtil.loadDestination().toString()]
                if (resultImg != null) {
                    binding.ivResultOneImage.setImageResource(resultImg)
                }

                Log.d(TAG, "tourist attraction list ready! start main activity")
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(
                        activity,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }, 1000)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}