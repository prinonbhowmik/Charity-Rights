package com.charityright.charityauditor.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.auditor.model.AssignedReport.Data
import com.charityright.charityauthority.databinding.FieldVisitItemBinding


class AssignReportAdapter(private val findNavController: NavController,private val data: List<Data?>) : RecyclerView.Adapter<AssignReportAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            FieldVisitItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AssignReportAdapter.ViewHolder, position: Int) {

        holder.binding.areaTV.text = data[position]?.zone_name
        holder.binding.nameTV.text = data[position]?.school_name
        holder.binding.dateTV.text = data[position]?.assign_date

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.school_id)
            bundle.putString("lat",data[position]?.latitude)
            bundle.putString("lon",data[position]?.longitude)
            bundle.putString("task",data[position]?.id)
            findNavController.navigate(R.id.action_assignReportFragment_to_locationConfirmFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: FieldVisitItemBinding) : RecyclerView.ViewHolder(binding.root)
}