package com.charityright.charityauthority.auditor.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.R
import com.charityright.charityauthority.auditor.viewModel.BeneficiaryViewModel
import com.charityright.charityauthority.databinding.FragmentBeneficiaryBinding
import com.charityright.charityauthority.model.admin.addSchool.addImageList
import com.charityright.charityauthority.util.CustomDialog
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import java.io.File
import android.content.ClipData

import android.os.Build
import android.os.Environment
import java.lang.Exception


class BeneficiaryFragment : Fragment() {

    private var _binding: FragmentBeneficiaryBinding? = null
    private val binding get() = _binding!!

    private var fileArray = ArrayList<File>()
    private var imageArrayList: ArrayList<addImageList> = ArrayList()

    private lateinit var beneficiaryViewModel: BeneficiaryViewModel

    private var itemCount = 0

    private lateinit var cacheFile1: File
    private lateinit var cacheFileUri1: Uri

    private lateinit var cacheFile2: File
    private lateinit var cacheFileUri2: Uri

    private lateinit var cacheFile3: File
    private lateinit var cacheFileUri3: Uri

    private lateinit var cacheFile4: File
    private lateinit var cacheFileUri4: Uri

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(requireContext(),"Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission request was denied.
                Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeneficiaryBinding.inflate(inflater,container,false)

        beneficiaryViewModel = ViewModelProvider(this).get(BeneficiaryViewModel::class.java)
        CustomDialog.init(requireContext())

        beneficiaryViewModel.addImageResponse.observe(viewLifecycleOwner,{

            if (it?.response_status == "200"){
                itemCount++
                beneficiaryViewModel.imageList.add(it.data.toString())
            }

            if (itemCount != fileArray.size){
                uploadImages(itemCount)
            }else{
                uploadAllInfo()
            }

        })

        beneficiaryViewModel.addBeneficiaryResponse.observe(viewLifecycleOwner,{
            if (it?.response_status == "200"){
                CustomDialog.dismiss()
                Toast.makeText(requireContext(),""+it.message,Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_beneficiaryFragment_to_auditorHomeFragment)
            }else{
                CustomDialog.dismiss()
                Toast.makeText(requireContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show()
            }
        })

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.card1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cacheFile1 = File(requireActivity().externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
                cacheFileUri1 = FileProvider.getUriForFile(requireContext(),"com.charityright.charityauthority.provider", cacheFile1)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheFileUri1)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 1)
            } else {
                requestPermission()
            }
        }

        binding.card2.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cacheFile2 = File(requireActivity().externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
                cacheFileUri2 = FileProvider.getUriForFile(requireContext(),"com.charityright.charityauthority.provider", cacheFile2)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheFileUri2)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 2)
            } else {
                requestPermission()
            }
        }

        binding.card3.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cacheFile3 = File(requireActivity().externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
                cacheFileUri3 = FileProvider.getUriForFile(requireContext(),"com.charityright.charityauthority.provider", cacheFile3)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheFileUri3)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 3)
            } else {
                requestPermission()
            }
        }

        binding.card4.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cacheFile4 = File(requireActivity().externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
                cacheFileUri4 = FileProvider.getUriForFile(requireContext(),"com.charityright.charityauthority.provider", cacheFile4)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheFileUri4)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 4)
            } else {
                requestPermission()
            }
        }

        binding.submitBtn.setOnClickListener {

            if (binding.titleET.text.toString() == ""){
                binding.titleET.requestFocus()
                binding.titleET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.nameET.text.toString() == ""){
                binding.nameET.requestFocus()
                binding.nameET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.ageET.text.toString() == ""){
                binding.ageET.requestFocus()
                binding.ageET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.locationET.text.toString() == ""){
                binding.locationET.requestFocus()
                binding.locationET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.additionalET.text.toString() == ""){
                binding.additionalET.requestFocus()
                binding.additionalET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.remarksET.text.toString() == ""){
                binding.remarksET.requestFocus()
                binding.remarksET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            beneficiaryViewModel.title = binding.titleET.text.toString()
            beneficiaryViewModel.name = binding.nameET.text.toString()
            beneficiaryViewModel.age = binding.ageET.text.toString()
            beneficiaryViewModel.location = binding.locationET.text.toString()
            beneficiaryViewModel.additional = binding.additionalET.text.toString()
            beneficiaryViewModel.remarks = binding.remarksET.text.toString()

            CustomDialog.show()
            uploadImages(itemCount)

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, cacheFileUri1)
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView1.setImageBitmap(temp)
                        fileArray.add(cacheFile1)

                    }catch (e: java.lang.Exception){
                        Log.wtf("AddBeneficiaryFragment", "onActivityResult: ",e)
                    }
                }
                2 -> {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, cacheFileUri2)
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView2.setImageBitmap(temp)
                        fileArray.add(cacheFile2)

                    }catch (e: java.lang.Exception){
                        Log.wtf("AddBeneficiaryFragment", "onActivityResult: ",e)
                    }
                }
                3 -> {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, cacheFileUri3)
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView3.setImageBitmap(temp)
                        fileArray.add(cacheFile3)

                    }catch (e: java.lang.Exception){
                        Log.wtf("AddBeneficiaryFragment", "onActivityResult: ",e)
                    }
                }
                4 -> {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, cacheFileUri3)
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView4.setImageBitmap(temp)
                        fileArray.add(cacheFile4)

                    }catch (e: java.lang.Exception){
                        Log.wtf("AddBeneficiaryFragment", "onActivityResult: ",e)
                    }
                }
            }
        }
    }

    private fun uploadAllInfo() {
        for (i in fileArray.indices){
            when(i){
                0-> {
                    cacheFile1.delete()
                }
                1-> {
                    cacheFile2.delete()
                }
                2-> {
                    cacheFile3.delete()
                }
                3-> {
                    cacheFile4.delete()
                }
            }
        }

        if (beneficiaryViewModel.imageList.isNotEmpty()) {
            beneficiaryViewModel.images = getImageJsonObject().toString()
        }else{
            beneficiaryViewModel.images = ""
        }

        lifecycleScope.launch {
            beneficiaryViewModel.launchBeneficiaryApiCall()
        }
    }

    private fun uploadImages(index: Int) {
        lifecycleScope.launch {
            val compressedImageFile = Compressor.compress(requireContext(), fileArray[index]){
                resolution(512,512)
                quality(50)
            }
            beneficiaryViewModel.launchImageUploadApiCall(compressedImageFile)
        }
    }

    private fun requestPermission() {
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
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

    fun getImageJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        imageArrayList.clear()

        for (i in beneficiaryViewModel.imageList.indices){
            val tempImageList = addImageList(beneficiaryViewModel.imageList[i])
            imageArrayList.add(tempImageList)
        }

        return gson.toJsonTree(imageArrayList).asJsonArray
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}