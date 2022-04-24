package com.charityright.charityauthority.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.databinding.NotificationsItemBinding
import com.charityright.charityauthority.model.notificationModel.Data

class NotificationAdapter(
    private val data: List<Data?>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
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

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {

        holder.binding.titleTv.text = data[position]?.title
        holder.binding.donorNameTV.text = data[position]?.donar_name
        holder.binding.dateTV.text = data[position]?.donation_date
        holder.binding.amountTV.text = "Amount : ${data[position]?.donation_amount}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: NotificationsItemBinding) : RecyclerView.ViewHolder(binding.root)

}