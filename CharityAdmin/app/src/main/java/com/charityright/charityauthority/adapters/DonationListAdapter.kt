package com.charityright.charityauthority.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.DonorDonationItemBinding
import com.charityright.charityauthority.model.admin.donationList.Data

class DonationListAdapter(private val findNavController: NavController, private val data: List<Data?>) :RecyclerView.Adapter<DonationListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DonationListAdapter.ViewHolder {
        return ViewHolder(
            DonorDonationItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DonationListAdapter.ViewHolder, position: Int) {
        holder.binding.titleTV.text = data[position]?.donar_name
        holder.binding.dateTV.text = data[position]?.donation_date
        holder.binding.amountTV.text = "${data[position]?.donation_amount} TK"

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",data[position]?.donar_id)

            findNavController.navigate(R.id.action_totalDonationReportFragment_to_donorDetailsFragment,bundle)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: DonorDonationItemBinding) : RecyclerView.ViewHolder(binding.root)

}