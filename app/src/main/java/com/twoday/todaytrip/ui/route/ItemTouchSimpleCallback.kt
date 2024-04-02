package com.twoday.todaytrip.ui.route

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

interface OnMoveEndListener{
    fun onMoveEnd()
}

class ItemTouchSimpleCallback : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP
            or ItemTouchHelper.DOWN
            or ItemTouchHelper.START
            or ItemTouchHelper.END,
    0
) {
    private val TAG = "ItemTouchSimpleCallback"

    var onMoveEndListener: OnMoveEndListener? = null

    override fun onMove(
        recyclerView: RecyclerView,
        selectedViewHolder: RecyclerView.ViewHolder,
        targetViewHolder: RecyclerView.ViewHolder
    ): Boolean {
        val routeAdapter = recyclerView.adapter as RouteAdapter

        val fromPosition = selectedViewHolder.absoluteAdapterPosition
        val toPosition = targetViewHolder.absoluteAdapterPosition
        //val dataSet = routeAdapter.currentList.toMutableList()

        //Log.d(TAG, "currentList")
        //routeAdapter.currentList.forEach { Log.d(TAG, "${it.toString()}")}

        //Log.d(TAG, "dataSet")
        //dataSet.forEach { Log.d(TAG, "${it.toString()}") }
        //Collections.swap(dataSet, fromPosition, toPosition)

        ContentIdPrefUtil.swapContentId(fromPosition, toPosition)
        routeAdapter.apply{
            Log.d(TAG, "from: ${fromPosition}, to: ${toPosition}")
            notifyItemMoved(fromPosition,toPosition)
            notifyItemRangeChanged(min(fromPosition, toPosition), abs(fromPosition - toPosition) +1, Any())
        }
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        Log.d(TAG,"clearview")
        super.clearView(recyclerView, viewHolder)

        onMoveEndListener?.onMoveEnd()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Do Nothing
    }
}