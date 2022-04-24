package com.charityright.charityauthority.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.ClassRecyclerItemBinding
import com.charityright.charityauthority.model.admin.classList.Class

class ClassAdapter(private val findNavController: NavController, private val clazz: List<Class?>) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ClassRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.classNameTV.text = clazz[position]?.name

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",clazz[position]?.id)

            findNavController.navigate(R.id.action_addSchoolReviewFragment_to_classDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return clazz.size
    }

    class ViewHolder(val binding: ClassRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
}