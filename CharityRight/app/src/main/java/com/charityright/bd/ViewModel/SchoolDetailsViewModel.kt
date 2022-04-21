package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.SchoolDetails.SchoolDetailsBaseResponse
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class SchoolDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val schoolDetailsResponse: LiveData<SchoolDetailsBaseResponse?> = MutableLiveData()

    var id = ""
    private val imagePath = MutableLiveData<String?>()


    fun setImagePath(path: String?){
        imagePath.value = path
    }

    fun getImagePath(): LiveData<String?> {
        return imagePath
    }

    suspend fun launchApiCall(){
        schoolDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            schoolDetailsResponse.value = getMealsCampaignDetails()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("SchoolDetailsViewModel",e.message)
        }
    }

    private suspend fun getMealsCampaignDetails(): SchoolDetailsBaseResponse {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).getSchoolDetails(id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}