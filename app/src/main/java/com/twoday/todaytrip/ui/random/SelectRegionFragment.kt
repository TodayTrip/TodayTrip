package com.twoday.todaytrip.ui.random

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.devs.vectorchildfinder.VectorChildFinder
import com.google.android.material.chip.Chip
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentSelectRegionBinding
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.SelectRegionPrefUtil
import com.twoday.todaytrip.viewModel.SelectRegionViewModel

class SelectRegionFragment : Fragment() {

    private val TAG = "SelectRegionBinding"

    private var _binding: FragmentSelectRegionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SelectRegionViewModel>()

    private val chips by lazy {
        listOf(
            binding.chipSelectRegionSeoul,
            binding.chipSelectRegionIncheon,
            binding.chipSelectRegionJeonbuk,
            binding.chipSelectRegionJeonnam,
            binding.chipSelectRegionGyeongbuk,
            binding.chipSelectRegionGyeongnam,
            binding.chipSelectRegionChungbuk,
            binding.chipSelectRegionChungnam,
            binding.chipSelectRegionGangwon,
            binding.chipSelectRegionDaegu,
            binding.chipSelectRegionBusan,
            binding.chipSelectRegionDaejeon,
            binding.chipSelectRegionJeju,
            binding.chipSelectRegionGyeonggi,
            binding.chipSelectRegionGwangju,
            binding.chipSelectRegionUlsan
        )
    }

    private val map by lazy { binding.ivSelectRegionMap }
    private val mapVector by lazy { VectorChildFinder(context, R.drawable.img_korea_map, map) }

    private val blueColorIdMap by lazy{
        mapOf<String, Int>(
            "서울" to R.color.map_blue3,
        "인천" to R.color.map_blue1,
        "전북" to R.color.map_blue2,
        "전남" to R.color.map_blue3,
        "경북" to R.color.map_blue2,
        "경남" to R.color.map_blue5,
        "충북" to R.color.map_blue4,
        "충남" to R.color.map_blue3,
        "강원" to R.color.map_blue6,
        "대구" to R.color.map_blue4,
        "부산" to R.color.map_blue1,
        "대전" to R.color.map_blue1,
        "제주" to R.color.map_blue2,
        "경기" to R.color.map_blue2,
        "광주" to R.color.map_blue5,
        "울산" to R.color.map_blue6
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)

        setUpAllButtonClickListener()
        setUpChipClickListener()
        setUpNextButtonListener()
        initModelObserver()
    }

    private fun initModelObserver() {
        viewModel.selectedRegionList.observe(viewLifecycleOwner) { selectedRegionList ->
            map.invalidate()

            chips.forEach {
                it.isChecked = selectedRegionList.contains(it.text)
                it.fillMap()
            }
            updateSelectAllBtn(selectedRegionList.size == 16)
            updateNextBtn(selectedRegionList.isNotEmpty())
        }
    }

    private fun Chip.fillMap() {
        val selected = mapVector.findPathByName(this.text.toString())
        if (this.isChecked) {
            selected.fillColor = resources.getColor(blueColorIdMap[this.text.toString()]!!)
        } else {
            selected.fillColor = Color.rgb(217, 217, 217)
        }
    }

    private fun setUpAllButtonClickListener() {
        binding.btnSelectRegionAll.setOnClickListener {
            if (viewModel.selectedRegionList.value!!.size == 16) {
                viewModel.clearSelectedRegionList()
            } else {
                chips.forEach {
                    viewModel.addSelectedRegion(it.text.toString())
                }
            }
        }
    }

    private fun setUpChipClickListener() {
        chips.forEach {
            it.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addSelectedRegion(it.text.toString())
                } else {
                    viewModel.removeSelectedRegion(it.text.toString())
                }
            }
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun updateSelectAllBtn(isChecked: Boolean) {
        if (isChecked) {
            binding.btnSelectRegionAll.setBackgroundResource(R.drawable.shape_main_blue_12_radius)
            binding.btnSelectRegionAll.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        } else {
            binding.btnSelectRegionAll.setBackgroundResource(R.drawable.shape_light_gray_12_radius)
            binding.btnSelectRegionAll.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dark_gray
                )
            )
        }
    }

    private fun updateNextBtn(isEnabled: Boolean) {
        binding.btnRegionSelectNext.setBackgroundResource(
            if (isEnabled) R.drawable.shape_main_blue_12_radius
            else R.drawable.shape_middle_gray_12_radius
        )
        binding.btnRegionSelectNext.isEnabled = isEnabled
    }

    private fun setUpNextButtonListener() {
        binding.btnRegionSelectNext.setOnClickListener {
            viewModel.saveSelectedRegionList()
            viewModel.selectAndSaveDestination()
            findNavController().navigate(R.id.action_navigation_select_region_to_navigation_random_result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}