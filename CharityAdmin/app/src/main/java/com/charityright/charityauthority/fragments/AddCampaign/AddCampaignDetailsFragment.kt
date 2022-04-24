package com.charityright.charityauthority.fragments.AddCampaign

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentAddCampaignDetailsBinding
import com.charityright.charityauthority.model.admin.addSchool.addImageList
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.addCampaignViewModel.addCampaignViewModel
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class AddCampaignDetailsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddCampaignDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBar: LinearLayout

    private lateinit var campaignViewModel: addCampaignViewModel
    private lateinit var activityViewModel: adminHomeActivityViewModel

    private var day = 0
    private var month = 0
    private var year = 0

    private lateinit var file1: File
    private lateinit var file2: File
    private lateinit var file3: File
    private lateinit var file4: File
    private lateinit var file5: File

    private var itemCount = 0

    private var fileArray = ArrayList<File>()
    private  var zoneIdList = ArrayList<String>()
    private  var zoneNameList = ArrayList<String>()
    private var flag: Boolean = false
    private var spinnerPosition = 0

    private var imageArrayList: ArrayList<addImageList> = ArrayList()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission request was denied.
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCampaignDetailsBinding.inflate(inflater, container, false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        activityViewModel = ViewModelProvider(requireActivity()).get(adminHomeActivityViewModel::class.java)
        campaignViewModel = ViewModelProvider(this).get(addCampaignViewModel::class.java)
        CustomDialog.init(requireContext())

        activityViewModel.addZoneResponse.observe(viewLifecycleOwner,{
            if (it?.data?.isNotEmpty() == true){

                if (!flag){
                    for (i in it.data.indices) {
                        zoneIdList.add(it.data[i]?.id.toString())
                        zoneNameList.add(it.data[i]?.name.toString())
                    }
                    flag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.textItem, zoneNameList)
                binding.spinner.adapter = spinnerArrayAdapter
            }
        })

        campaignViewModel.addImageResponse.observe(viewLifecycleOwner, {
            if (it?.response_status == "200") {
                itemCount++
                campaignViewModel.imageList.add(it.data.toString())

                if (itemCount != fileArray.size) {
                    uploadImages(itemCount)
                }else{

                    campaignViewModel.images = getImageJsonObject().toString()

                    lifecycleScope.launch {
                        campaignViewModel.launchAddCampaignApiCall()
                    }
                }
            }
        })

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lifecycleScope.launch {
                    spinnerPosition = position
                }
            }
        }

        campaignViewModel.addCampaignResponse.observe(viewLifecycleOwner, {
            if (it?.response_status == "200") {
                CustomDialog.dismiss()
                Toast.makeText(requireContext(), "Campaign Added Successfully", Toast.LENGTH_SHORT)
                    .show()
                requireActivity().onBackPressed()
            } else {
                CustomDialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })

        binding.campaignDateET.setOnClickListener {
            pickDate()
        }

        binding.card1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            } else {
                requestPermission()
            }
        }

        binding.card2.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 2)
            } else {
                requestPermission()
            }
        }

        binding.card3.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 3)
            } else {
                requestPermission()
            }
        }

        binding.card4.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 4)
            } else {
                requestPermission()
            }
        }

        binding.card5.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 5)
            } else {
                requestPermission()
            }
        }

        binding.submitBtn.setOnClickListener {
            val zone = zoneIdList[spinnerPosition]
            val name = binding.campaignNameET.text.toString()
            val date = binding.campaignDateET.text.toString()
            val location = binding.locationET.text.toString()
            val description = binding.descriptionET.text.toString()
            val objective = binding.objectiveET.text.toString()
            val moderator = binding.moderatorET.text.toString()
            val contact = binding.contactET.text.toString()

            if (name.isEmpty()){
                binding.campaignNameET.requestFocus()
                binding.campaignNameET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (date.isEmpty()){
                binding.campaignDateET.requestFocus()
                binding.campaignDateET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (location.isEmpty()){
                binding.locationET.requestFocus()
                binding.locationET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (description.isEmpty()){
                binding.descriptionET.requestFocus()
                binding.descriptionET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (objective.isEmpty()){
                binding.objectiveET.requestFocus()
                binding.objectiveET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (moderator.isEmpty()){
                binding.moderatorET.requestFocus()
                binding.moderatorET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (contact.isEmpty()){
                binding.contactET.requestFocus()
                binding.contactET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (fileArray.isEmpty()){
                Toast.makeText(requireContext(),"Please Select Image",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            campaignViewModel.zone_id = zone
            campaignViewModel.title = name
            campaignViewModel.date = date
            campaignViewModel.location = location
            campaignViewModel.details = description
            campaignViewModel.objective = objective
            campaignViewModel.beneficiary = "null"
            campaignViewModel.m_name = moderator
            campaignViewModel.m_contact = contact

            lifecycleScope.launch {

                CustomDialog.show()
                uploadImages(itemCount)
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun uploadImages(index: Int) {
        lifecycleScope.launch {
            val compressedImageFile = Compressor.compress(requireContext(), fileArray[index]){
                resolution(512,512)
                quality(50)
            }
            campaignViewModel.launchAddImageApiCall(compressedImageFile)
        }
    }

    fun getImageJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        imageArrayList.clear()

        for (i in campaignViewModel.imageList.indices) {
            println(campaignViewModel.imageList[i])
            val tempImageList = addImageList(campaignViewModel.imageList[i])
            imageArrayList.add(tempImageList)
        }

        return gson.toJsonTree(imageArrayList).asJsonArray
    }

    private fun pickDate() {
        getDateTimeCalender()
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun getDateTimeCalender() {
        val cal: Calendar = Calendar.getInstance()

        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding.campaignDateET.text = "$year-${month + 1}-$day"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    try {
                        val selectedImageUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView1.setImageBitmap(temp)
                        file1 = File(getRealPathFromURI(requireContext(), selectedImageUri)!!)
                        fileArray.add(file1)

                    } catch (e: java.lang.Exception) {
                        Log.wtf("AddSchoolDetailsFragment", "onActivityResult: ", e)
                    }
                }
                2 -> {
                    try {
                        val selectedImageUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView2.setImageBitmap(temp)
                        file2 = File(getRealPathFromURI(requireContext(), selectedImageUri)!!)
                        fileArray.add(file2)

                    } catch (e: java.lang.Exception) {
                        Log.wtf("AddSchoolDetailsFragment", "onActivityResult: ", e)
                    }
                }
                3 -> {
                    try {
                        val selectedImageUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView3.setImageBitmap(temp)
                        file3 = File(getRealPathFromURI(requireContext(), selectedImageUri)!!)
                        fileArray.add(file3)

                    } catch (e: java.lang.Exception) {
                        Log.wtf("AddSchoolDetailsFragment", "onActivityResult: ", e)
                    }
                }
                4 -> {
                    try {
                        val selectedImageUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView4.setImageBitmap(temp)
                        file4 = File(getRealPathFromURI(requireContext(), selectedImageUri)!!)
                        fileArray.add(file4)

                    } catch (e: java.lang.Exception) {
                        Log.wtf("AddSchoolDetailsFragment", "onActivityResult: ", e)
                    }
                }

                5 -> {
                    try {
                        val selectedImageUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView5.setImageBitmap(temp)
                        file5 = File(getRealPathFromURI(requireContext(), selectedImageUri)!!)
                        fileArray.add(file5)

                    } catch (e: java.lang.Exception) {
                        Log.wtf("AddSchoolDetailsFragment", "onActivityResult: ", e)
                    }
                }
            }
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

    private fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(column_index!!)
        } finally {
            cursor?.close()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        appBar.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        appBar.visibility = View.GONE
    }
}