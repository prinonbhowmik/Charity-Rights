package com.charityright.charityauthority.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.StudentItemBinding
import com.charityright.charityauthority.model.admin.filteredStudentList.Data
import com.squareup.picasso.Picasso

class FilterStudentAdapter(
    private val findNavController: NavController,
    private val data: List<Data?>,
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<FilterStudentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            StudentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load("${requireActivity.resources.getString(R.string.base_url)}${data[position]?.image}").placeholder(R.drawable.image_placeholder).into(holder.binding.imageView)
        holder.binding.nameTV.text = data[position]?.std_name
        holder.binding.ageTV.text = data[position]?.age

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)
            findNavController.navigate(R.id.action_totalStudentFragment_to_studentDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root)
}