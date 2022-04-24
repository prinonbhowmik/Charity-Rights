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

class ConfirmPassViewModel(application: Application) : AndroidViewModel(application) {
    val confPassResponse: LiveData<BaseResponse?> = MutableLiveData()

    var email = ""
    var pass = ""
    var otp = ""

    suspend fun launchApiCall(){
        confPassResponse as MutableLiveData

        try {
            CustomDialog.show()
            confPassResponse.value = getConfirmPassResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ConfirmPasswordViewModel",e.message)
        }
    }

    private suspend fun getConfirmPassResponse(): BaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).confirmPassApi(email,pass,otp)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}