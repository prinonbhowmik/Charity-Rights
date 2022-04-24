package com.charityright.charityauthority.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.DonorListItemBinding
import com.charityright.charityauthority.model.admin.donorList.Donar
import com.squareup.picasso.Picasso

class DonorListAdapter(
    private val requiredActivity: FragmentActivity,
    private val findNavController: NavController,
    private val donarList: List<Donar?>
) : RecyclerView.Adapter<DonorListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorListAdapter.ViewHolder {
        return ViewHolder(
            DonorListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DonorListAdapter.ViewHolder, position: Int) {

        Picasso.get().load("${requiredActivity.resources.getString(R.string.base_url)}${donarList[position]?.image}").placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(holder.binding.imageView)

        holder.binding.amountTV.text = "${donarList[position]?.amount } Tk"
        holder.binding.nameTV.text = donarList[position]?.fullname
        holder.binding.areaTV.text = donarList[position]?.address

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",donarList[position]?.id)
            findNavController.navigate(R.id.action_donorListFragment_to_donorDetailsFragment,bundle)
        }

    }

    override fun getItemCount(): Int {
        return donarList.size
    }

    class ViewHolder(val binding: DonorListItemBinding) : RecyclerView.ViewHolder(binding.root)

}