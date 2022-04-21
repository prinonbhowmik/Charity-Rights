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

class MobileSignInViewModel(application: Application) : AndroidViewModel(application) {

    val registrationResponse: LiveData<RegistrationBaseModel?> = MutableLiveData()

    var mobile = ""


    suspend fun launchApiCall(){
        registrationResponse as MutableLiveData

        try {
            CustomDialog.show()
            registrationResponse.value = getRegistrationResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("MobileRegistrationViewModel",e.message)
        }
    }

    private suspend fun getRegistrationResponse(): RegistrationBaseModel {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).loginWithMobile(mobile)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}