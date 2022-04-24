package com.charityright.charityauthority.auditor.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.auditor.model.Notification.AuditorNotificationBaseResponse
import com.charityright.charityauthority.retrofit.AuditorRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class AuditorNotificationViewModel(application: Application) : AndroidViewModel(application) {

    val notificationResponse: LiveData<AuditorNotificationBaseResponse?> = MutableLiveData()


    suspend fun launchApiCall(){
        notificationResponse as MutableLiveData

        try {
            CustomDialog.show()
            notificationResponse.value = getNotificationResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("AuditorNotificationViewModel",e.message)
        }
    }


    private suspend fun getNotificationResponse(): AuditorNotificationBaseResponse {
        val response = AuditorRequestInterface(getApplication<Application>().applicationContext).getNotification("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}