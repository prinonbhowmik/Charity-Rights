package com.charityright.bd.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.HistoryModelClass.Donar
import com.charityright.bd.databinding.HistoryItemBinding

class DonationHistoryAdapter(private val donarList: List<Donar?>) : RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DonationHistoryAdapter.ViewHolder {
        return ViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DonationHistoryAdapter.ViewHolder, position: Int) {
        holder.binding.nameTV.text = donarList[position]?.event_name
        holder.binding.DateTV.text = donarList[position]?.payment_date
        holder.binding.DonationTV.text = donarList[position]?.amount
    }

    override fun getItemCount(): Int {
        return donarList.size
    }

    class ViewHolder(val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}