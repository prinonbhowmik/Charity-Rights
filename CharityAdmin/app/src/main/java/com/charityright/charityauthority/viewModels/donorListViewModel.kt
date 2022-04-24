package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.donorList.DonorListBaseModel
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class donorListViewModel(application: Application) : AndroidViewModel(application) {
    val donorListResponse: LiveData<DonorListBaseModel?> = MutableLiveData()

    suspend fun launchApiCall(){
        donorListResponse as MutableLiveData

        try {
            CustomDialog.show()
            donorListResponse.value = getDonorListResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("DonorListViewModel",e.message)
        }
    }

    private suspend fun getDonorListResponse(): DonorListBaseModel {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getDonorList("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}