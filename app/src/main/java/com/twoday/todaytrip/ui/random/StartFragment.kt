package com.twoday.todaytrip.ui.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentStartBinding
import com.twoday.todaytrip.viewModel.RandomViewModel

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this@StartFragment)[RandomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        initView()
        setUpClickListener()
    }

    private fun initView() {
        Glide.with(this)
            .load(R.drawable.gif_start_walk)
            .into(binding.ivStartWalkGif)
    }

    private fun setUpClickListener() {
        binding.btnStartTrip.setOnClickListener {
            viewModel.resetSharedPref()
            findNavController().navigate(R.id.action_navigation_start_to_navigation_random_option)
        }
//        binding.btnSelectRegion.setOnClickListener {
//            viewModel.resetSharedPref()
//            findNavController().navigate(R.id.action_navigation_start_to_navigation_select_region)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}