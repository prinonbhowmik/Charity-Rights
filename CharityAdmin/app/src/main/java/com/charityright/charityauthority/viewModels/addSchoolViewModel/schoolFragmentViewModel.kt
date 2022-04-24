package com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.schoolList.SchoolListBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class schoolFragmentViewModel(application: Application) : AndroidViewModel(application) {
    val schoolListResponse: LiveData<SchoolListBaseResponse?> = MutableLiveData()

    var zone_id = ""

    suspend fun launchSchoolApiCall(){
        schoolListResponse as MutableLiveData

        try {
            CustomDialog.show()
            schoolListResponse.value = getSchoolResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("SchoolListViewModel",e.message)
        }
    }

    private suspend fun getSchoolResponse(): SchoolListBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).schoolList("Bearer "+ CustomSharedPref.read("TOKEN",""),zone_id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }
}