package com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.classDetails.classDetailsBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class ClassViewModel(application: Application) : AndroidViewModel(application) {

    val classDetailsResponse: LiveData<classDetailsBaseResponse?> = MutableLiveData()

    var id = ""

    suspend fun launchClassDetailsApiCall(){
        classDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            classDetailsResponse.value = getClassDetailsResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ClassListViewModel",e.message)
        }
    }


    private suspend fun getClassDetailsResponse(): classDetailsBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).classDetails("Bearer "+ CustomSharedPref.read("TOKEN",""),id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}