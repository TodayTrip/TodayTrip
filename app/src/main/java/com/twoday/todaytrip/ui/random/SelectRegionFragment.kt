package com.twoday.todaytrip.ui.random

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.fragment.findNavController
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
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

    private var selectedRegionList = mutableListOf<String>()

    private val map by lazy { binding.ivSelectRegionMap }
    private val mapVector by lazy { VectorChildFinder(context, R.drawable.img_select_region, map) }

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
        fillMap()
        initChipSet()
//        SelectRegionPrefUtil.resetSelectRegionListPref()
        setUpClickListener()
    }

    private fun initChipSet() {
        val selectedRegionPrefList = SelectRegionPrefUtil.loadSelectRegionList().toMutableList()
        chips.forEach {
            if (selectedRegionPrefList.contains(it.text)){
                it.isChecked = true
                it.isSelected = true
                val selected = mapVector.findPathByName(it.text.toString())
                selected.fillColor = Color.BLUE
            }
        }
    }

    private fun fillMap() {
        chips.forEach {
            val selected = mapVector.findPathByName(it.text.toString())
            it.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d(TAG, "selected chip=${it.text.toString()}, selected map=${selected.toString()}")
                Log.d(TAG, "selected regions=${selectedRegionList}")
                if (isChecked) {
                    selected.fillColor = Color.BLUE
                    selectedRegionList.add(it.text.toString())
                } else {
                    selected.fillColor = Color.GRAY
                    selectedRegionList.remove(it.text.toString())
                }
                updateNextBtn()
                map.invalidate()
            }
        }
    }

    private fun setUpClickListener() {
        binding.btnRegionSelectNext.setOnClickListener {
            SelectRegionPrefUtil.resetSelectRegionListPref()
            SelectRegionPrefUtil.saveSelectRegionList(selectedRegionList)
            val selectedDestination = SelectRegionPrefUtil.loadSelectRegionList().random()
            DestinationPrefUtil.saveDestination(selectedDestination)
            findNavController().navigate(R.id.action_navigation_select_region_to_navigation_random_result)
        }
        //
    }

    private fun updateNextBtn() {
        if (selectedRegionList.isNotEmpty()) {
            binding.btnRegionSelectNext.isEnabled = true
            binding.btnRegionSelectNext.setBackgroundResource(R.drawable.shape_main_blue_12_radius)
        } else {
            binding.btnRegionSelectNext.isEnabled = false
            binding.btnRegionSelectNext.setBackgroundResource(R.drawable.shape_middle_gray_12_radius)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}