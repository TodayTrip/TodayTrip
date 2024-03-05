package com.twoday.todaytrip.ui.random

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomOptionBinding
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil

class RandomOptionFragment : Fragment() {
    private var _binding: FragmentRandomOptionBinding? = null
    private val binding get() = _binding!!
    private var isSelectedAllRandomBtn = true
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
        binding.btnOptionSelect.setOnClickListener {
            if (isSelectedAllRandomBtn) {
                selectRandomDestination() // 전체 랜덤 선택 시 Sharf에 여행지 랜덤 저장
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
        isSelectedAllRandomBtn = isAllRandom
        isSelectedThemeRandomBtn = !isAllRandom

        binding.btnAllRandom.apply {
            setBackgroundResource(if (isAllRandom) R.drawable.shape_yellow_boarder else R.drawable.shape_without_boarder)
        }

        binding.btnThemeRandom.apply {
            setBackgroundResource(if (!isAllRandom) R.drawable.shape_yellow_boarder else R.drawable.shape_without_boarder)
        }
    }

    // 전체 랜덤 시 여행지 랜덤 선택하는 함수, 테마 Sharf에는 null로 저장
    private fun selectRandomDestination() {
        val randomDestination = DestinationData.allRandomDestination.random()
        DestinationPrefUtil.saveDestination(randomDestination)
        DestinationPrefUtil.saveTheme("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
