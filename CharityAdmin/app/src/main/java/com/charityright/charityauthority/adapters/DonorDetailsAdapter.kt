package com.charityright.charityauthority.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.databinding.DonorDonationItemBinding
import com.charityright.charityauthority.model.admin.donorDetails.Donation

class DonorDetailsAdapter(private val data: List<Donation?>) : RecyclerView.Adapter<DonorDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DonorDonationItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.titleTV.text = data[position]?.event_name
        holder.binding.dateTV.text = data[position]?.payment_date
        holder.binding.amountTV.text = "${data[position]?.amount} TK"

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: DonorDonationItemBinding) : RecyclerView.ViewHolder(binding.root)
}