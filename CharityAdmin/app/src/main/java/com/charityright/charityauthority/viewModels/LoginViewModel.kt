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

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val loginResponse: LiveData<BaseResponse?> = MutableLiveData()

    var email = ""
    var password = ""
    var userType = ""
    var deviceToken = ""

    suspend fun launchApiCall(){
        loginResponse as MutableLiveData

        try {
            CustomDialog.show()
            loginResponse.value = getLoginResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("LoginViewModel",e.message)
        }
    }

    private suspend fun getLoginResponse(): BaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).loginApi(email,password,userType,deviceToken)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}