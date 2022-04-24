package com.charityright.charityauthority.viewModels.adminViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.filteredStudentList.FilteredStudentList
import com.charityright.charityauthority.model.admin.schoolWiseClass.SchoolWiseClassList
import com.charityright.charityauthority.model.admin.zoneWiseSchool.ZoneWiseSchoolList
import com.charityright.charityauthority.retrofit.AdminRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class TotalStudentViewModel(application: Application) : AndroidViewModel(application) {

    val schoolListResponse: LiveData<ZoneWiseSchoolList?> = MutableLiveData()
    val classListResponse: LiveData<SchoolWiseClassList?> = MutableLiveData()
    val studentListResponse: LiveData<FilteredStudentList?> = MutableLiveData()

    var zone_id = ""
    var school_id = ""
    var class_id = ""

    suspend fun launchSchoolListApiCall(){
        schoolListResponse as MutableLiveData

        try {
            schoolListResponse.value = getZoneWiseSchoolListResponse()
        }catch (e: Exception){
            Log.wtf("TotalStudentViewModel School",e.message)
        }
    }
    suspend fun launchClassListApiCall(){
        classListResponse as MutableLiveData
        try {

            val data: SchoolWiseClassList = getSchoolWiseClassListResponse()
            if (data.data.isNotEmpty()){
                classListResponse.value = data
            }else{
                classListResponse.value = SchoolWiseClassList(emptyList(),"","")
            }

        }catch (e: Exception){
            Log.wtf("TotalStudentViewModel Class",e.message)
        }
    }
    suspend fun launchStudentListApiCall(){
        studentListResponse as MutableLiveData

        try {
            CustomDialog.show()
            studentListResponse.value = getStudentListResponse()
            CustomDialog.dismiss()
        }catch (e: Exception){
            CustomDialog.dismiss()
            Log.wtf("TotalStudentViewModel Student",e.message)
        }
    }

    private suspend fun getStudentListResponse(): FilteredStudentList {
        println("getStudentListResponse Zone Id: $zone_id")
        println("getStudentListResponse School Id: $school_id")
        println("getStudentListResponse Class Id: $class_id")
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getFilteredStudentList("Bearer "+ CustomSharedPref.read("TOKEN",""),zone_id,school_id,class_id)

        return  withContext(Dispatchers.IO){
            response.await()
        }
    }
    private suspend fun getSchoolWiseClassListResponse(): SchoolWiseClassList {
        println("getSchoolWiseClassListResponse SchoolId $school_id")
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getSchoolWiseClass("Bearer "+ CustomSharedPref.read("TOKEN",""),school_id)

        return  withContext(Dispatchers.IO){
            response.await()
        }
    }
    private suspend fun getZoneWiseSchoolListResponse(): ZoneWiseSchoolList {
        println("getZoneWiseSchoolListResponse ClassID $zone_id")
        val response = AdminRequestInterface(getApplication<Application>().applicationContext).getZoneWiseSchool("Bearer "+ CustomSharedPref.read("TOKEN",""),zone_id)

        return withContext(Dispatchers.IO){
            response.await()
        }
    }

}