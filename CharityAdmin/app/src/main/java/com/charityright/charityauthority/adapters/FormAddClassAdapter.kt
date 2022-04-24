package com.charityright.charityauthority.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.auditor.viewModel.FieldVisitViewModel
import com.charityright.charityauthority.databinding.FieldClassItemBinding

class FormAddClassAdapter(
    private val fieldVisitViewModel: FieldVisitViewModel,
    private val presentET: EditText,
    private val absentET: EditText
) : RecyclerView.Adapter<FormAddClassAdapter.ViewHolder>() {

    var size = fieldVisitViewModel.className.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FormAddClassAdapter.ViewHolder {
        return ViewHolder(
            FieldClassItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FormAddClassAdapter.ViewHolder, position: Int) {

        holder.binding.classTitleTV.text = fieldVisitViewModel.className[position]
        holder.binding.classTotalStudTV.text = fieldVisitViewModel.totalStudent[position]
        holder.binding.classTotalPreTV.text = fieldVisitViewModel.totalPresent[position]
        holder.binding.classTotalAbsTV.text = fieldVisitViewModel.totalAbsent[position]
        holder.binding.avgAgeTV.text = fieldVisitViewModel.avgAge[position]

        holder.binding.deleteBtn.setOnClickListener {
            fieldVisitViewModel.className.removeAt(position)
            fieldVisitViewModel.totalStudent.removeAt(position)
            fieldVisitViewModel.totalPresent.removeAt(position)
            fieldVisitViewModel.totalAbsent.removeAt(position)
            fieldVisitViewModel.avgAge.removeAt(position)
            presentET.setText(fieldVisitViewModel.getTotalPresent().toString())
            absentET.setText(fieldVisitViewModel.getTotalAbsent().toString())
            getNewCount(fieldVisitViewModel.className.size)
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

    class ViewHolder(val binding: FieldClassItemBinding) : RecyclerView.ViewHolder(binding.root)
}