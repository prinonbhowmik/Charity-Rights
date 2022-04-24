package com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel

import android.app.Application
import android.content.Context
import android.location.Address
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.addSchool.AddSchoolBaseResponse
import com.charityright.charityauthority.model.admin.addSchool.AddSchoolImageBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.io.File
import java.lang.Exception
import android.location.Geocoder
import java.util.*
import kotlin.collections.ArrayList


class AddSchoolFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val addSchoolResponse: LiveData<AddSchoolBaseResponse?> = MutableLiveData()
    val addImageResponse: LiveData<AddSchoolImageBaseResponse?> = MutableLiveData()

    var zone_id = ""
    var school_name = ""
    var address = ""
    var details = ""
    var class_list = ""
    var images = ""

    var lat: Double = 0.0
    var lon: Double = 0.0

    var classList = ArrayList<String>()
    var imageList = ArrayList<String>()

    suspend fun launchAddImageApiCall(file: File){
        addImageResponse as MutableLiveData

        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val updateImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

        try {
            addImageResponse.value = getAddImageResponse(updateImage)
        }catch (e: Exception){
            Log.wtf("AddSchoolFragmentViewModel",e.message)
        }
    }

    suspend fun launchAddSchoolApiCall(){
        addSchoolResponse as MutableLiveData

        try {
            addSchoolResponse.value = getAddSchoolResponse()
        }catch (e: Exception){
            Log.wtf("AddSchoolFragmentViewModel",e.message)
        }
    }

    private suspend fun getAddImageResponse(updateImage: MultipartBody.Part): AddSchoolImageBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).addSchoolImage("Bearer "+ CustomSharedPref.read("TOKEN",""),updateImage)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

    private suspend fun getAddSchoolResponse(): AddSchoolBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).addSchool("Bearer "+ CustomSharedPref.read("TOKEN",""),zone_id,school_name,address,details,lat.toString(),lon.toString(),class_list,images)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}