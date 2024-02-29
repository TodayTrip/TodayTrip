package com.twoday.todaytrip.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomThemeBinding
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.SharedPreferencesUtil

class RandomThemeFragment : Fragment() {
    private var _binding: FragmentRandomThemeBinding? = null
    private val binding get() = _binding!!
    private var selectedTheme: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomThemeBinding.inflate(inflater, container, false)
        binding.btnNext.isEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()
    }

    private fun setUpClickListener() {
        binding.btnNext.setOnClickListener {
            selectedTheme?.let {
                val action = RandomThemeFragmentDirections.actionNavigationRandomThemeToNavigationRandomResult()
                findNavController().navigate(action)
            }
        }
        binding.btnTheme1.setOnClickListener { selectTheme("산", 0) }
        binding.btnTheme2.setOnClickListener { selectTheme("바다", 1) }
        binding.btnTheme3.setOnClickListener { selectTheme("역사", 2) }
        binding.btnTheme4.setOnClickListener { selectTheme("휴양", 3) }
        binding.btnTheme5.setOnClickListener { selectTheme("체험", 4) }
        binding.btnTheme6.setOnClickListener { selectTheme("레포츠", 5) }
        binding.btnTheme7.setOnClickListener { selectTheme("문화시설", 6) }
    }

    private fun selectTheme(theme: String, selectedNumber: Int) {
        selectedTheme = theme
        selectButton(selectedNumber)
        selectRandomDestination(theme)
    }
    private fun selectButton(selectedNumber: Int) {
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
        binding.btnNext.isEnabled = true
    }

    private fun selectRandomDestination(theme: String) {
        val themeDestination = DestinationData.themeDestinations[theme]?.random()
        Log.d("themeDestination", themeDestination.toString())
        themeDestination?.let {
            Log.d("themeDestination", it)
            SharedPreferencesUtil.saveDestination(requireContext(), it, PrefConstants.DESTINATION_KEY)
            SharedPreferencesUtil.saveDestination(requireContext(), theme, PrefConstants.THEME_KEY)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}