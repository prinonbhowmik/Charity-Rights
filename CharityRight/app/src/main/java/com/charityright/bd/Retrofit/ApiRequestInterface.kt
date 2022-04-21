package com.charityright.bd.Retrofit

import android.content.Context
import com.charityright.bd.Models.CampaignListModel.CampaignListBaseModel
import com.charityright.bd.Models.ClassDetailsModel.ClassDetailsBaseResponse
import com.charityright.bd.Models.Config.ConfigBaseModel
import com.charityright.bd.Models.DonationModel.DonationListBaseResponse
import com.charityright.bd.Models.HistoryModelClass.HistoryBaseModelClass
import com.charityright.bd.Models.LoginModel.LoginBaseResponse
import com.charityright.bd.Models.MealsDetailsModel.MealsDetailsBaseResponse
import com.charityright.bd.Models.NotificationModel.NotificationBaseModel
import com.charityright.bd.Models.RegistrationModel.RegistrationBaseModel
import com.charityright.bd.Models.SchoolDetails.SchoolDetailsBaseResponse
import com.charityright.bd.Models.SchoolListBaseModel.SchoolListBaseResponse
import com.charityright.bd.Models.StudentDetailsModel.StudentDetailsBaseResponse
import com.charityright.bd.Models.UserInfo.ProfileInfoBaseResponse
import com.charityright.bd.Models.ZoneList.ZoneListBaseResponse
import com.charityright.bd.R
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiRequestInterface {

    @FormUrlEncoded
    @POST("api/donar_login")
    fun loginApi(
        @Field("mobile") email: String,
        @Field("password") password: String,
        @Field("user_type") user_type: String,
        @Field("device_token") device_token: String
    ): Call<LoginBaseResponse>

    @FormUrlEncoded
    @POST("api/donar_register")
    fun registrationApi(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String
    ): Call<RegistrationBaseModel>

    @FormUrlEncoded
    @POST("api/forget_password")
    fun forgotApi(
        @Field("email") email: String,
    ): Call<RegistrationBaseModel>

    @FormUrlEncoded
    @POST("api/confirm_password")
    fun confirmPassApi(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("otp") otp: String
    ): Call<RegistrationBaseModel>

    @FormUrlEncoded
    @POST("api/donar_register_phone")
    fun loginWithMobile(
        @Field("mobile") mobile: String
    ): Call<RegistrationBaseModel>

    @FormUrlEncoded
    @POST("api/donar_login_otp")
    fun verifyMobileOtp(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String,
    ): Call<LoginBaseResponse>

    @GET("api/donar_profile_details")
    fun getUserInfo(
        @Header("Authorization") token: String
    ): Call<ProfileInfoBaseResponse>

    @GET("api/ssl_live")
    fun getConfig(): Call<ConfigBaseModel>

    @GET("api/donation_history")
    fun getUserDonationHistory(
        @Header("Authorization") token: String
    ): Call<HistoryBaseModelClass>

    @FormUrlEncoded
    @POST("api/donar_profile_update")
    fun updateUserInfo(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("address") address: String,
        @Field("post") post: String,
        @Field("country") country: String,
        @Field("password") password: String
    ): Call<RegistrationBaseModel>

    @GET("api/campaign_list_donar")
    fun getCampaignList(): Call<CampaignListBaseModel>

    @GET("api/zone_list_donor")
    fun getZoneList(): Call<ZoneListBaseResponse>

    @FormUrlEncoded
    @POST("api/school_list_donar")
    fun getSchoolList(@Field("zone_id") zone_id: String): Call<SchoolListBaseResponse>

    @FormUrlEncoded
    @POST("api/campaign_details_donor")
    fun getMealsDetails(
        @Field("id") id: String
    ): Call<MealsDetailsBaseResponse>

    @FormUrlEncoded
    @POST("api/school_details_donor")
    fun getSchoolDetails(
        @Field("school_id") school_id: String
    ): Call<SchoolDetailsBaseResponse>

    @FormUrlEncoded
    @POST("api/class_details_donor")
    fun getClassDetails(
        @Field("class_id") class_id: String
    ): Call<ClassDetailsBaseResponse>

    @FormUrlEncoded
    @POST("api/student_details_donor")
    fun getStudentDetails(
        @Field("student_id") student_id: String
    ): Call<StudentDetailsBaseResponse>

    @GET("api/donation_donor")
    fun getDonationList(): Call<DonationListBaseResponse>

    @GET("api/notification")
    fun getNotificationList(
        @Header("Authorization") token: String
    ): Call<NotificationBaseModel>

    @FormUrlEncoded
    @POST("api/save_donation")
    fun saveDonation(
        @Header("Authorization") token: String,
        @Field("event_id") event_id: String,
        @Field("event_name") event_name: String,
        @Field("event_type") event_type: String,
        @Field("payment_date") payment_date: String,
        @Field("gift_type") gift_type: String,
        @Field("donation_type") donation_type: String,
        @Field("amount") amount: String,
        @Field("method_name") method_name: String,
        @Field("tran_id") tran_id: String,
        @Field("fullname") fullname: String,
        @Field("bussiness_name") bussiness_name: String,
        @Field("address") address: String,
        @Field("info_verify") info_verify: String,
        @Field("password") password: String,
        @Field("email") email: String,
    ): Call<RegistrationBaseModel>



    companion object {

        val httpClient = OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build()

        operator fun invoke(applicationContext: Context): ApiRequestInterface {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(applicationContext.resources.getString(R.string.base_url))
                .build()
                .create(ApiRequestInterface::class.java)
        }
    }
}