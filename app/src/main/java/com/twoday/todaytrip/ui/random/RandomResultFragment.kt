package com.twoday.todaytrip.ui.random

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import androidx.transition.TransitionInflater
import com.twoday.todaytrip.databinding.FragmentRandomResultBinding
import com.twoday.todaytrip.ui.MainActivity
import com.twoday.todaytrip.pref_utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.glide
import com.twoday.todaytrip.viewModel.RandomResultViewModel

class RandomResultFragment : Fragment() {
    private val TAG = "RandomResultFragment"

    private var _binding: FragmentRandomResultBinding? = null
    private val binding get() = _binding!!

    private val model: RandomResultViewModel by lazy {
        ViewModelProvider(this@RandomResultFragment)[RandomResultViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initModelObserver()
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    @SuppressLint("SetTextI18n")
    private fun initModelObserver(){
        model.isTouristAttractionListReady.observe(viewLifecycleOwner, Observer { isReady ->
            if(isReady) {
                val someRegions = listOf("서울", "대구", "제주", "경기", "광주")
                if (DestinationPrefUtil.loadDestination() in someRegions) {
                    binding.textView2.text =
                        "\n" + DestinationPrefUtil.loadDestination().toString() + "로 \n떠나볼까요?"
                } else {
                    binding.textView2.text =
                        "\n" + DestinationPrefUtil.loadDestination().toString() + "으로 \n떠나볼까요?"
                }
                binding.textView2.textSize = 36F

                val resultImg = model.regionToMap[DestinationPrefUtil.loadDestination().toString()]
                if (resultImg != null) {
                    binding.ivRandomMapGif.setImageResource(resultImg)
                }

                Log.d(TAG, "tourist attraction list ready! start main activity")
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(activity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }, 1000)
            }
        })
    }

    private fun initView() {
        binding.ivRandomMapGif.glide(R.drawable.gif_map)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}