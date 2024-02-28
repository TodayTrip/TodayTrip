package com.twoday.todaytrip.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentStartBinding

//TODO 여행지 초기화 작업 필요할 듯
class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        setUpClickListener()
        return binding.root
    }

    private fun setUpClickListener() {
        binding.btnStartTrip.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_start_to_navigation_random_option)
        }
        Glide.with(this)
            .load(R.drawable.start_walk_gif)
            .into(binding.ivStartWalkGif)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}