package com.charityright.charityauthority.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.allAuditReport.AllAuditReportBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class AuditorReportListViewModel(application: Application) : AndroidViewModel(application) {

    val reportListResponse: LiveData<AllAuditReportBaseResponse?> = MutableLiveData()

    suspend fun launchApiCall(id: String) {
        reportListResponse as MutableLiveData

        try {
            CustomDialog.show()
            reportListResponse.value = getAuditorListResponse(id)
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("AuditorReportListViewModel",e.message)
        }
    }

    private suspend fun getAuditorListResponse(id: String): AllAuditReportBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getAuditorReportList("Bearer "+ CustomSharedPref.read("TOKEN",""),id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}