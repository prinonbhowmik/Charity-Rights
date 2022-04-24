package com.charityright.charityauthority.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.databinding.AuditorItemBinding

class AuditorDetailsAdapter: RecyclerView.Adapter<AuditorDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AuditorDetailsAdapter.ViewHolder {
        return ViewHolder(
            AuditorItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AuditorDetailsAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 8
    }

    class ViewHolder(val binding: AuditorItemBinding) : RecyclerView.ViewHolder(binding.root)
}