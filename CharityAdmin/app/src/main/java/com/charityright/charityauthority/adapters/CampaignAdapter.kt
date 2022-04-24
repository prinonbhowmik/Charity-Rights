package com.charityright.charityauthority.adapters

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.CampaignItemBinding
import com.charityright.charityauthority.model.admin.campaignList.campaignData
import com.squareup.picasso.Picasso

class CampaignAdapter(
    private val requiredActivity: FragmentActivity,
    private val findNavController: NavController,
    private val flag: String,
    private val data: List<campaignData?>
) : RecyclerView.Adapter<CampaignAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignAdapter.ViewHolder {
        return ViewHolder(
            CampaignItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CampaignAdapter.ViewHolder, position: Int) {

        Picasso.get().load("${requiredActivity.resources.getString(R.string.base_url)}${data[position]?.cover_img}").placeholder(R.drawable.image_placeholder).into(holder.binding.campaignIV)

        if (Build.VERSION.SDK_INT >= 24) {
            holder.binding.titleTV.text = Html.fromHtml(data[position]?.title, Html.FROM_HTML_MODE_LEGACY)
            holder.binding.detailsTV.text = Html.fromHtml(data[position]?.details, Html.FROM_HTML_MODE_LEGACY)
        } else {
            holder.binding.titleTV.text = Html.fromHtml(data[position]?.title)
            holder.binding.detailsTV.text = Html.fromHtml(data[position]?.details)
        }

        holder.itemView.setOnClickListener {
            if (flag == "totalCampaign"){
                val bundle = Bundle()
                bundle.putString("id",data[position]?.id)
                findNavController.navigate(R.id.action_totalCampaignListFragment_to_addCampaignReviewFragment,bundle)
            }else{
                val bundle = Bundle()
                bundle.putString("id",data[position]?.id)
                findNavController.navigate(R.id.action_campaignFragment_to_addCampaignReviewFragment,bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: CampaignItemBinding) : RecyclerView.ViewHolder(binding.root)

}