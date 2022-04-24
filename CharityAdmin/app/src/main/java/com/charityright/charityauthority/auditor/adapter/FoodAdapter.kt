package com.charityright.charityauthority.auditor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.auditor.viewModel.FieldVisitViewModel
import com.charityright.charityauthority.databinding.FoodItemRecyclerBinding
import com.charityright.charityauthority.databinding.NutritionItemBinding
import com.charityright.charityauthority.model.FoodlistItem

class FoodAdapter(private val fieldVisitViewModel: FieldVisitViewModel) : RecyclerView.Adapter<FoodAdapter.ViewHolder>()  {

    var size = fieldVisitViewModel.foodlistItem.size

    class ViewHolder(val binding: FoodItemRecyclerBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( FoodItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleTV.text = fieldVisitViewModel.foodlistItem[position].food_name

        holder.binding.deleteBtn.setOnClickListener {
            fieldVisitViewModel.foodlistItem.removeAt(position)
            getNewCount(fieldVisitViewModel.foodlistItem.size)
            notifyDataSetChanged()
        }
    }

    fun getNewCount(temp: Int) {
        size = temp
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
       return size
    }
}