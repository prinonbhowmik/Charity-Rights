package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.HistoryModelClass.HistoryBaseModelClass
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class DonationHistoryViewModel(application: Application) : AndroidViewModel(application) {

    val donationHistoryResponse: LiveData<HistoryBaseModelClass?> = MutableLiveData()


    suspend fun launchApiCall(){
        donationHistoryResponse as MutableLiveData

        try {
            CustomDialog.show()
            donationHistoryResponse.value = getHistoryResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("DonationHistoryViewModel",e.message)
        }
    }

    private suspend fun getHistoryResponse(): HistoryBaseModelClass {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).getUserDonationHistory("Bearer "+ CustomSharedPref.read("Token",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}