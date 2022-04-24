package com.charityright.charityauditor.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.auditor.viewModel.FieldVisitViewModel
import com.charityright.charityauthority.databinding.NutritionItemBinding

class NutritionAdapter(private val fieldVisitViewModel: FieldVisitViewModel) : RecyclerView.Adapter<NutritionAdapter.ViewHolder>() {

    var size = fieldVisitViewModel.nutritionTitleList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionAdapter.ViewHolder {
        return ViewHolder(
            NutritionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NutritionAdapter.ViewHolder, position: Int) {

        holder.binding.titleTV.text = fieldVisitViewModel.nutritionTitleList[position]
        holder.binding.amountTV.text = fieldVisitViewModel.nutritionAmountList[position]

        holder.binding.deleteBtn.setOnClickListener {
            fieldVisitViewModel.nutritionTitleList.removeAt(position)
            fieldVisitViewModel.nutritionAmountList.removeAt(position)
            getNewCount(fieldVisitViewModel.nutritionTitleList.size)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return size
    }

    fun getNewCount(temp: Int) {
        size = temp
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: NutritionItemBinding) : RecyclerView.ViewHolder(binding.root)
}