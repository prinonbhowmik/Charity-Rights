package com.charityright.charityauthority.auditor.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.auditor.model.ImageUploadModel
import com.charityright.charityauthority.model.admin.BaseResponse
import com.charityright.charityauthority.retrofit.AuditorRequestInterface
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.io.File
import java.lang.Exception

class BeneficiaryViewModel(application: Application) : AndroidViewModel(application) {

    val addBeneficiaryResponse: LiveData<BaseResponse?> = MutableLiveData()
    val addImageResponse: LiveData<ImageUploadModel?> = MutableLiveData()

    var title = ""
    var name = ""
    var age = ""
    var location = ""
    var additional = ""
    var remarks = ""
    var images = ""

    var imageList = ArrayList<String>()

    suspend fun launchBeneficiaryApiCall(){
        addBeneficiaryResponse as MutableLiveData

        try {
            addBeneficiaryResponse.value = getBeneficiaryResponse()
        }catch (e: Exception){
            Log.wtf("AddBeneficiaryViewModel",e.message)
        }
    }

    suspend fun launchImageUploadApiCall(file: File){
        addImageResponse as MutableLiveData

        try {

            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            val updateImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

            addImageResponse.value = getImageUploadResponse(updateImage)
        }catch (e: Exception){
            Log.wtf("AddBeneficiaryViewModel ImageUpload",e.message)
        }
    }

    private suspend fun getImageUploadResponse(updateImage: MultipartBody.Part): ImageUploadModel {
        val response = AuditorRequestInterface(getApplication<Application>().applicationContext).uploadImage("Bearer "+ CustomSharedPref.read("TOKEN",""),updateImage)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }


    private suspend fun getBeneficiaryResponse(): BaseResponse {
        val response = AuditorRequestInterface(getApplication<Application>().applicationContext).addBeneficiary("Bearer "+ CustomSharedPref.read("TOKEN",""),title,name,age,location,additional,remarks,images)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}