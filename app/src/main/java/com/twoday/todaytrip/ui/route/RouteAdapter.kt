package com.twoday.todaytrip.ui.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding

class RouteAdapter(
    private val item: MutableList<RouteListData> = mutableListOf()
) : RecyclerView.Adapter<RouteAdapter.ViewHolder>() {



    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val name = binding.tvRoadText
        val num = binding.tvRoutePocketNumber
        val visi = binding.viRouteLastView
        fun bind(item: RouteListData) {
            name.text = item.name
            num.text = item.num.toString()
        }

        override fun onClick(v: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRouteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = item[position]
        holder.name.text = currentItem.name
        holder.num.text = currentItem.num.toString()
        holder.bind(item[position])

        if (position == item.count() - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
    }
}