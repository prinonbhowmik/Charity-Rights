package com.charityright.charityauthority.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.StudentItemBinding
import com.charityright.charityauthority.model.admin.classDetails.Student
import com.squareup.picasso.Picasso

class StudentAdapter(
    private val findNavController: NavController,
    private val flag: String,
    private val studentList: List<Student?>,
    private val requireActivity: FragmentActivity
) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.ViewHolder {
        return ViewHolder(
            StudentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {

        Picasso.get().load("${requireActivity.resources.getString(R.string.base_url)}${studentList[position]?.image}").placeholder(R.drawable.image_placeholder).into(holder.binding.imageView)
        holder.binding.nameTV.text = studentList[position]?.std_name
        holder.binding.ageTV.text = studentList[position]?.age

        holder.itemView.setOnClickListener {
            if (flag == "totalStudent") {
                val bundle = Bundle()
                bundle.putString("id",studentList[position]?.student_id)
                findNavController.navigate(R.id.action_totalStudentFragment_to_studentDetailsFragment,bundle)
            } else {
                val bundle = Bundle()
                bundle.putString("id",studentList[position]?.student_id)
                findNavController.navigate(R.id.action_classDetailsFragment_to_studentDetailsFragment,bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    class ViewHolder(val binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root)
}