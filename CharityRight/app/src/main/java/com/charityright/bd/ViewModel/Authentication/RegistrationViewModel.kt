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

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    val registationResponse: LiveData<RegistrationBaseModel?> = MutableLiveData()

    var email = ""
    var password = ""
    var name = ""
    var phone = ""


    suspend fun launchApiCall(){
        registationResponse as MutableLiveData

        try {
            CustomDialog.show()
            registationResponse.value = getRegistrationResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("RegistrationViewModel",e.message)
        }
    }

    private suspend fun getRegistrationResponse(): RegistrationBaseModel {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).registrationApi(name,phone,email,password,password)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}