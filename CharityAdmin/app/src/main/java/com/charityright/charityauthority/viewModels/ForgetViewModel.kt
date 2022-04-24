package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.BaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class ForgetViewModel(application: Application) : AndroidViewModel(application) {
    val forgetResponse: LiveData<BaseResponse?> = MutableLiveData()

    var email = ""

    suspend fun launchApiCall(){
        forgetResponse as MutableLiveData

        try {
            CustomDialog.show()
            forgetResponse.value = getForgetResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ForgetViewModel",e.message)
        }
    }

    private suspend fun getForgetResponse(): BaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).forgetApi(email)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}