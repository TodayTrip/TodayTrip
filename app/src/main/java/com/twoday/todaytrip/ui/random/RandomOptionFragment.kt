package com.twoday.todaytrip.ui.random

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomOptionBinding
import com.twoday.todaytrip.viewModel.RandomViewModel

class RandomOptionFragment : Fragment() {

    private var _binding: FragmentRandomOptionBinding? = null
    private val binding get() = _binding!!
    private var isSelectedAllRandomBtn = false
    private var isSelectedThemeRandomBtn = false
    private var isButtonClickable = false
    private val viewModel by lazy {
        ViewModelProvider(this@RandomOptionFragment)[RandomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpClickListener()
    }

    private fun initView() {
        binding.btnOptionSelect.visibility = View.INVISIBLE
    }

    private fun setUpClickListener() {
        binding.btnOptionSelect.setOnClickListener {
            if (isSelectedAllRandomBtn) {
                // 전체 랜덤 선택 시 Sharf에 여행지 랜덤 저장
                viewModel.selectRandomDestination()
                findNavController().navigate(R.id.action_navigation_random_option_to_navigation_random_result)
            } else {
                findNavController().navigate(R.id.action_navigation_random_option_to_navigation_random_theme)
            }
        }
        binding.btnAllRandom.setOnClickListener {
            selectButton(isAllRandom = true)
        }
        binding.btnThemeRandom.setOnClickListener {
            selectButton(isAllRandom = false)
        }
    }

    private fun selectButton(isAllRandom: Boolean) {
        isButtonClickable = true
        if (isAllRandom) {
            isSelectedAllRandomBtn = true
            isSelectedThemeRandomBtn = false
        } else {
            isSelectedAllRandomBtn = false
            isSelectedThemeRandomBtn = true
        }

        updateRandomButtonStyle()
        updateNextButtonStyle(isButtonClickable)
    }

    private fun updateNextButtonStyle(isButtonClickable: Boolean) {
        binding.btnOptionSelect.visibility = View.VISIBLE
        binding.btnOptionSelect.isEnabled = isButtonClickable
        binding.btnOptionSelect.setBackgroundResource(R.drawable.shape_main_blue_12_radius)
    }

    private fun updateRandomButtonStyle() {
        setAllRandomButtonStyle(isSelectedAllRandomBtn)
        setThemeRandomButtonStyle(isSelectedThemeRandomBtn)
    }

    private fun setAllRandomButtonStyle(isSelected: Boolean) {
        binding.btnAllRandom.apply {
            if (isSelected) {
                binding.ivAllRandomGray.visibility = View.INVISIBLE
            } else {
                binding.ivAllRandomGray.visibility = View.VISIBLE
            }
        }
    }

    private fun setThemeRandomButtonStyle(isSelected: Boolean) {
        binding.btnThemeRandom.apply {
            if (!isSelected) {
                binding.ivThemeRandomGray.visibility = View.VISIBLE
            } else {
                binding.ivThemeRandomGray.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
