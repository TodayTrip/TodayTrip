package com.twoday.todaytrip.ui.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRandomOptionBinding
import com.twoday.todaytrip.viewModel.RandomOptionViewModel

class RandomOptionFragment : Fragment() {

    private var _binding: FragmentRandomOptionBinding? = null
    private val binding get() = _binding!!

    private val model by viewModels<RandomOptionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRandomOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListener()
        initModelObserver()

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    private fun setUpClickListener() {
        binding.btnOptionNext.setOnClickListener {
            if(!model.isButtonClickable.value!!) return@setOnClickListener

            if (model.isSelectedAllRandomBtn.value!!) {
                findNavController().navigate(R.id.action_navigation_random_option_to_navigation_select_region)
            } else {
                findNavController().navigate(R.id.action_navigation_random_option_to_navigation_random_theme)
            }
        }
        binding.btnAllRandom.setOnClickListener {
            model.selectAllRandomBtn()
        }
        binding.btnThemeRandom.setOnClickListener {
            model.selectThemeRandomBtn()
        }
    }

    private fun initModelObserver() {
        model.isSelectedAllRandomBtn.observe(viewLifecycleOwner) { isSelected ->
            setAllRandomButtonStyle(isSelected)
        }
        model.isSelectedThemeRandomBtn.observe(viewLifecycleOwner) { isSelected ->
            setThemeRandomButtonStyle(isSelected)
        }
        model.isButtonClickable.observe(viewLifecycleOwner) { isClickable ->
            setNextButtonStyle(isClickable)
        }
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

    private fun setNextButtonStyle(isButtonClickable: Boolean) {
        binding.btnOptionNext.isEnabled = isButtonClickable
        binding.btnOptionNext.setBackgroundResource(
            if (isButtonClickable) R.drawable.shape_main_blue_12_radius
            else R.drawable.shape_middle_gray_12_radius
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
