package com.charityright.charityauthority.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.databinding.FieldVisitReportItemBinding
import com.charityright.charityauthority.model.schoolReport.Data

class FieldVisitReportAdapter(private val data: List<Data?>) : RecyclerView.Adapter<FieldVisitReportAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FieldVisitReportAdapter.ViewHolder {
        return ViewHolder(
            FieldVisitReportItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FieldVisitReportAdapter.ViewHolder, position: Int) {
        holder.binding.dateTV.text = data[position]?.date
        holder.binding.auditorTV.text = data[position]?.name
        holder.binding.presentStuTV.text = data[position]?.present_std
        holder.binding.absentStuTV.text = data[position]?.absent_std
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: FieldVisitReportItemBinding) : RecyclerView.ViewHolder(binding.root)
}