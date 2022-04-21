package com.charityright.bd.Adapters.SchoolDetailsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.SchoolDetails.Image
import com.charityright.bd.R
import com.charityright.bd.ViewModel.SchoolDetailsViewModel
import com.charityright.bd.databinding.SliderImageItemBinding
import com.squareup.picasso.Picasso

class ImageAdapter(
    private val images: List<Image?>,
    private val viewModel: SchoolDetailsViewModel,
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    var flag = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SliderImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (flag == 0){
            viewModel.setImagePath(images[position]?.url)
            flag++
        }

        Picasso.get().load("${requireActivity.resources.getString(R.string.base_url)}${images[position]?.url}").placeholder(R.drawable.image_placeholder).into(holder.binding.itemImageView)

        holder.itemView.setOnClickListener {
            viewModel.setImagePath(images[position]?.url)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(val binding: SliderImageItemBinding) : RecyclerView.ViewHolder(binding.root)
}