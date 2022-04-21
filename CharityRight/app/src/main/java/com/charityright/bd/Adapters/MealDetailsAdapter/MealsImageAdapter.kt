package com.charityright.bd.Adapters.MealDetailsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.charityright.bd.Models.MealsDetailsModel.Image
import com.charityright.bd.R
import com.charityright.bd.ViewModel.MealDetailsViewModel
import com.charityright.bd.databinding.SliderImageItemBinding
import com.squareup.picasso.Picasso

class MealsImageAdapter(
    private val imageList: List<Image?>,
    private val schoolDetailsViewModel: MealDetailsViewModel,
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<MealsImageAdapter.ViewHolder>() {

    var flag = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealsImageAdapter.ViewHolder {
        return ViewHolder(
            SliderImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealsImageAdapter.ViewHolder, position: Int) {
        if (flag == 0){
            schoolDetailsViewModel.setImagePath(imageList[position]?.url)
            flag++
        }

        Picasso.get().load("${requireActivity.resources.getString(R.string.base_url)}${imageList[position]?.url}").placeholder(
            R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(holder.binding.itemImageView)

        holder.itemView.setOnClickListener {
            schoolDetailsViewModel.setImagePath(imageList[position]?.url)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(val binding: SliderImageItemBinding) : RecyclerView.ViewHolder(binding.root)
}