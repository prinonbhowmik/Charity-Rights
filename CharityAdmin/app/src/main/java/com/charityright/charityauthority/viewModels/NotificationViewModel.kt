package com.charityright.charityauthority.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.notificationModel.NotificationBaseModel
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
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
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getNotificationList("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}