package com.charityright.bd.Adapters.HomeFragmentAdapters

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.CampaignListModel.Data
import com.charityright.bd.R
import com.charityright.bd.databinding.HelpItemBinding
import com.squareup.picasso.Picasso

class MealFragmentAdapter(private val requireActivity: FragmentActivity, private val data: List<Data?>) : RecyclerView.Adapter<MealFragmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            HelpItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealFragmentAdapter.ViewHolder, position: Int) {

        Picasso.get()
            .load("${requireActivity.resources.getString(R.string.base_url)}${data[position]?.image_url}")
            .fit()
            .centerCrop()
            .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)
            .into(holder.binding.imageView)

        holder.binding.titleTV.text = data[position]?.title

        if (Build.VERSION.SDK_INT >= 24) {
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details ?: "", Html.FROM_HTML_MODE_LEGACY)
        } else {
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details ?: "")
        }

        holder.binding.donationBtn.setOnClickListener {

        }

        holder.binding.donationBtn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_allSchoolMealFragment_to_donationStepOneFragment,bundle)

        }

        holder.binding.viewBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_allSchoolMealFragment_to_mealDetailsFragment,bundle)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)
            requireActivity.findNavController(R.id.homeFragment).navigate(R.id.action_allSchoolMealFragment_to_mealDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: HelpItemBinding) : RecyclerView.ViewHolder(binding.root)
}