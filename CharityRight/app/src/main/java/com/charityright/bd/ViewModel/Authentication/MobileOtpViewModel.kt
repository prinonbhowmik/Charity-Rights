package com.charityright.bd.ViewModel.Authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.LoginModel.LoginBaseResponse
import com.charityright.bd.Models.RegistrationModel.RegistrationBaseModel
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class MobileOtpViewModel(application: Application) : AndroidViewModel(application) {

    val OtpResponse: LiveData<LoginBaseResponse?> = MutableLiveData()

    var mobile = ""
    var otp = ""


    suspend fun launchApiCall(){
        OtpResponse as MutableLiveData

        try {
            CustomDialog.show()
            OtpResponse.value = getOtpResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("MobileRegistrationOTPViewModel",e.message)
        }
    }

    private suspend fun getOtpResponse(): LoginBaseResponse {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).verifyMobileOtp(mobile,otp)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}