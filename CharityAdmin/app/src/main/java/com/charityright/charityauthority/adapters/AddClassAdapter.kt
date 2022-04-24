package com.charityright.charityauthority.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.databinding.ClassItemBinding
import com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel.AddSchoolFragmentViewModel

class AddClassAdapter(
    private val addSchoolFragmentViewModel: AddSchoolFragmentViewModel
) : RecyclerView.Adapter<AddClassAdapter.ViewHolder>() {

    var size = addSchoolFragmentViewModel.classList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ClassItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.classET.text = addSchoolFragmentViewModel.classList[position]

        holder.binding.deleteBtn.setOnClickListener {
            addSchoolFragmentViewModel.classList.removeAt(position)
            getNewCount(addSchoolFragmentViewModel.classList.size)
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

    class ViewHolder(val binding: ClassItemBinding) : RecyclerView.ViewHolder(binding.root)
}