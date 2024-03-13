package com.twoday.todaytrip.ui.place_list

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.databinding.FragmentPlaceListTouristAttractionRecyclerViewBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.ui.place_list.adapter.PlaceListAdapter
import com.twoday.todaytrip.viewModel.MainViewModel


class TouristAttractionRecyclerViewFragment : Fragment(), OnTourItemClickListener {
    private val TAG = "TouristAttractionRecyclerViewFragment"

    private var _binding: FragmentPlaceListTouristAttractionRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter: PlaceListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListTouristAttractionRecyclerViewBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userVisibleHint = false
        setLoadingUI(true)
        initRecyclerView()
        initModelObserver()
    }

    private fun setLoadingUI(isLoading: Boolean) {
        binding.shimmerTouristAttractionRecyclerView.isVisible = isLoading
        binding.rvTouristAttractionRecyclerView.isVisible = !isLoading

        if (isLoading) binding.shimmerTouristAttractionRecyclerView.startShimmer()
    }

    private fun initRecyclerView() {
        adapter = PlaceListAdapter().apply {
            onTourItemClickListener = this@TouristAttractionRecyclerViewFragment
        }
        binding.rvTouristAttractionRecyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onTourItemClick(tourItem: TourItem) {
        Log.d(TAG, "onTourItemClick) called, ${tourItem.getTitle()}")
        val placeDetailIntent = PlaceDetailActivity.newIntent(
            requireContext(),
            tourItem.getContentTypeId(),
            tourItem
        )
        startActivity(placeDetailIntent)
    }

    private fun initModelObserver() {
        mainModel.touristAttractionList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "tourist attraction list size: ${it.size}")
            adapter.submitList(it.toMutableList())
            setLoadingUI(false)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}