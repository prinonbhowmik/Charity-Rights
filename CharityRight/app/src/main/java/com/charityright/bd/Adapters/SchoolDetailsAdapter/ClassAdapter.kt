package com.charityright.bd.Adapters.SchoolDetailsAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.SchoolDetails.Class
import com.charityright.bd.R
import com.charityright.bd.databinding.ClassRecyclerItemBinding

class ClassAdapter(
    private val requireActivity: FragmentActivity,
    private val classList: List<Class?>,
    private val id: String
) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ClassRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.classNameTV.text = classList[position]?.name

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",classList[position]?.id)
            bundle.putString("school_id",id)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_schoolDetailsFragment_to_classDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    class ViewHolder(val binding: ClassRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
}