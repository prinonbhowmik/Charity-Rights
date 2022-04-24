package com.charityright.charityauthority.auditor.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauditor.Adapters.FoundationAdapter
import com.charityright.charityauditor.Adapters.NutritionAdapter
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.FormAddClassAdapter
import com.charityright.charityauthority.auditor.adapter.FoodAdapter
import com.charityright.charityauthority.auditor.adapter.ImageCategoryAdapter
import com.charityright.charityauthority.auditor.model.ComplaintsModel
import com.charityright.charityauthority.auditor.model.FieldImageModel
import com.charityright.charityauthority.auditor.model.FormClassModel
import com.charityright.charityauthority.auditor.model.NutritionModel
import com.charityright.charityauthority.auditor.viewModel.AuditorActivityViewModel
import com.charityright.charityauthority.auditor.viewModel.FieldVisitViewModel
import com.charityright.charityauthority.databinding.FragmentVisitFromBinding
import com.charityright.charityauthority.model.FoodlistItem
import com.charityright.charityauthority.util.CustomUploadDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VisitFormFragment : Fragment() {

    private var _binding: FragmentVisitFromBinding? = null
    private val binding get() = _binding!!

    private lateinit var nutritionLayoutManager: LinearLayoutManager
    private lateinit var nutritionAdapter: NutritionAdapter
    private lateinit var foodAdapter: FoodAdapter

    private lateinit var foundationLayoutManager: LinearLayoutManager
    private lateinit var foundationAdapter: FoundationAdapter

    private lateinit var imageCategoryManager: LinearLayoutManager
    private lateinit var imageCategoryAdapter: ImageCategoryAdapter

    private lateinit var addClassLayoutManager: LinearLayoutManager
    private lateinit var formAddClassAdapter: FormAddClassAdapter

    private lateinit var activityViewModel: AuditorActivityViewModel
    private lateinit var fieldVisitViewModel: FieldVisitViewModel

    private var type_id = ""
    private var school_id = ""

    private var schoolIdList = ArrayList<String>()
    private var schoolNameList = ArrayList<String>()
    private var schoolZoneIdList = ArrayList<String>()
    private var schoolTypeList = ArrayList<String>()
    private var schoolTotalStdList = ArrayList<String>()
    private var foodlistItem = ArrayList<FoodlistItem>()
    private var flag: Boolean = false

    private var mealQuality = ""
    private var foodItem = ""
    private var imageCategory = ""

    private var itemCount = 0

    private lateinit var cacheFile: File
    private lateinit var cacheFileUri: Uri

    private var nutritionArrayList: ArrayList<NutritionModel> = ArrayList()
    private var complaintsArrayList: ArrayList<ComplaintsModel> = ArrayList()
    private var classArrayList: ArrayList<FormClassModel> = ArrayList()
    private var imageArrayList: ArrayList<FieldImageModel> = ArrayList()

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
        _binding = FragmentVisitFromBinding.inflate(inflater,container,false)

        activityViewModel = ViewModelProvider(requireActivity()).get(AuditorActivityViewModel::class.java)
        fieldVisitViewModel = ViewModelProvider(this).get(FieldVisitViewModel::class.java)
        CustomUploadDialog.init(requireContext())

        try {
            school_id = requireArguments().getString("id","")
            type_id = requireArguments().getString("task","")
        }catch (e: Exception){
            Log.wtf("VisitFormFragment",e.message)
        }

        activityViewModel.allSchoolListResponse.observe(viewLifecycleOwner) {

            if (it?.data?.isNotEmpty() == true) {

                if (!flag) {
                    for (i in it.data.indices) {
                        schoolIdList.add(it.data[i]?.id.toString())
                        schoolNameList.add(it.data[i]?.school_name.toString())
                        schoolZoneIdList.add(it.data[i]?.zone_id.toString())
                        schoolTypeList.add(it.data[i]?.school_type.toString())
                        schoolTotalStdList.add(it.data[i]?.total_std.toString())
                    }
                    flag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    R.id.textItem,
                    schoolNameList
                )
                binding.spinner.adapter = spinnerArrayAdapter

                if (school_id != "") {
                    for (i in schoolIdList.indices) {
                        if (school_id == schoolIdList[i]) {
                            println("School Id ${schoolIdList[i]}")
                            binding.spinner.setSelection(i)
                            binding.spinner.isEnabled = false
                            break
                        }
                    }
                }
            }
        }

        fieldVisitViewModel.addImageResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                itemCount++
                fieldVisitViewModel.imageCategoryUploadedList.add(it.data.toString())
            }

            if (itemCount != fieldVisitViewModel.imageCategoryFile.size) {
                uploadImages(itemCount)
            } else {
                uploadAllInfo()
            }

        }

        fieldVisitViewModel.fieldVisitResponse.observe(viewLifecycleOwner) {
            if (it?.response_status == "200") {
                CustomUploadDialog.dismiss()
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_visitFromFragment_to_auditorHomeFragment)
            } else {
                CustomUploadDialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }

        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())

        binding.dateET.setText(currentDate)
        binding.timeET.setText(currentTime)

        mealQuality = "Good"
        foodItem = ""

        binding.goodRB.isChecked = true
        binding.AvgRB.isChecked = false
        binding.WorseRB.isChecked = false

        //set recycler views
        nutritionLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        nutritionAdapter = NutritionAdapter(fieldVisitViewModel)
        binding.nutritionRecyclerView.layoutManager = nutritionLayoutManager
        binding.nutritionRecyclerView.adapter = nutritionAdapter

        foodAdapter = FoodAdapter(fieldVisitViewModel)
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.foodRecyclerView.adapter = foodAdapter

        foundationLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        foundationAdapter = FoundationAdapter(fieldVisitViewModel)
        binding.foundationRecyclerView.layoutManager = foundationLayoutManager
        binding.foundationRecyclerView.adapter = foundationAdapter

        addClassLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        formAddClassAdapter = FormAddClassAdapter(fieldVisitViewModel,binding.presentET,binding.absentET)
        binding.classRecyclerView.layoutManager = addClassLayoutManager
        binding.classRecyclerView.adapter = formAddClassAdapter

        imageCategoryManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        imageCategoryAdapter = ImageCategoryAdapter(fieldVisitViewModel,requireContext())
        binding.categoryImageRecycler.layoutManager = imageCategoryManager
        binding.categoryImageRecycler.adapter = imageCategoryAdapter

        //back press action
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        //add nutrition action
        binding.addNutritionBtn.setOnClickListener {

            if (binding.nutritionTitle.text.toString() == ""){
                binding.nutritionTitle.requestFocus()
                binding.nutritionTitle.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.nutritionAmount.text.toString() == ""){
                binding.nutritionAmount.requestFocus()
                binding.nutritionAmount.error = "Can't Be Empty"
                return@setOnClickListener
            }

            fieldVisitViewModel.nutritionTitleList.add(binding.nutritionTitle.text.toString())
            fieldVisitViewModel.nutritionAmountList.add(binding.nutritionAmount.text.toString())

            nutritionAdapter.getNewCount(fieldVisitViewModel.nutritionTitleList.size)
            binding.nutritionTitle.text.clear()
            binding.nutritionAmount.text.clear()
        }

        binding.addFoodBtn.setOnClickListener {
            if (foodlistItem.isEmpty()){
                binding.milkRB.requestFocus()
                Toast.makeText(requireContext(),"Select at least one food item",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fieldVisitViewModel.foodlistItem = foodlistItem
            foodAdapter.getNewCount(fieldVisitViewModel.foodlistItem.size)

            binding.milkRB.isChecked = false
            binding.breadRB.isChecked = false
            binding.fruitRB.isChecked = false
            binding.biscuitRB.isChecked = false
            binding.eggRB.isChecked = false
        }

        binding.addClassBtn.setOnClickListener {

            if (binding.classTitleET.text.toString().isEmpty()){
                binding.classTitleET.requestFocus()
                binding.classTitleET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.totalStudentET.text.toString().isEmpty()){
                binding.totalStudentET.requestFocus()
                binding.totalStudentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.classPresentET.text.toString().isEmpty()){
                binding.classPresentET.requestFocus()
                binding.classPresentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.classAbsentET.text.toString().isEmpty()){
                binding.classAbsentET.requestFocus()
                binding.classAbsentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.avgAgeET.text.toString().isEmpty()){
                binding.avgAgeET.requestFocus()
                binding.avgAgeET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            fieldVisitViewModel.className.add(binding.classTitleET.text.toString())
            fieldVisitViewModel.totalStudent.add(binding.totalStudentET.text.toString())
            fieldVisitViewModel.totalPresent.add(binding.classPresentET.text.toString())
            fieldVisitViewModel.totalAbsent.add(binding.classAbsentET.text.toString())
            fieldVisitViewModel.avgAge.add(binding.avgAgeET.text.toString())

            formAddClassAdapter.getNewCount(fieldVisitViewModel.className.size)
            binding.presentET.setText(fieldVisitViewModel.getTotalPresent().toString())
            binding.absentET.setText(fieldVisitViewModel.getTotalAbsent().toString())

            binding.classTitleET.text.clear()
            binding.totalStudentET.text.clear()
            binding.classPresentET.text.clear()
            binding.classAbsentET.text.clear()
            binding.avgAgeET.text.clear()
        }

        //counter btn action
        binding.addNewTeacher.setOnClickListener {

            /*if (binding.foundationTitleET.text.toString() == ""){
                binding.foundationTitleET.requestFocus()
                binding.foundationTitleET.error = "Can't Be Empty"
                return@setOnClickListener
            }*/

           /* if (binding.foundationTeacherET.text.toString() == ""){
                binding.foundationTeacherET.requestFocus()
                binding.foundationTeacherET.error = "Can't Be Empty"
                return@setOnClickListener
            }*/

            /*if (binding.foundationSuggestImprovementET.text.toString() == ""){
                binding.foundationSuggestImprovementET.requestFocus()
                binding.foundationSuggestImprovementET.error = "Can't Be Empty"
                return@setOnClickListener
            }*/

            fieldVisitViewModel.foundationTitleList.add(binding.foundationTitleET.text.toString())
            fieldVisitViewModel.foundationTeacherList.add(binding.foundationTeacherET.text.toString())
            fieldVisitViewModel.foundationImprovementList.add(binding.foundationSuggestImprovementET.text.toString())

            foundationAdapter.getNewCount(fieldVisitViewModel.foundationTeacherList.size)
            binding.foundationTitleET.text.clear()
            binding.foundationTeacherET.text.clear()
            binding.foundationSuggestImprovementET.text.clear()
        }

        //step one btn action
        binding.stepOneBtn.setOnClickListener {

            if (binding.presentET.text.toString() == ""){
                binding.presentET.requestFocus()
                binding.presentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.absentET.text.toString() == ""){
                binding.absentET.requestFocus()
                binding.absentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.reasonET.text.toString() == ""){
                binding.reasonET.requestFocus()
                binding.reasonET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.avgET.text.toString() == ""){
                binding.avgET.requestFocus()
                binding.avgET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.totalTeacherET.text.toString() == ""){
                binding.totalTeacherET.requestFocus()
                binding.totalTeacherET.error = "Can't Be Empty"
                return@setOnClickListener
            }
            if (binding.teacherPresentET.text.toString() == ""){
                binding.teacherPresentET.requestFocus()
                binding.teacherPresentET.error = "Can't Be Empty"
                return@setOnClickListener
            }
            if (binding.teacherAbsentET.text.toString() == ""){
                binding.teacherAbsentET.requestFocus()
                binding.teacherAbsentET.error = "Can't Be Empty"
                return@setOnClickListener
            }


            binding.stepOneBtn.visibility = View.GONE

            binding.stepOneLayout.visibility = View.VISIBLE
            binding.stepTwoBtn.visibility = View.VISIBLE
        }

        //step two btn action
        binding.stepTwoBtn.setOnClickListener {

            if (binding.presentET.text.toString() == ""){
                binding.presentET.requestFocus()
                binding.presentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.absentET.text.toString() == ""){
                binding.absentET.requestFocus()
                binding.absentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.reasonET.text.toString() == ""){
                binding.reasonET.requestFocus()
                binding.reasonET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.avgET.text.toString() == ""){
                binding.avgET.requestFocus()
                binding.avgET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.performanceET.text.toString() == ""){
                binding.performanceET.requestFocus()
                binding.performanceET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.efficiencyET.text.toString() == ""){
                binding.efficiencyET.requestFocus()
                binding.efficiencyET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            binding.stepTwoBtn.visibility = View.GONE

            binding.stepTwoLayout.visibility = View.VISIBLE
            binding.stepThreeBtn.visibility = View.VISIBLE
        }

        binding.milkRB.setOnClickListener {
            foodlistItem.add(FoodlistItem("Milk"))
            binding.milkRB.isChecked = true
        }
        binding.breadRB.setOnClickListener {
            foodlistItem.add(FoodlistItem("Bread"))
            binding.breadRB.isChecked = true
        }
        binding.fruitRB.setOnClickListener {
            foodlistItem.add(FoodlistItem("Fruit"))
            binding.fruitRB.isChecked = true
        }
        binding.biscuitRB.setOnClickListener {
            foodlistItem.add(FoodlistItem("Biscuit"))
            binding.biscuitRB.isChecked = true
        }
        binding.eggRB.setOnClickListener {
            foodlistItem.add(FoodlistItem("Egg"))
            binding.eggRB.isChecked = true
        }

        binding.goodRB.setOnClickListener {
            mealQuality = "Good"
            binding.goodRB.isChecked = true
            binding.AvgRB.isChecked = false
            binding.WorseRB.isChecked = false
        }

        binding.AvgRB.setOnClickListener {
            mealQuality = "Average"
            binding.goodRB.isChecked = false
            binding.AvgRB.isChecked = true
            binding.WorseRB.isChecked = false
        }

        binding.WorseRB.setOnClickListener {
            mealQuality = "Poor"
            binding.goodRB.isChecked = false
            binding.AvgRB.isChecked = false
            binding.WorseRB.isChecked = true
        }

        //step three btn action
        binding.stepThreeBtn.setOnClickListener {

            if (binding.presentET.text.toString() == ""){
                binding.presentET.requestFocus()
                binding.presentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.absentET.text.toString() == ""){
                binding.absentET.requestFocus()
                binding.absentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.reasonET.text.toString() == ""){
                binding.reasonET.requestFocus()
                binding.reasonET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.avgET.text.toString() == ""){
                binding.avgET.requestFocus()
                binding.avgET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.performanceET.text.toString() == ""){
                binding.performanceET.requestFocus()
                binding.performanceET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.efficiencyET.text.toString() == ""){
                binding.efficiencyET.requestFocus()
                binding.efficiencyET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.approxPriceET.text.toString() == ""){
                binding.approxPriceET.requestFocus()
                binding.approxPriceET.error = "Cant Be Empty"
                return@setOnClickListener
            }

            binding.stepThreeBtn.visibility = View.GONE

            binding.stepThreeLayout.visibility = View.VISIBLE
            binding.stepFourBtn.visibility = View.VISIBLE
        }

        //step four btn action
        binding.stepFourBtn.setOnClickListener {

            if (binding.presentET.text.toString() == ""){
                binding.presentET.requestFocus()
                binding.presentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.absentET.text.toString() == ""){
                binding.absentET.requestFocus()
                binding.absentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.reasonET.text.toString() == ""){
                binding.reasonET.requestFocus()
                binding.reasonET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.avgET.text.toString() == ""){
                binding.avgET.requestFocus()
                binding.avgET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.performanceET.text.toString() == ""){
                binding.performanceET.requestFocus()
                binding.performanceET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.efficiencyET.text.toString() == ""){
                binding.efficiencyET.requestFocus()
                binding.efficiencyET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.approxPriceET.text.toString() == ""){
                binding.approxPriceET.requestFocus()
                binding.approxPriceET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            binding.stepFourBtn.visibility = View.GONE

            binding.stepFourLayout.visibility = View.VISIBLE
        }

        binding.categoryPicCard.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cacheFile = File(requireActivity().externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
                cacheFileUri = FileProvider.getUriForFile(requireContext(),"com.charityright.charityauthority.provider", cacheFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheFileUri)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 1)
            } else {
                requestPermission()
            }
        }

        //image category radio action
        binding.foodRB.setOnClickListener {
            imageCategory = "Food & Kitchen"
            binding.foodRB.isChecked = true
            binding.classRB.isChecked = false
            binding.diningRB.isChecked = false
            binding.attendanceRB.isChecked = false
            binding.groupRB.isChecked = false
        }

        binding.classRB.setOnClickListener {
            imageCategory = "Class Education"
            binding.foodRB.isChecked = false
            binding.classRB.isChecked = true
            binding.diningRB.isChecked = false
            binding.attendanceRB.isChecked = false
            binding.groupRB.isChecked = false
        }

        binding.diningRB.setOnClickListener {
            imageCategory = "Dining"
            binding.foodRB.isChecked = false
            binding.classRB.isChecked = false
            binding.diningRB.isChecked = true
            binding.attendanceRB.isChecked = false
            binding.groupRB.isChecked = false
        }

        binding.attendanceRB.setOnClickListener {
            imageCategory = "Attendance Sheet"
            binding.foodRB.isChecked = false
            binding.classRB.isChecked = false
            binding.diningRB.isChecked = false
            binding.attendanceRB.isChecked = true
            binding.groupRB.isChecked = false
        }

        binding.groupRB.setOnClickListener {
            imageCategory = "Group/Play Time"
            binding.foodRB.isChecked = false
            binding.classRB.isChecked = false
            binding.diningRB.isChecked = false
            binding.attendanceRB.isChecked = false
            binding.groupRB.isChecked = true
        }

        binding.addNewPicture.setOnClickListener {

            if (imageCategory != "" && fieldVisitViewModel.imageCategoryName.size != 5){
                fieldVisitViewModel.imageCategoryName.add(imageCategory)
                fieldVisitViewModel.imageCategoryFile.add(cacheFile)
                fieldVisitViewModel.imageCategoryList.add(cacheFileUri)

                imageCategoryAdapter.getNewCount(fieldVisitViewModel.imageCategoryName.size)
                clearImageInfo()

            }else{
                Toast.makeText(requireContext(),"Please Select At Least One Image And Maximum 5 Image",Toast.LENGTH_SHORT).show()
            }

        }

        //submit btn action
        binding.submitBtn.setOnClickListener {

            if (binding.presentET.text.toString() == ""){
                binding.presentET.requestFocus()
                binding.presentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.absentET.text.toString() == ""){
                binding.absentET.requestFocus()
                binding.absentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.reasonET.text.toString() == ""){
                binding.reasonET.requestFocus()
                binding.reasonET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.avgET.text.toString() == ""){
                binding.avgET.requestFocus()
                binding.avgET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.performanceET.text.toString() == ""){
                binding.performanceET.requestFocus()
                binding.performanceET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.efficiencyET.text.toString() == ""){
                binding.efficiencyET.requestFocus()
                binding.efficiencyET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.approxPriceET.text.toString() == ""){
                binding.approxPriceET.requestFocus()
                binding.approxPriceET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (fieldVisitViewModel.imageCategoryName.isEmpty()){
                Toast.makeText(requireContext(),"Please Add Data In Image Category List",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /*if (binding.remarksET.text.toString() == ""){
                binding.remarksET.requestFocus()
                binding.remarksET.error = "Can't Be Empty"
                return@setOnClickListener
            }*/

            if (itemCount > 10){
                Toast.makeText(requireContext(),"10 Maximum Image Can be Chosen",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.wtf("foodlistCheck",Gson().toJson(foodlistItem))
            Log.wtf("complainListCheck",getComplaintsJsonObject().toString())
            Log.wtf("teacherCheck",binding.totalTeacherET.text.toString()+" , "+binding.teacherPresentET.text.toString()+" , "+binding.teacherAbsentET.text.toString() )

            fieldVisitViewModel.task_id = type_id
            fieldVisitViewModel.zone_id = schoolZoneIdList[binding.spinner.selectedItemPosition]
            fieldVisitViewModel.school_id = schoolIdList[binding.spinner.selectedItemPosition]
            fieldVisitViewModel.school_time = binding.timeET.text.toString()
            fieldVisitViewModel.date = binding.dateET.text.toString()
            fieldVisitViewModel.total_std = schoolTotalStdList[binding.spinner.selectedItemPosition]
            fieldVisitViewModel.present_std = binding.presentET.text.toString()
            fieldVisitViewModel.absent_std = binding.absentET.text.toString()
            fieldVisitViewModel.reason_absent = binding.reasonET.text.toString()
            fieldVisitViewModel.avg_age_std = binding.avgET.text.toString()
            fieldVisitViewModel.edu_type = schoolTypeList[binding.spinner.selectedItemPosition]
            fieldVisitViewModel.performance = binding.performanceET.text.toString()
            fieldVisitViewModel.efficiency_other = binding.efficiencyET.text.toString()
            fieldVisitViewModel.approx_meal_price = binding.approxPriceET.text.toString()
            fieldVisitViewModel.meal_quality = mealQuality
            fieldVisitViewModel.foodlist = Gson().toJson(foodlistItem)
            fieldVisitViewModel.totalTeacher = binding.totalTeacherET.text.toString()
            fieldVisitViewModel.presentTeacher = binding.teacherPresentET.text.toString()
            fieldVisitViewModel.absentTeacher = binding.teacherAbsentET.text.toString()

            fieldVisitViewModel.suggestion = binding.suggestImprovementET.text.toString()
            fieldVisitViewModel.nutrition_list = getNutritionJsonObject().toString()
            fieldVisitViewModel.complaints_list = getComplaintsJsonObject().toString()
            fieldVisitViewModel.class_list = getClassJsonObject().toString()
            fieldVisitViewModel.remarks = binding.remarksET.text.toString()

            CustomUploadDialog.show()
            uploadImages(itemCount)

        }

        return binding.root
    }

    private fun uploadAllInfo() {
        for (i in fieldVisitViewModel.imageCategoryFile.indices){
            fieldVisitViewModel.imageCategoryFile[i].delete()
        }

        fieldVisitViewModel.images = getImageJsonObject().toString()

        lifecycleScope.launch {
            fieldVisitViewModel.launchFieldApiCall()
        }
    }

    private fun uploadImages(index: Int) {
        lifecycleScope.launch {
            val compressedImageFile = Compressor.compress(requireContext(), fieldVisitViewModel.imageCategoryFile[index]){
                resolution(512,512)
                quality(50)
            }
            fieldVisitViewModel.launchImageUploadApiCall(compressedImageFile)
        }
    }

    private fun clearImageInfo() {

        imageCategory = ""
        binding.foodRB.isChecked = false
        binding.classRB.isChecked = false
        binding.diningRB.isChecked = false
        binding.attendanceRB.isChecked = false
        binding.groupRB.isChecked = false

        binding.imageView1.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.add_image))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, cacheFileUri)
                        val temp = getResizedBitmap(bitmap, 120, 120)
                        binding.imageView1.setImageBitmap(temp)

                    }catch (e: Exception){
                        Log.wtf("FiledVisitFormFragment", "onActivityResult: ",e)
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

    fun getNutritionJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        nutritionArrayList.clear()

        for (i in fieldVisitViewModel.nutritionTitleList.indices){
            val tempAddNutritionName = NutritionModel(fieldVisitViewModel.nutritionTitleList[i],fieldVisitViewModel.nutritionAmountList[i])
            nutritionArrayList.add(tempAddNutritionName)
        }

        return gson.toJsonTree(nutritionArrayList).asJsonArray
    }

    fun getComplaintsJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        complaintsArrayList.clear()

        for (i in fieldVisitViewModel.foundationTitleList.indices){
            val tempAddComplaintsName = ComplaintsModel(fieldVisitViewModel.foundationTitleList[i],fieldVisitViewModel.foundationTeacherList[i],fieldVisitViewModel.foundationImprovementList[i])
            complaintsArrayList.add(tempAddComplaintsName)
        }

        return gson.toJsonTree(complaintsArrayList).asJsonArray
    }

    fun getImageJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        imageArrayList.clear()

        for (i in fieldVisitViewModel.imageCategoryUploadedList.indices){
            val tempImageList = FieldImageModel(fieldVisitViewModel.imageCategoryName[i],fieldVisitViewModel.imageCategoryUploadedList[i])
            imageArrayList.add(tempImageList)
        }

        return gson.toJsonTree(imageArrayList).asJsonArray
    }

    fun getClassJsonObject(): JsonArray? {
        val gson = GsonBuilder().create()

        classArrayList.clear()

        for (i in fieldVisitViewModel.className.indices){
            val tempClassList = FormClassModel(fieldVisitViewModel.className[i],fieldVisitViewModel.totalStudent[i],fieldVisitViewModel.totalPresent[i],fieldVisitViewModel.totalAbsent[i],fieldVisitViewModel.avgAge[i])
            classArrayList.add(tempClassList)
        }

        return gson.toJsonTree(classArrayList).asJsonArray

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}