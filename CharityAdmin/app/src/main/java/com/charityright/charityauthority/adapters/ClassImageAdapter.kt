package com.charityright.charityauthority.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel.SchoolViewModel
import com.charityright.charityauthority.databinding.SliderImageItemBinding
import com.charityright.charityauthority.model.admin.classList.Image
import com.squareup.picasso.Picasso

class ClassImageAdapter(
    private val images: List<Image?>,
    private val viewModel: SchoolViewModel,
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<ClassImageAdapter.ViewHolder>() {

    var flag = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(SliderImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (flag == 0){
            viewModel.setImagePath(images[position]?.url.toString())
            flag++
        }

        Picasso.get().load("${requireActivity.resources.getString(R.string.base_url)}${images[position]?.url}").fit().centerCrop().placeholder(R.drawable.image_placeholder).into(holder.binding.itemImageView)

        holder.itemView.setOnClickListener {
            viewModel.setImagePath(images[position]?.url.toString())
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(val binding: SliderImageItemBinding) : RecyclerView.ViewHolder(binding.root)
}