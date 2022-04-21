package com.charityright.bd.ViewModel.Authentication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.RegistrationModel.RegistrationBaseModel
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val profileUpdateResponse: LiveData<RegistrationBaseModel?> = MutableLiveData()

    var name = ""
    var email = ""
    var address = ""
    var post = ""
    var country = ""
    var password = ""


    suspend fun launchApiCall(){
        profileUpdateResponse as MutableLiveData

        try {
            CustomDialog.show()
            profileUpdateResponse.value = getProfileUpdateResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ProfileViewModel",e.message)
        }
    }

    private suspend fun getProfileUpdateResponse(): RegistrationBaseModel {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).updateUserInfo("Bearer "+CustomSharedPref.read("Token",""),name,email,address,post,country,password)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}