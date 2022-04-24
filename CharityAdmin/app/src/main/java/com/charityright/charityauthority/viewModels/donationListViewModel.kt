package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.donationList.DonationListBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class donationListViewModel(application: Application) : AndroidViewModel(application) {
    val donationListResponse: LiveData<DonationListBaseResponse?> = MutableLiveData()

    var startDate = ""
    var endDate = ""
    var donationType = ""

    suspend fun launchApiCall(){
        donationListResponse as MutableLiveData

        try {
            CustomDialog.show()
            donationListResponse.value = getDonationListResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("DonationListViewModel",e.message)
        }
    }

    private suspend fun getDonationListResponse(): DonationListBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getDonationList("Bearer "+ CustomSharedPref.read("TOKEN",""),startDate,endDate,donationType)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}