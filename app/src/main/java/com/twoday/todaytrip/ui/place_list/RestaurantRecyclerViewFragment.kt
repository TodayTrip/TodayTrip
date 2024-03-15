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
import com.twoday.todaytrip.databinding.FragmentPlaceListRestaurantRecyclerViewBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.ui.place_list.adapter.PlaceListAdapter
import com.twoday.todaytrip.utils.showSnackBar
import com.twoday.todaytrip.viewModel.MainViewModel


class RestaurantRecyclerViewFragment : Fragment(), OnTourItemClickListener {
    private val TAG = "RestaurantRecyclerViewFragment"

    private var _binding: FragmentPlaceListRestaurantRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var restaurantAdapter: PlaceListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            FragmentPlaceListRestaurantRecyclerViewBinding.inflate(
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

    private fun initUI() {
        setLoadingUI(true)
        setNoResultUI(false)

        Glide.with(requireContext())
            .load(resources.getDrawable(R.drawable.gif_loading_reading_glasses))
            .into(binding.ivRestaurantRecyclerViewNoResult)
    }

    private fun setNoResultUI(isNoResult: Boolean) {
        Log.d(TAG, "setNoResultUI) isNoResult: $isNoResult")
        binding.layoutRestaurantRecyclerViewNoResult.isVisible = isNoResult
    }

    private fun setLoadingUI(isLoading: Boolean) {
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.shimmerRestaurantRecyclerView.isVisible = isLoading
        if (isLoading) binding.shimmerRestaurantRecyclerView.startShimmer()
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRestaurantRecyclerView.setOnRefreshListener {
            setNoResultUI(false)
            setLoadingUI(true)
            mainModel.loadOrFetchRestaurantList()

            binding.swipeRestaurantRecyclerView.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        restaurantAdapter = PlaceListAdapter().apply {
            onTourItemClickListener = this@RestaurantRecyclerViewFragment
        }
        binding.rvRestaurantRecyclerView.run {
            this.adapter = restaurantAdapter
            initScrollListener(this)
        }
    }

    private fun initScrollListener(recyclerView: RecyclerView) {
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "recyclerview end of scroll!")
                    Log.d(TAG, "adapter current list size: ${restaurantAdapter.currentList.size}")
                    Log.d(TAG, "isRestaurantLoadReady: ${mainModel.isRestaurantLoadReady}")

                    if ((restaurantAdapter.currentList.isNotEmpty()) &&
                        (mainModel.isRestaurantLoadReady)
                    ) {
                        Log.d(TAG, "fetch and save more restaurant list")
                        restaurantAdapter.addDummyTourItem()
                        mainModel.fetchAndSaveMoreRestaurantList()
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
        mainModel.restaurantList.observe(viewLifecycleOwner, Observer { restaurantList ->
            Log.d(TAG, "observe) restaurant list size: ${restaurantList.size}")
            restaurantAdapter.submitList(restaurantList.toMutableList())
            Log.d(TAG, "observe) current list size: ${restaurantAdapter.currentList.size}")

            setLoadingUI(false)
            if (restaurantList.isEmpty()) setNoResultUI(true)
        })

        mainModel.restaurantMoreLoaded.observe(viewLifecycleOwner){
            if(it == 0) return@observe

            Log.d(TAG, "observe) restaurantMoreLoaded: $it")
            restaurantAdapter.removeDummyTourItem()
            showSnackBar(
                message = R.string.place_list_more_restaurant_no_result,
                anchorView = requireActivity().findViewById(R.id.fab_bottom_random)
            )
            mainModel.setRestaurantMoreLoadedDefault()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}