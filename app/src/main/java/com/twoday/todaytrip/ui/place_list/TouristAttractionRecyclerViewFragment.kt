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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
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

    private lateinit var touristAttractionAdapter: PlaceListAdapter
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

        initUI()
        initSwipeRefreshLayout()
        initRecyclerView()
        initModelObserver()
    }

    private fun initUI(){
        setLoadingUI(true)
        setNoResultUI(false)

        Glide.with(requireContext())
            .load(resources.getDrawable(R.drawable.gif_place_list_no_result))
            .into(binding.ivTouristAttractionRecyclerViewNoResult)
    }
    private fun setNoResultUI(isNoResult: Boolean){
        Log.d(TAG, "setNoResultUI) isNoResult: $isNoResult")
        binding.layoutTouristAttractionRecyclerViewNoResult.isVisible = isNoResult
    }
    private fun setLoadingUI(isLoading: Boolean) {
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.shimmerTouristAttractionRecyclerView.isVisible = isLoading
        if (isLoading) binding.shimmerTouristAttractionRecyclerView.startShimmer()
    }

    private fun initSwipeRefreshLayout(){
        binding.swipeTouristAttractionRecyclerView.setOnRefreshListener {
            setNoResultUI(false)
            setLoadingUI(true)
            mainModel.loadOrFetchTouristAttractionList()

            binding.swipeTouristAttractionRecyclerView.isRefreshing = false
        }
    }
    private fun initRecyclerView() {
        touristAttractionAdapter = PlaceListAdapter().apply {
            onTourItemClickListener = this@TouristAttractionRecyclerViewFragment
        }
        binding.rvTouristAttractionRecyclerView.run{
            this.adapter = touristAttractionAdapter
            initScrollListener(this)
        }
    }
    private fun initScrollListener(recyclerView: RecyclerView){
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "recyclerview end of scroll!")
                    Log.d(TAG, "adapter current list size: ${touristAttractionAdapter.currentList.size}")
                    Log.d(TAG, "isTouristAttractionLoadReady: ${mainModel.isTouristAttractionLoadReady}")

                    if((touristAttractionAdapter.currentList.isNotEmpty()) &&
                        (mainModel.isTouristAttractionLoadReady)){
                        Log.d(TAG, "fetch and save more tourist attraction list")
                        touristAttractionAdapter.addDummyTourItem()
                        mainModel.fetchAndSaveMoreTouristAttractionList()
                    }
                }
            }
        })
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
        mainModel.touristAttractionList.observe(viewLifecycleOwner, Observer {touristAttractionList ->
            Log.d(TAG, "observe) tourist attraction list size: ${touristAttractionList.size}")
            touristAttractionAdapter.submitList(touristAttractionList.toMutableList())
            Log.d(TAG, "observe) current list size: ${touristAttractionAdapter.currentList.size}")

            setLoadingUI(false)
            if(touristAttractionList.isEmpty()) setNoResultUI(true)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}