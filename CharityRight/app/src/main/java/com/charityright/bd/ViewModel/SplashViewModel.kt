package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.Config.ConfigBaseModel
import com.charityright.bd.Retrofit.ApiRequestInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    val configResponse: LiveData<ConfigBaseModel?> = MutableLiveData()

    suspend fun launchApiCall(){
        configResponse as MutableLiveData
        try {
            configResponse.value = getStudentDetails()
        }catch (e: Exception){
            Log.wtf("SplashViewModel Exception",e.message)
        }
    }

    private suspend fun getStudentDetails(): ConfigBaseModel {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).getConfig()

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}