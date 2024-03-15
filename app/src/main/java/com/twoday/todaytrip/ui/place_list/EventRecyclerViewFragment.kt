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
import com.twoday.todaytrip.databinding.FragmentPlaceListEventRecyclerViewBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.ui.place_list.adapter.PlaceListAdapter
import com.twoday.todaytrip.utils.showSnackBar
import com.twoday.todaytrip.viewModel.MainViewModel


class EventRecyclerViewFragment : Fragment(), OnTourItemClickListener {
    private val TAG = "EventRecyclerViewFragment"

    private var _binding: FragmentPlaceListEventRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var eventAdapter: PlaceListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            FragmentPlaceListEventRecyclerViewBinding.inflate(layoutInflater, container, false)
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
            .into(binding.ivEventRecyclerViewNoResult)
    }

    private fun setNoResultUI(isNoResult: Boolean) {
        Log.d(TAG, "setNoResultUI) isNoResult: $isNoResult")
        binding.layoutEventRecyclerViewNoResult.isVisible = isNoResult
    }

    private fun setLoadingUI(isLoading: Boolean) {
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.shimmerEventRecyclerView.isVisible = isLoading
        if (isLoading) binding.shimmerEventRecyclerView.startShimmer()
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeEventRecyclerView.setOnRefreshListener {
            setNoResultUI(false)
            setLoadingUI(true)
            mainModel.loadOrFetchEventList()

            binding.swipeEventRecyclerView.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        eventAdapter = PlaceListAdapter().apply {
            onTourItemClickListener = this@EventRecyclerViewFragment
        }
        binding.rvEventRecyclerView.run {
            this.adapter = eventAdapter
            initScrollListener(this)
        }
    }

    private fun initScrollListener(recyclerView: RecyclerView) {
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "recyclerview end of scroll!")
                    Log.d(TAG, "adapter current list size: ${eventAdapter.currentList.size}")
                    Log.d(TAG, "isEventLoadReady: ${mainModel.isEventLoadReady}")

                    if ((eventAdapter.currentList.isNotEmpty()) &&
                        (mainModel.isEventLoadReady)
                    ) {
                        Log.d(TAG, "fetch and save more event list")
                        eventAdapter.addDummyTourItem()
                        mainModel.fetchAndSaveMoreEventList()
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
        mainModel.eventList.observe(viewLifecycleOwner, Observer {eventList ->
            Log.d(TAG, "observe) event list size: ${eventList.size}")
            eventAdapter.submitList(eventList.toMutableList())
            Log.d(TAG, "observe) current list size: ${eventAdapter.currentList.size}")

            setLoadingUI(false)
            if (eventList.isEmpty()) setNoResultUI(true)

        })

        mainModel.eventMoreLoaded.observe(viewLifecycleOwner){
            if(it == 0) return@observe

            Log.d(TAG, "observe) eventMoreLoaded: $it")
            eventAdapter.removeDummyTourItem()
            showSnackBar(
                message = R.string.place_list_more_event_no_result,
                anchorView = requireActivity().findViewById(R.id.fab_bottom_random)
            )
            mainModel.setEventMoreLoadedDefault()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}