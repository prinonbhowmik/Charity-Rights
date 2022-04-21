package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.StudentDetailsModel.StudentDetailsBaseResponse
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class StudentDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val studentDetailsResponse: LiveData<StudentDetailsBaseResponse?> = MutableLiveData()

    var id = ""

    suspend fun launchApiCall(){
        studentDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            studentDetailsResponse.value = getStudentDetails()
            println(getStudentDetails())
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("StudentDetailsViewModel",e.message)
        }
    }

    private suspend fun getStudentDetails(): StudentDetailsBaseResponse {
        val response = ApiRequestInterface(getApplication<Application>().applicationContext).getStudentDetails(id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}