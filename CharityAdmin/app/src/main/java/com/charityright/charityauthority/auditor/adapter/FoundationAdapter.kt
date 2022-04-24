package com.charityright.charityauditor.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.auditor.viewModel.FieldVisitViewModel
import com.charityright.charityauthority.databinding.FoundationItemBinding

class FoundationAdapter(private val fieldVisitViewModel: FieldVisitViewModel) : RecyclerView.Adapter<FoundationAdapter.ViewHolder>() {

    var size = fieldVisitViewModel.foundationTeacherList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoundationAdapter.ViewHolder {
        return ViewHolder(
            FoundationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FoundationAdapter.ViewHolder, position: Int) {

        holder.binding.foundationTV.text = fieldVisitViewModel.foundationTitleList[position]
        holder.binding.teacherTV.text = fieldVisitViewModel.foundationTeacherList[position]
        holder.binding.suggestImprovementTV.text = fieldVisitViewModel.foundationImprovementList[position]

        holder.binding.deleteBtn.setOnClickListener {
            fieldVisitViewModel.foundationTeacherList.removeAt(position)
            fieldVisitViewModel.foundationImprovementList.removeAt(position)
            fieldVisitViewModel.foundationTitleList.removeAt(position)
            getNewCount(fieldVisitViewModel.foundationTeacherList.size)
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

    class ViewHolder(val binding: FoundationItemBinding) : RecyclerView.ViewHolder(binding.root)
}