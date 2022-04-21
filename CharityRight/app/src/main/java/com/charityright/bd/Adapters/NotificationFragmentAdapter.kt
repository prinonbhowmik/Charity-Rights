package com.charityright.bd.Adapters

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.charityright.bd.Models.NotificationModel.Data
import com.charityright.bd.R
import com.charityright.bd.databinding.NotificationsItemBinding

class NotificationFragmentAdapter(
    private val requireContext: Context,
    private val data: List<Data?>,
    private val findNavController: NavController
) : RecyclerView.Adapter<NotificationFragmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            NotificationsItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.imageView.load("${requireContext.resources.getString(R.string.base_url)}${data[position]?.image_url}") {
            crossfade(true)
            placeholder(R.drawable.ic_charity_logo)
            error(R.drawable.ic_charity_logo)
            transformations(CircleCropTransformation())
        }

        holder.binding.titleTv.text = data[position]?.title

        if (Build.VERSION.SDK_INT >= 24) {
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details ?: "", Html.FROM_HTML_MODE_LEGACY)
        } else {
            holder.binding.descriptionTV.text = Html.fromHtml(data[position]?.details ?: "")
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.id)

            if (data[position]?.event_type == "Campaign"){
                findNavController.navigate(R.id.action_noticeFragment_to_mealDetailsFragment,bundle)
            }else{
                findNavController.navigate(R.id.action_noticeFragment_to_schoolDetailsFragment,bundle)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: NotificationsItemBinding) : RecyclerView.ViewHolder(binding.root)
}