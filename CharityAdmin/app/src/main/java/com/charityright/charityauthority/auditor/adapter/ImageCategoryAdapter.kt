package com.charityright.charityauthority.auditor.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.charityright.charityauthority.auditor.viewModel.FieldVisitViewModel
import com.charityright.charityauthority.databinding.ImageCategoryItemBinding
import java.lang.Exception

class ImageCategoryAdapter(
    private val fieldVisitViewModel: FieldVisitViewModel,
    private val requireContext: Context
):RecyclerView.Adapter<ImageCategoryAdapter.ViewHolder>() {

    var size = fieldVisitViewModel.imageCategoryName.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ImageCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.categoryTV.text = fieldVisitViewModel.imageCategoryName[position]
        println("URI 1 ${fieldVisitViewModel.imageCategoryList[position]}")

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext.contentResolver, fieldVisitViewModel.imageCategoryList[position])
            val temp = getResizedBitmap(bitmap, 120, 120)
            holder.binding.imageView.setImageBitmap(temp)
        }catch (e: Exception){
            Log.wtf("FiledVisitFormAdapter", "onImageLoading: ",e)
        }

        holder.binding.deleteBtn.setOnClickListener {
            fieldVisitViewModel.imageCategoryName.removeAt(position)
            fieldVisitViewModel.imageCategoryList.removeAt(position)
            getNewCount(fieldVisitViewModel.imageCategoryName.size)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return size
    }

    fun getNewCount(temp: Int) {
        size = temp
        notifyDataSetChanged()
    }

    fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)
        // RECREATE THE NEW BITMAP
        return Bitmap.createBitmap(
            bm, 0, 0, width, height,
            matrix, false
        )
    }

    class ViewHolder(val binding: ImageCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}