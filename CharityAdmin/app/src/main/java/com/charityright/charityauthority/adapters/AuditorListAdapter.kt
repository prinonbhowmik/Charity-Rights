package com.charityright.charityauthority.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.AuditorItemBinding
import com.charityright.charityauthority.model.admin.auditorReportList.Data

class AuditorListAdapter(
    private val findNavController: NavController,
    private val requireActivity: FragmentActivity,
    private val list: List<Data?>
) : RecyclerView.Adapter<AuditorListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AuditorListAdapter.ViewHolder {
        return ViewHolder(
            AuditorItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AuditorListAdapter.ViewHolder, position: Int) {

        holder.binding.nameTV.text = list[position]?.auditor_name

        val site_report: Int = list[position]?.site_visit_report?.toInt() ?: 0
        val circum_report: Int = list[position]?.special_circumstance_report?.toInt() ?: 0
        val benef_report: Int = list[position]?.beneficiary_remarks_report?.toInt() ?: 0

        val total = site_report + circum_report + benef_report

        holder.binding.submitReportTV.text = total.toString()

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", list[position]?.auditor_id)
            findNavController.navigate(
                R.id.action_auditorListFragment_to_auditorReportListFragment,
                bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: AuditorItemBinding) : RecyclerView.ViewHolder(binding.root)

}