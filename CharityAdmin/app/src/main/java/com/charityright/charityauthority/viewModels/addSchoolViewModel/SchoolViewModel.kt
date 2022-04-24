package com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.classList.ClassListBaseResponse
import com.charityright.charityauthority.model.schoolReport.SchoolReportBaseModel
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val imagePath = MutableLiveData<String>()
    val schoolDetailsResponse: LiveData<ClassListBaseResponse?> = MutableLiveData()
    val schoolVisitReportResponse: LiveData<SchoolReportBaseModel?> = MutableLiveData()

    var id = ""

    fun setImagePath(path: String){
        imagePath.value = path
    }

    fun getImagePath(): LiveData<String> {
        return imagePath
    }

    suspend fun launchSchoolDetailsApiCall(){
        schoolDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            schoolDetailsResponse.value = getSchoolDetailsResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ClassListViewModel",e.message)
        }
    }

    suspend fun launchSchoolVisitReport(){
        schoolVisitReportResponse as MutableLiveData

        try {
            CustomDialog.show()
            schoolVisitReportResponse.value = getSchoolReportResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ClassListViewModel",e.message)
        }
    }

    private suspend fun getSchoolReportResponse(): SchoolReportBaseModel {

        val response = AdminRequestInterface(getApplication<Application>().applicationContext).schoolReport("Bearer "+ CustomSharedPref.read("TOKEN",""),id)

        return withContext(Dispatchers.IO){
            response.await()
        }

    }


    private suspend fun getSchoolDetailsResponse(): ClassListBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).schoolDetailsList("Bearer "+ CustomSharedPref.read("TOKEN",""),id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}