package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.ClassDetailsModel.ClassDetailsBaseResponse
import com.charityright.bd.Models.SchoolDetails.SchoolDetailsBaseResponse
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class ClassDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val classDetailsResponse: LiveData<ClassDetailsBaseResponse?> = MutableLiveData()

    var id = ""

    suspend fun launchApiCall(){
        classDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            classDetailsResponse.value = getMealsCampaignDetails()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("ClassDetailsViewModel",e.message)
        }
    }

    private suspend fun getMealsCampaignDetails(): ClassDetailsBaseResponse {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).getClassDetails(id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}