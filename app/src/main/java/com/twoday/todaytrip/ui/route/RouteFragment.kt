package com.twoday.todaytrip.ui.route

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRouteBinding

class RouteFragment : Fragment() {

    //    private lateinit var adapter: RouteAdapter
    private val itemTouchSimpleCallback = ItemTouchSimpleCallback()
    private val itemTouchHelper = ItemTouchHelper(itemTouchSimpleCallback)
    private val adapter: RouteAdapter by lazy {
        RouteAdapter()
    }
    private lateinit var binding: FragmentRouteBinding
//    private lateinit var mContext: Context

//    private val itemTouchHelper by lazy { ItemTouchHelper(ItemTouchCallback(RouteAdapter)) }


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datalist = mutableListOf<RouteListData>()
        datalist.add(RouteListData("서울역", 1, "어딘가"))
        datalist.add(RouteListData("N서울 타워", 2, "있겠지"))
        datalist.add(RouteListData("청계천", 3, "아무데나"))
        datalist.add(RouteListData("북촌 한옥 마을", 4, "가볼까"))
        datalist.add(RouteListData("경로", 5, "어디든지"))
        datalist.add(RouteListData("경로", 6, "떠나자"))

        adapter.submitList(datalist)
        binding.rvRouteRecyclerview.adapter = adapter

        binding.btnRouteFinish.setOnClickListener {
            val frag = BottomSheetDialog()
            frag.show(childFragmentManager, frag.tag)
        }

        // itemTouchSimpleCallback 인터페이스로 추가 작업
        itemTouchSimpleCallback.setOnItemMoveListener(object :
            ItemTouchSimpleCallback.OnItemMoveListener {
            override fun onItemMove(from: Int, to: Int) {
            }
        })

        // itemTouchHelper와 recyclerview 연결, 아이템 순서변경
        itemTouchHelper.attachToRecyclerView(binding.rvRouteRecyclerview)

//        binding.cvRouteEditFrame.setOnClickListener {
//            if (binding.tvRouteListEdit.isVisible) {
//                binding.tvRouteListEdit.visibility = View.INVISIBLE
//                binding.tvRouteListCompletion.visibility = View.VISIBLE
//            } else {
//                binding.tvRouteListEdit.visibility = View.VISIBLE
//                binding.tvRouteListCompletion.visibility = View.INVISIBLE
//            }
//            Toast.makeText(context, "편집클릭", Toast.LENGTH_SHORT).show()
//
//        }

        adapter.itemClick = object : RouteAdapter.ItemClick {
            override fun onClick(item: RouteListData) {

//                (activity as? MainActivity)?.removeFavorites(item)
                adapter.notifyDataSetChanged()


                Log.d("favoritefragment", "remove  ${item}")

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RouteFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}