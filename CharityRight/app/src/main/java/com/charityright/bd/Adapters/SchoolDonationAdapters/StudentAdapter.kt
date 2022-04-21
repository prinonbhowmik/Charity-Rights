package com.charityright.bd.Adapters.SchoolDonationAdapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.ClassDetailsModel.Student
import com.charityright.bd.R
import com.charityright.bd.databinding.StudentItemBinding
import com.squareup.picasso.Picasso

class StudentAdapter(
    private val requireActivity: FragmentActivity,
    private val data: List<Student?>,
    private val school_id: String
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.ViewHolder {
        return ViewHolder(StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {

        Picasso.get().load("${requireActivity.resources.getString(R.string.base_url)}${data[position]?.image}").placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(holder.binding.imageView)
        holder.binding.nameTV.text = data[position]?.std_name
        holder.binding.ageTV.text = data[position]?.age

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.student_id)
            bundle.putString("school_id",school_id)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_classDetailsFragment_to_studentDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root)
}