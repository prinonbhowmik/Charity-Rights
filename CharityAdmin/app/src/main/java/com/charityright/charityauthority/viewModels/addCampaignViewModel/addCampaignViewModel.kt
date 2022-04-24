package com.charityright.charityauthority.viewModels.adminViewModel.addCampaignViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.addSchool.AddSchoolImageBaseResponse
import com.charityright.charityauthority.model.admin.campaignDetails.campaignDetailsBaseReponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.io.File

class addCampaignViewModel(application: Application) : AndroidViewModel(application) {
    val addCampaignResponse: LiveData<campaignDetailsBaseReponse?> = MutableLiveData()
    val addImageResponse: LiveData<AddSchoolImageBaseResponse?> = MutableLiveData()

    var zone_id = ""
    var title = ""
    var date = ""
    var location = ""
    var details = ""
    var objective = ""
    var beneficiary = ""
    var m_name = ""
    var m_contact = ""
    var images  = ""

    var imageList = ArrayList<String>()

    suspend fun launchAddImageApiCall(file: File){
        addImageResponse as MutableLiveData

        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val updateImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

        try {
            addImageResponse.value = getAddImageResponse(updateImage)
        }catch (e: java.lang.Exception){
            Log.wtf("AddCampaignFragmentViewModel Image Uploading",e.message)
        }
    }

    suspend fun launchAddCampaignApiCall(){
        addCampaignResponse as MutableLiveData

        try {
            addCampaignResponse.value = getAddCampaignResponse()
        } catch (e: Exception) {
            Log.wtf("addCampaignViewModel", e.message)
        }

    }


    private suspend fun getAddCampaignResponse(): campaignDetailsBaseReponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).addCampaignData("Bearer "+ CustomSharedPref.read("TOKEN",""),zone_id, title, date, location, details, objective, beneficiary, m_name, m_contact, images)
        return withContext(Dispatchers.IO){
            response.await()
        }
    }

    private suspend fun getAddImageResponse(updateImage: MultipartBody.Part): AddSchoolImageBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).addSchoolImage("Bearer "+ CustomSharedPref.read("TOKEN",""),updateImage)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}