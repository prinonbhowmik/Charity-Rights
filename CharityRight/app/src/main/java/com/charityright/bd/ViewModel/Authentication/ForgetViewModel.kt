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

class ForgetViewModel(application: Application) : AndroidViewModel(application) {

    val forgetResponse: LiveData<RegistrationBaseModel?> = MutableLiveData()

    var email = ""


    suspend fun launchApiCall(){
        forgetResponse as MutableLiveData

        try {
            CustomDialog.show()
            forgetResponse.value = getForgotResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ForgotViewModel",e.message)
        }
    }

    private suspend fun getForgotResponse(): RegistrationBaseModel {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).forgotApi(email)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}