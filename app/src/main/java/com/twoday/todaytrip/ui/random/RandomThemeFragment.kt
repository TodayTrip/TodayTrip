package com.twoday.todaytrip.ui.random

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomThemeBinding
import com.twoday.todaytrip.utils.DestinationPrefUtil

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
        setUpButtonClickState(false)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    private fun setUpButtonClickState(status: Boolean) {
        binding.btnNext.isEnabled = status
        if(status){
            binding.btnNext.setBackgroundResource(R.drawable.shape_main_blue_12_radius)
            binding.tvBtnNext.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }else{
            binding.btnNext.setBackgroundResource(R.drawable.shape_light_gray_12_radius)
            binding.tvBtnNext.setTextColor(ContextCompat.getColor(requireContext(), R.color.middle_gray))
        }
    }

    private fun setUpClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_random_theme_to_navigation_random_option)
        }
        binding.btnNext.setOnClickListener {
            selectedTheme?.let {
                findNavController().navigate(R.id.action_navigation_random_theme_to_navigation_select_region)
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
                button.setBackgroundResource(R.drawable.shape_yellow_stroke_10_radius)
            } else {
                button.setBackgroundResource(R.drawable.shape_white_10_radius)
            }
        }
        setUpButtonClickState(true)
    }

    private fun selectRandomDestination(theme: String) {
//        val themeDestination = DestinationData.themeDestinations[theme]?.random()
//        Log.d("themeDestination", themeDestination.toString())
        // 지역 선택으로 넘어가서 여행지 선택하니까 테마만 선택된 거 저장하면 될 것 같습니다
//        themeDestination?.let {
//            Log.d("themeDestination", it)
//            DestinationPrefUtil.saveDestination(it)
            DestinationPrefUtil.saveTheme(theme)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}