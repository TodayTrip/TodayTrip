package com.twoday.todaytrip.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoday.todaytrip.databinding.FragmentRandomResultBinding
class RandomResultFragment : Fragment() {
    private var _binding: FragmentRandomResultBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomResultBinding.inflate(inflater, container, false)
        setUpClickListener()
        return binding.root
    }

    private fun setUpClickListener() {
        binding.btnTripResult.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}