package com.charityright.bd.ViewModel.Authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.RegistrationModel.RegistrationBaseModel
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class ConfirmPassViewModel(application: Application) : AndroidViewModel(application) {

    val resetResponse: LiveData<RegistrationBaseModel?> = MutableLiveData()

    var email = ""
    var otp = ""
    var pass = ""


    suspend fun launchApiCall(){
        resetResponse as MutableLiveData

        try {
            CustomDialog.show()
            resetResponse.value = getResetResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ConfirmPassViewModel",e.message)
        }
    }

    private suspend fun getResetResponse(): RegistrationBaseModel {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).confirmPassApi(email,pass,otp)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}