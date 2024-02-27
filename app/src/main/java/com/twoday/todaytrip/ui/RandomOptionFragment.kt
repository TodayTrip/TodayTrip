package com.twoday.todaytrip.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomOptionBinding

class RandomOptionFragment : Fragment() {
    private var _binding: FragmentRandomOptionBinding? = null
    private val binding get() = _binding!!
    private var isSelectedAllRandomBtn = false
    private var isSelectedThemeRandomBtn = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAllRandom.setBackgroundResource(R.drawable.shape_yellow_boarder)
        setUpClickListener()
    }

    private fun setUpClickListener() {
        binding.btnOptionSelect.setOnClickListener{
            Log.d("btn", "올랜덤 $isSelectedAllRandomBtn, 테마랜덤 $isSelectedThemeRandomBtn")
            findNavController().navigate(R.id.action_navigation_random_option_to_navigation_random_theme)
        }
        binding.btnAllRandom.setOnClickListener {
            selectButton(isAllRandom = true)

        }
        binding.btnThemeRandom.setOnClickListener {
            selectButton(isAllRandom = false)
        }
    }

    private fun selectButton(isAllRandom: Boolean) {
        isSelectedAllRandomBtn = isAllRandom
        isSelectedThemeRandomBtn = !isAllRandom

        binding.btnAllRandom.apply {
            setBackgroundResource(if (isAllRandom) R.drawable.shape_yellow_boarder else R.drawable.shape_without_boarder)
        }

        binding.btnThemeRandom.apply {
            setBackgroundResource(if (!isAllRandom) R.drawable.shape_yellow_boarder else R.drawable.shape_without_boarder)
        }

        Log.d("btn", "올랜덤 $isSelectedAllRandomBtn, 테마랜덤 $isSelectedThemeRandomBtn")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
