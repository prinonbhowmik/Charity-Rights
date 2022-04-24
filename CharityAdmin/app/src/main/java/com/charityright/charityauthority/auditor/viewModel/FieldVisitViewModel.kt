package com.charityright.charityauthority.auditor.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.auditor.model.ImageUploadModel
import com.charityright.charityauthority.model.FoodlistItem
import com.charityright.charityauthority.model.admin.BaseResponse
import com.charityright.charityauthority.retrofit.AuditorRequestInterface
import com.charityright.charityauthority.util.CustomSharedPref
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.io.File
import java.lang.Exception

class FieldVisitViewModel(application: Application) : AndroidViewModel(application) {

    val addImageResponse: LiveData<ImageUploadModel?> = MutableLiveData()
    val fieldVisitResponse: LiveData<BaseResponse?> = MutableLiveData()

    var nutritionTitleList = ArrayList<String>()
    var nutritionAmountList = ArrayList<String>()

    var foundationTitleList = ArrayList<String>()
    var foundationTeacherList = ArrayList<String>()
    var foundationImprovementList = ArrayList<String>()
    var foodlistItem = ArrayList<FoodlistItem>()

    var imageCategoryName = ArrayList<String>()
    var imageCategoryUploadedList = ArrayList<String>()
    var imageCategoryFile = ArrayList<File>()
    var imageCategoryList = ArrayList<Uri>()

    var className = ArrayList<String>()
    var totalStudent = ArrayList<String>()
    var totalPresent = ArrayList<String>()
    var totalAbsent = ArrayList<String>()
    var avgAge = ArrayList<String>()

    var task_id = ""
    var zone_id = ""
    var school_id = ""
    var school_time = ""
    var date = ""
    var total_std = ""
    var present_std = ""
    var absent_std = ""
    var reason_absent = ""
    var totalTeacher = ""
    var presentTeacher = ""
    var absentTeacher = ""
    var avg_age_std = ""
    var edu_type = ""
    var performance = ""
    var efficiency_other = ""
    var approx_meal_price = ""
    var meal_quality = ""
    var suggestion = ""
    var nutrition_list = ""
    var foodlist = ""
    var complaints_list = ""
    var class_list = ""
    var images = ""
    var remarks = ""

    fun getTotalPresent(): Int{
        var temp = 0
        for (i in totalPresent.indices){
            temp += totalPresent[i].toInt()
        }
        return temp
    }

    fun getTotalAbsent(): Int{
        var temp = 0
        for (i in totalAbsent.indices){
            temp += totalAbsent[i].toInt()
        }
        return temp
    }

    suspend fun launchImageUploadApiCall(file: File) {
        addImageResponse as MutableLiveData

        try {

            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            val updateImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

            addImageResponse.value = getImageUploadResponse(updateImage)
        } catch (e: Exception) {
            Log.wtf("FieldVisitViewModel ImageUpload", e.message)
        }
    }

    suspend fun launchFieldApiCall() {
        fieldVisitResponse as MutableLiveData

        try {

            val updateTaskId: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), task_id)
            val updateZoneId: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), zone_id)
            val updateSchoolId: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), school_id)
            val updateSchoolTime: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), school_time)
            val updateDate: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), date)
            val updateTotalStd: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), total_std)
            val updatePresentStd: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), present_std)
            val updateAbsentStd: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), absent_std)
            val updateReasonAbsent: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), reason_absent)
            val updateAvgAge: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), avg_age_std)
            val updateEduType: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), edu_type)
            val updatePerformance: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), performance)
            val updateEfficiency: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), efficiency_other)
            val updateMealPrice: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), approx_meal_price)
            val updateMealQuality: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), meal_quality)
            val updateFoodList: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), foodlist)
            val updateTotalTeacher: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), totalTeacher)
            val updatePresentTeacher: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), presentTeacher)
            val updateAbsentTeacher: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), absentTeacher)
            val updateSuggestion: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), suggestion)
            val updateNutritionList: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), nutrition_list)
            val updateComplaintsList: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), complaints_list)
            val updateImageList: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), images)
            val updateRemarks: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), remarks)
            val updateClassList: RequestBody =
                RequestBody.create(MediaType.parse("multiplart/form-data"), class_list)

            fieldVisitResponse.value = getFieldAddResponse(
                updateTaskId,
                updateZoneId,
                updateSchoolId,
                updateSchoolTime,
                updateDate,
                updateTotalStd,
                updatePresentStd,
                updateAbsentStd,
                updateReasonAbsent,
                updateAvgAge,
                updateEduType,
                updatePerformance,
                updateEfficiency,
                updateMealPrice,
                updateMealQuality,
                updateFoodList,
                updateTotalTeacher,
                updatePresentTeacher,
                updateAbsentTeacher,
                updateSuggestion,
                updateNutritionList,
                updateComplaintsList,
                updateImageList,
                updateRemarks,
                updateClassList

            )
        } catch (e: Exception) {
            Log.wtf("FieldVisitViewModel", e.message)
        }
    }

    private suspend fun getImageUploadResponse(updateImage: MultipartBody.Part): ImageUploadModel {
        val response =
            AuditorRequestInterface(getApplication<Application>().applicationContext).uploadImage(
                "Bearer " + CustomSharedPref.read(
                    "TOKEN",
                    ""
                ), updateImage
            )

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

    private suspend fun getFieldAddResponse(
        updateTaskId: RequestBody,
        updateZoneId: RequestBody,
        updateSchoolId: RequestBody,
        updateSchoolTime: RequestBody,
        updateDate: RequestBody,
        updateTotalStd: RequestBody,
        updatePresentStd: RequestBody,
        updateAbsentStd: RequestBody,
        updateReasonAbsent: RequestBody,
        updateAvgAge: RequestBody,
        updateEduType: RequestBody,
        updatePerformance: RequestBody,
        updateEfficiency: RequestBody,
        updateMealPrice: RequestBody,
        updateMealQuality: RequestBody,
        updateFoodList: RequestBody,
        updateTotalTeacher: RequestBody,
        updatePresentTeacher: RequestBody,
        updateAbsentTeacher: RequestBody,
        updateSuggestion: RequestBody,
        updateNutritionList: RequestBody,
        updateComplaintsList: RequestBody,
        updateImageList: RequestBody,
        updateRemarks: RequestBody,
        updateClassList: RequestBody
    ): BaseResponse {
        val response =
            AuditorRequestInterface(getApplication<Application>().applicationContext).addFieldVisit(
                "Bearer " + CustomSharedPref.read("TOKEN", ""),
                updateZoneId,
                updateSchoolId,
                updateSchoolTime,
                updateDate,
                updateTotalStd,
                updatePresentStd,
                updateAbsentStd,
                updateReasonAbsent,
                updateAvgAge,
                updateEduType,
                updatePerformance,
                updateEfficiency,
                updateMealPrice,
                updateMealQuality,
                updateFoodList,
                updateTotalTeacher,
                updatePresentTeacher,
                updateAbsentTeacher,
                updateSuggestion,
                updateNutritionList,
                updateComplaintsList,
                updateRemarks,
                updateImageList,
                updateTaskId,
                updateClassList
            )

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

}