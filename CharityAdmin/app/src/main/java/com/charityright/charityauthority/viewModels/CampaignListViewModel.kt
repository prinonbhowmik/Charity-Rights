package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.campaignList.campaignBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class CampaignListViewModel(application: Application) : AndroidViewModel(application) {

    val campaignListResponse: LiveData<campaignBaseResponse?> = MutableLiveData()

    var zone_id = ""

    suspend fun launchCampaignApiCall(){
        campaignListResponse as MutableLiveData

        try {
            CustomDialog.show()
            campaignListResponse.value = getCampaignResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("CampaignListViewModel",e.message)
        }
    }


    private suspend fun getCampaignResponse(): campaignBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).campaignList("Bearer "+ CustomSharedPref.read("TOKEN",""),zone_id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}