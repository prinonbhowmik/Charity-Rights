package com.charityright.bd.Adapters.HomeFragmentAdapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.CampaignListModel.Data
import com.charityright.bd.R
import com.charityright.bd.databinding.HelpHorizontalItemBinding
import com.squareup.picasso.Picasso
import android.os.Build
import android.text.Html

class HelpAdapter(
        private val requireActivity: FragmentActivity,
        private val data: List<Data?>
) :
        RecyclerView.Adapter<HelpAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                HelpHorizontalItemBinding.inflate(
                        LayoutInflater.from(
                                parent.context
                        ), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get()
                .load("${requireActivity.resources.getString(R.string.base_url).dropLast(1)}${data[position]?.image_url}")
                .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).fit().centerCrop()
                .into(holder.binding.imageView)

        if (Build.VERSION.SDK_INT >= 24) {
            holder.binding.titleTV.text = Html.fromHtml(data[position]?.title, Html.FROM_HTML_MODE_LEGACY)
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details, Html.FROM_HTML_MODE_LEGACY)
        } else {
            holder.binding.titleTV.text = Html.fromHtml(data[position]?.title)
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details)
        }

        holder.binding.donationBtn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("id", data[position]?.id)

            requireActivity.findNavController(R.id.homeFragment)
                    .navigate(R.id.action_homeFragment_to_donationStepOneFragment, bundle)

        }

        holder.binding.viewBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", data[position]?.id)
            requireActivity.findNavController(R.id.homeFragment)
                    .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", data[position]?.id)
            requireActivity.findNavController(R.id.homeFragment)
                    .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: HelpHorizontalItemBinding) : RecyclerView.ViewHolder(binding.root)
}