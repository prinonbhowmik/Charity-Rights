package com.charityright.charityauthority.auditor.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.auditor.model.AssignedReport.AssignedReportBaseModel
import com.charityright.charityauthority.auditor.model.SchoolListBaseResponse.SchoolListBaseResponse
import com.charityright.charityauthority.retrofit.AuditorRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class AuditorActivityViewModel(application: Application) : AndroidViewModel(application) {

    val assignedReportResponse: LiveData<AssignedReportBaseModel?> = MutableLiveData()
    val allSchoolListResponse: LiveData<SchoolListBaseResponse?> = MutableLiveData()

    var lat = ""
    var lon = ""

    suspend fun launchApiCall(){
        assignedReportResponse as MutableLiveData
        allSchoolListResponse as MutableLiveData

        try {
            CustomDialog.show()
            assignedReportResponse.value = getAssignedReportResponse()
            allSchoolListResponse.value = getAllSchoolListResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("AuditorActivityViewModel",e.message)
        }
    }

    private suspend fun getAllSchoolListResponse(): SchoolListBaseResponse {
        val response = AuditorRequestInterface(getApplication<Application>().applicationContext).schoolList("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

    private suspend fun getAssignedReportResponse(): AssignedReportBaseModel {
        val response = AuditorRequestInterface(getApplication<Application>().applicationContext).assignedReportList("Bearer "+ CustomSharedPref.read("TOKEN",""))

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}