package com.twoday.todaytrip.ui.place_list

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceListEventRecyclerViewBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.TourItemJsonConverter
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.ui.place_list.adapter.PlaceListAdapter
import com.twoday.todaytrip.utils.glide
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

    private val eventAdapter = PlaceListAdapter()
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

    override fun onResume() {
        super.onResume()
        mainModel.loadOrFetchEventList()
    }

    private fun initUI() {
        setLoadingUI(true)
        setNoResultUI(false)

        binding.ivEventRecyclerViewNoResult.glide(R.drawable.gif_loading_reading_glasses)
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
        eventAdapter.onTourItemClickListener = this@EventRecyclerViewFragment
        binding.rvEventRecyclerView.run {
            this.adapter = eventAdapter
            initScrollListener(this)
        }
        initFloatingButton()
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
            requireContext(), TourItemJsonConverter.toJson(tourItem)
        )
        startActivity(placeDetailIntent)
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun initModelObserver() {
        mainModel.eventList.observe(viewLifecycleOwner, Observer { eventList ->
            Log.d(TAG, "observe) event list size: ${eventList.size}")
            eventAdapter.submitList(eventList.toMutableList())
            Log.d(TAG, "observe) current list size: ${eventAdapter.currentList.size}")

            setLoadingUI(false)
            if (eventList.isEmpty()) setNoResultUI(true)

        })

        mainModel.eventMoreLoaded.observe(viewLifecycleOwner) {
            if (it == 0) return@observe

            Log.d(TAG, "observe) eventMoreLoaded: $it")
            //eventAdapter.removeDummyTourItem()
            if (it == -1)
                showSnackBar(
                    message = R.string.place_list_more_event_no_result,
                    anchorView = requireActivity().findViewById(R.id.fab_bottom_random)
                )
            mainModel.setEventMoreLoadedDefault()
        }
    }

    private fun initFloatingButton() {
        // 플로팅 버튼
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true
        binding.rvEventRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.rvEventRecyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fbContactFloating.startAnimation(fadeOut)
                    binding.fbContactFloating.visibility = View.GONE
                    isTop = true
                } else {
                    if (isTop) {
                        binding.fbContactFloating.visibility = View.VISIBLE
                        binding.fbContactFloating.startAnimation(fadeIn)
                        isTop = false
                    }
                }
            }
        })
        binding.fbContactFloating.setOnClickListener {
            binding.rvEventRecyclerView.smoothScrollToPosition(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}