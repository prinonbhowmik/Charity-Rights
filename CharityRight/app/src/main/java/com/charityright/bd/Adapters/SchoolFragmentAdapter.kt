package com.charityright.bd.Adapters

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.SchoolListBaseModel.Data
import com.charityright.bd.R
import com.charityright.bd.databinding.SchoolMealItemBinding
import com.squareup.picasso.Picasso


class SchoolFragmentAdapter(private val requireActivity: FragmentActivity, private val data: List<Data?>) : RecyclerView.Adapter<SchoolFragmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(SchoolMealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SchoolFragmentAdapter.ViewHolder, position: Int) {

        Picasso.get()
            .load("${requireActivity.resources.getString(R.string.base_url)}${data[position]?.image_url}")
            .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)
            .into(holder.binding.imageView)

        holder.binding.titleTV.text = data[position]?.school_name

        holder.binding.locationTv.text = data[position]?.zone

        if (Build.VERSION.SDK_INT >= 24) {
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details ?: "", Html.FROM_HTML_MODE_LEGACY)
        } else {
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details ?: "")
        }

        holder.binding.donationBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_allSchoolListFragment_to_donationStepOneFragment,bundle)
        }

        holder.binding.viewBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_allSchoolListFragment_to_schoolDetailsFragment,bundle)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)
            bundle.putString("name",data[position]?.school_name)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_allSchoolListFragment_to_schoolDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: SchoolMealItemBinding) : RecyclerView.ViewHolder(binding.root)
}