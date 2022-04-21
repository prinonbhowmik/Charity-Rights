package com.charityright.bd.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.charityright.bd.Models.RegistrationModel.RegistrationBaseModel
import com.charityright.bd.Retrofit.ApiRequestInterface
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class DonationViewModel(application: Application) : AndroidViewModel(application) {

    val donationResponse: LiveData<RegistrationBaseModel?> = MutableLiveData()

    var event_id = ""
    var event_name = ""
    var event_type = ""
    var payment_date = ""
    var gift_type = ""
    var donation_type = ""
    var amount = ""
    var method_name = ""
    var tran_id = ""
    var fullname = ""
    var bussiness_name = ""
    var address = ""
    var info_verify = ""
    var password = ""
    var email = ""

    suspend fun launchApiCall() {
        donationResponse as MutableLiveData

        try {
            CustomDialog.show()
            donationResponse.value = getDonationDetails()
            CustomDialog.dismiss()
        } catch (e: Exception) {
            CustomDialog.dismiss()
            Log.wtf("DonationViewModel", e.message)
        }
    }

    private suspend fun getDonationDetails(): RegistrationBaseModel {
        val response =
            ApiRequestInterface(getApplication<Application>().applicationContext).saveDonation(
                "Bearer " + CustomSharedPref.read(
                    "Token",
                    ""
                ),
                event_id,
                event_name,
                event_type,
                payment_date,
                gift_type,
                donation_type,
                amount,
                method_name,
                tran_id,
                fullname,
                bussiness_name,
                address,
                info_verify,
                password,
                email
            )

        return withContext(Dispatchers.IO) {
            response.await()
        }
    }

}