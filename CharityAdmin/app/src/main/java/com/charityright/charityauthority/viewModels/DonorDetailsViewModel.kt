package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.donorDetails.DonorDetailsBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class donorDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val donorDetailsResponse: LiveData<DonorDetailsBaseResponse?> = MutableLiveData()

    var donor_id = ""

    suspend fun launchDonorDetailsApiCall(){
        donorDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            donorDetailsResponse.value = getDonorDetailsResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("DonorDetailsViewModel",e.message)
        }
    }

    private suspend fun getDonorDetailsResponse(): DonorDetailsBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getDonorDetails("Bearer "+ CustomSharedPref.read("TOKEN",""),donor_id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}