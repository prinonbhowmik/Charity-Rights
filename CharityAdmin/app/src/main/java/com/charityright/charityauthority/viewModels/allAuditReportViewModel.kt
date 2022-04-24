package com.charityright.charityauthority.viewModels.adminViewModel

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

class allAuditReportViewModel(application: Application) : AndroidViewModel(application) {
    val reportListResponse: LiveData<AllAuditReportBaseResponse?> = MutableLiveData()

    var startDate = ""
    var endDate = ""

    suspend fun launchApiCall(){
        reportListResponse as MutableLiveData

        try {
            CustomDialog.show()
            reportListResponse.value = getAuditorListResponse(startDate,endDate)
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("AllAuditorReportListViewModel",e.message)
        }
    }

    private suspend fun getAuditorListResponse(startDate: String, endDate: String): AllAuditReportBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getAllAuditReportList("Bearer "+ CustomSharedPref.read("TOKEN",""),startDate,endDate)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}