package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.campaignDetails.campaignDetailsBaseReponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class campaignDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val campaignDetailsResponse: LiveData<campaignDetailsBaseReponse?> = MutableLiveData()

    var campaign_id = ""

    suspend fun launchCampaignDetailsApiCall(){
        campaignDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            campaignDetailsResponse.value = getCampaignDetailsResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("CampaignDetailsViewModel",e.message)
        }
    }


    private suspend fun getCampaignDetailsResponse(): campaignDetailsBaseReponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).campaignDetails("Bearer "+ CustomSharedPref.read("TOKEN",""),campaign_id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}