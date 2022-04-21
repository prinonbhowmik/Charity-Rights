package com.charityright.bd.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.databinding.HomeSliderItemBinding

class HomeViewPagerAdapter(private val goalCardImageList: ArrayList<Int>, private val goalImageList: ArrayList<Int>) : RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            HomeSliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.backgroundImageView.setImageResource(goalCardImageList[position])
        holder.binding.goalImageView.setImageResource(goalImageList[position])
    }

    override fun getItemCount(): Int {
        return goalCardImageList.size
    }

    class ViewHolder(val binding: HomeSliderItemBinding) : RecyclerView.ViewHolder(binding.root)

}