package com.charityright.charityauthority.fragments.AddSchool

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.adapters.AddClassAdapter
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentAddSchoolDetailsBinding
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel
import kotlinx.coroutines.launch
import java.io.File
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.fragments.LocationPickerFragment
import com.charityright.charityauthority.model.admin.addSchool.addClassName
import com.charityright.charityauthority.model.admin.addSchool.addImageList
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel.AddSchoolFragmentViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution


class AddSchoolDetailsFragment : Fragment() {

    private var _binding: FragmentAddSchoolDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBar: LinearLayout

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var addClassAdapter: AddClassAdapter

    private var fileArray = ArrayList<File>()
    private var classArrayList: ArrayList<addClassName> = ArrayList()
    private var imageArrayList: ArrayList<addImageList> = ArrayList()

    private lateinit var activityViewModel: adminHomeActivityViewModel
    private lateinit var addSchoolFragmentViewModel: AddSchoolFragmentViewModel

    private var zoneIdList = ArrayList<String>()
    private var zoneNameList = ArrayList<String>()
    private var flag: Boolean = false
    private var zoneId: String = ""
    private lateinit var file1: File
    private lateinit var file2: File
    private lateinit var file3: File
    private lateinit var file4: File
    private lateinit var file5: File

    private var itemCount = 0

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
        _binding = FragmentAddSchoolDetailsBinding.inflate(inflater, container, false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        activityViewModel =
            ViewModelProvider(requireActivity()).get(adminHomeActivityViewModel::class.java)
        addSchoolFragmentViewModel =
            ViewModelProvider(this).get(AddSchoolFragmentViewModel::class.java)
        CustomDialog.init(requireContext())

        activityViewModel.addZoneResponse.observe(viewLifecycleOwner, {
            if (it?.data?.isNotEmpty() == true) {

                if (!flag) {
                    for (i in it.data.indices) {
                        zoneIdList.add(it.data[i]?.id.toString())
                        zoneNameList.add(it.data[i]?.name.toString())
                    }
                    flag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    R.id.textItem,
                    zoneNameList
                )
                binding.spinner.adapter = spinnerArrayAdapter
            }
        })

        activityViewModel.addressResponse.observe(viewLifecycleOwner, {

            if (it != "" && it != null) {
                addSchoolFragmentViewModel.lat = activityViewModel.lat
                addSchoolFragmentViewModel.lon = activityViewModel.lon
                binding.addressET.text = it.toString()
            }

        })

        addSchoolFragmentViewModel.addSchoolResponse.observe(viewLifecycleOwner, {
            if (it?.response_status == "200") {
                CustomDialog.dismiss()
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            } else {
                CustomDialog.dismiss()
                Toast.makeText(requireContext(), "" + it?.message, Toast.LENGTH_SHORT).show()
            }
        })

        addSchoolFragmentViewModel.addImageResponse.observe(viewLifecycleOwner, {
            if (it?.response_status == "200") {
                itemCount++
                addSchoolFragmentViewModel.imageList.add(it.data.toString())

                if (itemCount != fileArray.size) {
                    uploadImages(itemCount)
                }else{
                    val name = binding.schoolNameET.text.toString()
                    val zone_id = zoneIdList[binding.spinner.selectedItemPosition]
                    val address = binding.addressET.text.toString()
                    val description = binding.descriptionET.text.toString()

                    addSchoolFragmentViewModel.school_name = name
                    addSchoolFragmentViewModel.zone_id = zone_id
                    addSchoolFragmentViewModel.address = address
                    addSchoolFragmentViewModel.details = description
                    addSchoolFragmentViewModel.class_list = getClassJsonObject().toString()
                    addSchoolFragmentViewModel.images = getImageJsonObject().toString()

                    lifecycleScope.launch {
                        addSchoolFragmentViewModel.launchAddSchoolApiCall()
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
                    zoneId = zoneIdList[position]

                }
            }
        }

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        addClassAdapter = AddClassAdapter(addSchoolFragmentViewModel)
        binding.classRecyclerView.layoutManager = layoutManager
        binding.classRecyclerView.adapter = addClassAdapter

        binding.addNewBtn.setOnClickListener {
            if (binding.classET.text.toString() != "") {
                addSchoolFragmentViewModel.classList.add(binding.classET.text.toString())
                addClassAdapter.getNewCount(addSchoolFragmentViewModel.classList.size)
                binding.classET.text.clear()
            } else {
                Toast.makeText(requireContext(), "Class Can't Be Empty", Toast.LENGTH_SHORT).show()
            }
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

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.addressET.setOnClickListener {
            val locationPickerFragment = LocationPickerFragment()
            locationPickerFragment.show(
                requireActivity().supportFragmentManager,
                locationPickerFragment.tag
            )
        }

        binding.submitBtn.setOnClickListener {

            if (binding.schoolNameET.text.isEmpty()) {
                binding.schoolNameET.requestFocus()
                binding.schoolNameET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.addressET.text.isEmpty()) {
                binding.addressET.requestFocus()
                binding.addressET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.descriptionET.text.isEmpty()) {
                binding.descriptionET.requestFocus()
                binding.descriptionET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (fileArray.isEmpty()){
                Toast.makeText(requireContext(),"Please Select Image",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CustomDialog.show()
            uploadImages(itemCount)
        }

        return binding.root
    }

    private fun uploadImages(index: Int) {
        lifecycleScope.launch {
            val compressedImageFile = Compressor.compress(requireContext(), fileArray[index]){
                resolution(512,512)
                quality(50)
            }
            addSchoolFragmentViewModel.launchAddImageApiCall(compressedImageFile)
        }
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
                        file4 = File(getRealPathFromURI(requireContext(), selectedImageUri)!!)
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

    fun getClassJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        classArrayList.clear()

        for (i in addSchoolFragmentViewModel.classList.indices) {
            val tempAddClassName = addClassName(addSchoolFragmentViewModel.classList[i])
            classArrayList.add(tempAddClassName)
        }

        return gson.toJsonTree(classArrayList).asJsonArray
    }

    fun getImageJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        imageArrayList.clear()

        for (i in addSchoolFragmentViewModel.imageList.indices) {
            println(addSchoolFragmentViewModel.imageList[i])
            val tempImageList = addImageList(addSchoolFragmentViewModel.imageList[i])
            imageArrayList.add(tempImageList)
        }

        return gson.toJsonTree(imageArrayList).asJsonArray
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