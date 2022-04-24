package com.charityright.charityauthority.auditor.viewModel

import android.app.Application
import android.app.ProgressDialog
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.charityauthority.model.admin.BaseResponse
import com.charityright.charityauthority.retrofit.AuditorRequestInterface
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.io.File
import java.lang.Exception

class CircumstanceViewModel(application: Application) : AndroidViewModel(application) {

    val addCircumstanceResponse: LiveData<BaseResponse?> = MutableLiveData()

    suspend fun launchImageUploadApiCall(
        location: String,
        date: String,
        incident: String,
        affected: String,
        situation: String,
        support: String,
        remarks: String
    ) {
        addCircumstanceResponse as MutableLiveData

        CustomDialog.show()

        try {

            val updateLocation: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), location)
            val updateDate: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), date)
            val updateIncident: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), incident)
            val updateAffected: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), affected)
            val updateSituation: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), situation)
            val updateSupport: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), support)
            val updateRemarks: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), remarks)

            addCircumstanceResponse.value = getAddCircumstanceResponse(
                updateLocation,
                updateDate,
                updateIncident,
                updateAffected,
                updateSituation,
                updateSupport,
                updateRemarks
            )
            CustomDialog.dismiss()
        } catch (e: Exception) {
            CustomDialog.dismiss()
            Log.wtf("AddBeneficiaryViewModel ImageUpload", e.message)
        }
    }

    private suspend fun getAddCircumstanceResponse(
        updateLocation: RequestBody,
        updateDate: RequestBody,
        updateIncident: RequestBody,
        updateAffected: RequestBody,
        updateSituation: RequestBody,
        updateSupport: RequestBody,
        updateRemarks: RequestBody
    ): BaseResponse {
        val response =
            AuditorRequestInterface(getApplication<Application>().applicationContext).addCircumstance(
                "Bearer " + CustomSharedPref.read("TOKEN", ""),
                updateLocation,
                updateDate,
                updateIncident,
                updateAffected,
                updateSituation,
                updateSupport,
                updateRemarks
            )

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

}