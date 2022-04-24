package com.charityright.charityauthority.adapters

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.SchoolItemBinding
import com.charityright.charityauthority.model.admin.schoolList.Data
import com.squareup.picasso.Picasso

class SchoolAdapter(
    private val requiredActivity: FragmentActivity,
    private val findNavController: NavController,
    private val flag: String,
    private val data: List<Data?>
) : RecyclerView.Adapter<SchoolAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolAdapter.ViewHolder {
        return ViewHolder(
            SchoolItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SchoolAdapter.ViewHolder, position: Int) {

        Picasso.get().load("${requiredActivity.resources.getString(R.string.base_url)}${data[position]?.image_url}").placeholder(R.drawable.image_placeholder).into(holder.binding.imageView)


        if (Build.VERSION.SDK_INT >= 24) {
            holder.binding.titleTV.text = Html.fromHtml(data[position]?.school_name, Html.FROM_HTML_MODE_LEGACY)
            holder.binding.addressTV.text = Html.fromHtml(data[position]?.zone_id, Html.FROM_HTML_MODE_LEGACY)
            holder.binding.detailsTV.text = Html.fromHtml(data[position]?.details ?: "", Html.FROM_HTML_MODE_LEGACY)
        } else {
            holder.binding.titleTV.text = Html.fromHtml(data[position]?.school_name)
            holder.binding.addressTV.text = Html.fromHtml(data[position]?.zone_id)
            holder.binding.detailsTV.text = Html.fromHtml(data[position]?.details ?: "")
        }

        holder.itemView.setOnClickListener {
            if (flag == "totalSchool"){
                val bundle = Bundle()
                bundle.putString("id",data[position]?.id)
                findNavController.navigate(R.id.action_totalSchoolListFragment_to_addSchoolReviewFragment,bundle)
            }else{
                val bundle = Bundle()
                bundle.putString("id",data[position]?.id)
                findNavController.navigate(R.id.action_schoolFragment_to_addSchoolReviewFragment,bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: SchoolItemBinding) : RecyclerView.ViewHolder(binding.root)
}