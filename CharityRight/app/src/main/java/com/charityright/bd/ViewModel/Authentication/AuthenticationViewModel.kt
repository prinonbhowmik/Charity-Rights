package com.charityright.bd.ViewModel.Authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.LoginModel.LoginBaseResponse
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {

    val authenticationResponse: LiveData<LoginBaseResponse?> = MutableLiveData()

    var email = ""
    var password = ""
    var type = "3"
    var token = ""

    suspend fun launchApiCall(){
        authenticationResponse as MutableLiveData

        try {
            CustomDialog.show()
            authenticationResponse.value = getAuthenticationResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("AuthenticationViewModel",e.message)
        }
    }

    private suspend fun getAuthenticationResponse(): LoginBaseResponse {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).loginApi(email,password,type,token)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}