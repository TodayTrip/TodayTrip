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
import com.twoday.todaytrip.databinding.FragmentRandomThemeBinding

class RandomThemeFragment : Fragment() {
    private var _binding: FragmentRandomThemeBinding? = null
    private val binding get() = _binding!!
    private var isBtnSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomThemeBinding.inflate(inflater, container, false)
        binding.btnTripTheme.isEnabled = false
        setUpClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpClickListener() {
        binding.btnTripTheme.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_random_theme_to_navigation_random_result)
        }
        binding.btnTheme1.setOnClickListener {
            selectButton(selectedNumber = 0)
        }
        binding.btnTheme2.setOnClickListener {
            selectButton(selectedNumber = 1)
        }
        binding.btnTheme3.setOnClickListener {
            selectButton(selectedNumber = 2)
        }
        binding.btnTheme4.setOnClickListener {
            selectButton(selectedNumber = 3)
        }
        binding.btnTheme5.setOnClickListener {
            selectButton(selectedNumber = 4)
        }
        binding.btnTheme6.setOnClickListener {
            selectButton(selectedNumber = 5)
        }
        binding.btnTheme7.setOnClickListener {
            selectButton(selectedNumber = 6)
        }
    }

    private fun selectButton(selectedNumber: Int) {
        // 모든 버튼의 선택 상태를 초기화
        val buttons = listOf(
            binding.btnTheme1,
            binding.btnTheme2,
            binding.btnTheme3,
            binding.btnTheme4,
            binding.btnTheme5,
            binding.btnTheme6,
            binding.btnTheme7
        )

        // 모든 버튼을 순회하면서, 선택된 번호의 버튼에만 특정 스타일 적용
        for ((index, button) in buttons.withIndex()) {
            if (index == selectedNumber) {
                button.setBackgroundResource(R.drawable.shape_yellow_boarder)
            } else {
                button.setBackgroundResource(R.drawable.shape_without_boarder)
            }
        }

        // '다음' 버튼 활성화
        binding.btnTripTheme.isEnabled = true

        Log.d("btn", "선택된 테마는 ${selectedNumber}번째 테마")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}