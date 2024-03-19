package com.twoday.todaytrip.ui.random

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.devs.vectorchildfinder.VectorChildFinder
import com.google.android.material.chip.Chip
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentSelectRegionBinding
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.SelectRegionPrefUtil

class SelectRegionFragment : Fragment() {

    private val TAG = "SelectRegionBinding"

//    companion object {
//        fun newInstance() = SelectRegionFragment()
//    }

    private var _binding: FragmentSelectRegionBinding? = null
    private val binding get() = _binding!!

//    private lateinit var viewModel: SelectRegionViewModel

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

    private lateinit var selectedRegionList: MutableSet<String>

    private val map by lazy { binding.ivSelectRegionMap }
    private val mapVector by lazy { VectorChildFinder(context, R.drawable.img_korea_map, map) }

    private val blueColorRGBList
        get() = listOf(
            Color.rgb(5, 8, 98),
            Color.rgb(4, 63, 136),
            Color.rgb(0, 121, 180),
            Color.rgb(2, 151, 201),
            Color.rgb(6, 180, 214),
            Color.rgb(68, 204, 233),
            Color.rgb(145, 225, 238),
            Color.rgb(175, 231, 249),
            Color.rgb(202, 240, 246),
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initChipSet()
        setUpClickListener()
    }

    private fun initChipSet() {
        val selectedRegionPrefList = SelectRegionPrefUtil.loadSelectRegionList()
        selectedRegionList = selectedRegionPrefList.toMutableSet()

        chips.forEach {
            if (selectedRegionPrefList.contains(it.text)){
                it.isChecked = true
            }
            it.fillMap()
        }
        updateSelectAllBtn(selectedRegionList.size == 16)
    }

    private fun Chip.fillMap() {
            val selected = mapVector.findPathByName(this.text.toString())
            if (this.isChecked) {
                selected.fillColor = blueColorRGBList.random()
            } else {
                selected.fillColor = Color.rgb(217,217,217)
            }
    }

    private fun setUpClickListener() {
        updateNextBtn()
        binding.btnSelectRegionAll.setOnClickListener {
            if (selectedRegionList.size == 16) {
                chips.forEach {
                    it.isChecked = false
                    it.fillMap()
                }
                selectedRegionList.clear()
                updateSelectAllBtn(false)
            } else {
                chips.forEach {
                    it.isChecked = true
                    selectedRegionList.add(it.text.toString())
                    it.fillMap()
                }
                updateSelectAllBtn(true)
            }
        }
        chips.forEach {
            val selected = mapVector.findPathByName(it.text.toString())
            it.setOnCheckedChangeListener { _, isChecked ->
                Log.d(TAG, "selected chip=${it.text.toString()}, selected map=${selected.toString()}")
                Log.d(TAG, "selected regions=${selectedRegionList}")
                if (it.isChecked) {
                    selectedRegionList.add(it.text.toString())
                } else {
                    selectedRegionList.remove(it.text.toString())
                }
                updateSelectAllBtn(selectedRegionList.size == 16)
                it.fillMap()
                map.invalidate()
                updateNextBtn()
            }
        }
        binding.btnRegionSelectNext.setOnClickListener {
            SelectRegionPrefUtil.resetSelectRegionListPref()
            SelectRegionPrefUtil.saveSelectRegionList(selectedRegionList.toMutableList())

            val selectedDestination = SelectRegionPrefUtil.loadSelectRegionList().random()
            DestinationPrefUtil.saveDestination(selectedDestination)
            findNavController().navigate(R.id.action_navigation_select_region_to_navigation_random_result)
        }
    }

    private fun updateNextBtn() {
        if (selectedRegionList.isNotEmpty()) {
            binding.btnRegionSelectNext.setBackgroundResource(R.drawable.shape_main_blue_12_radius)
            binding.btnRegionSelectNext.isEnabled = true
        } else {
            binding.btnRegionSelectNext.setBackgroundResource(R.drawable.shape_middle_gray_12_radius)
            binding.btnRegionSelectNext.isEnabled = false
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun updateSelectAllBtn(isChecked: Boolean) {
        if (isChecked) {
            binding.btnSelectRegionAll.setBackgroundResource(R.drawable.shape_main_blue_12_radius)
            binding.btnSelectRegionAll.setTextColor(R.color.selector_chip_text)
        } else {
            binding.btnSelectRegionAll.setBackgroundResource(R.drawable.shape_light_gray_12_radius)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}