package com.charityright.charityauthority.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.databinding.HealthStatusItemBinding
import com.charityright.charityauthority.model.admin.studentList.HealthStatus

class HealthStatusAdapter(private val healthStatus: List<HealthStatus?>) : RecyclerView.Adapter<HealthStatusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HealthStatusAdapter.ViewHolder {
        return ViewHolder(
            HealthStatusItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HealthStatusAdapter.ViewHolder, position: Int) {
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