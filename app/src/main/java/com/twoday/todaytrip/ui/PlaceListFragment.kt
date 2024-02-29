package com.twoday.todaytrip.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoday.todaytrip.adapter.PagerFragmentStateAdapter
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding


class PlaceListFragment : Fragment() {
    private var _binding: FragmentPlaceListBinding? = null
    private val binding get() = _binding!!
    private lateinit var ViewPagerAdapter: PagerFragmentStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

    }

    private fun initAdapter() {

        ViewPagerAdapter = PagerFragmentStateAdapter(requireActivity())

        with(ViewPagerAdapter) {
            addFragment(FirstRecyclerViewFragment())
            addFragment(SecondRecyclerViewFragment())
            addFragment(ThirdRecyclerViewFragment())
        }

        binding.vpViewpagerMain.adapter = ViewPagerAdapter
        setUpClickListener()
    }
    private fun setUpClickListener() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}