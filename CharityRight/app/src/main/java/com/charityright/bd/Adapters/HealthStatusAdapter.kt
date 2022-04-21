package com.charityright.bd.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.StudentDetailsModel.HealthStatus
import com.charityright.bd.databinding.HealthStatusItemBinding

class HealthStatusAdapter(private val healthStatus: List<HealthStatus?>) : RecyclerView.Adapter<HealthStatusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            HealthStatusItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dateTV.text = healthStatus[position]?.month
        holder.binding.ageTV.text = healthStatus[position]?.age
        holder.binding.heightTV.text = healthStatus[position]?.cm
        holder.binding.weightTV.text = healthStatus[position]?.kg
    }

    override fun getItemCount(): Int {
        return healthStatus.size
    }

    class ViewHolder(val binding: HealthStatusItemBinding) : RecyclerView.ViewHolder(binding.root)
}