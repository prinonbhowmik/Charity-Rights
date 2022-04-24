package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.studentList.StudentDetailsBaseResponse
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class StudentDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val studentDetailsResponse: LiveData<StudentDetailsBaseResponse?> = MutableLiveData()

    var student_id = ""

    suspend fun launchStudentDetailsApiCall(){
        studentDetailsResponse as MutableLiveData

        try {
            CustomDialog.show()
            studentDetailsResponse.value = getStudentDetailsResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("StudentDetailsViewModel",e.message)
        }
    }


    private suspend fun getStudentDetailsResponse(): StudentDetailsBaseResponse {
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).studentDetails("Bearer "+ CustomSharedPref.read("TOKEN",""),student_id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}