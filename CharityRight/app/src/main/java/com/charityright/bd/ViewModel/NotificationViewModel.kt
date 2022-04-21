package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.NotificationModel.NotificationBaseModel
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    val notificationBaseResponse: LiveData<NotificationBaseModel?> = MutableLiveData()

    suspend fun launchApiCall(){
        notificationBaseResponse as MutableLiveData

        try {
            CustomDialog.show()
            notificationBaseResponse.value = getNotificationList()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("NotificationViewModel",e.message)
        }
    }

    private suspend fun getNotificationList(): NotificationBaseModel {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).getNotificationList("Bearer "+ CustomSharedPref.read("Token",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}