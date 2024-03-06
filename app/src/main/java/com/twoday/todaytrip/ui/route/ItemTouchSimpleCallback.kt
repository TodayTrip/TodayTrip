package com.twoday.todaytrip.ui.route

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class ItemTouchSimpleCallback : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP
            or ItemTouchHelper.DOWN
            or ItemTouchHelper.START
            or ItemTouchHelper.END,
    0
) {

    override fun onMove(
        recyclerView: RecyclerView,
        selectedViewHolder: RecyclerView.ViewHolder,
        targetViewHolder: RecyclerView.ViewHolder
    ): Boolean {
        val routeAdapter = recyclerView.adapter as RouteAdapter

        val fromPosition = selectedViewHolder.absoluteAdapterPosition
        val toPosition = targetViewHolder.absoluteAdapterPosition

        val dataSet = routeAdapter.currentList.toMutableList()
        Collections.swap(dataSet, fromPosition, toPosition)
        routeAdapter.apply{
            //submitList(dataSet)
            notifyItemMoved(fromPosition, toPosition)
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Do Nothing
    }
}