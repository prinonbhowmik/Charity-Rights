package com.charityright.charityauthority.retrofit

import android.content.Context
import com.charityright.charityauthority.R
import com.charityright.charityauthority.model.admin.BaseResponse
import com.charityright.charityauthority.model.admin.addSchool.AddSchoolBaseResponse
import com.charityright.charityauthority.model.admin.addSchool.AddSchoolImageBaseResponse
import com.charityright.charityauthority.model.admin.allAuditReport.AllAuditReportBaseResponse
import com.charityright.charityauthority.model.admin.auditorReportList.AuditorReportBaseResponse
import com.charityright.charityauthority.model.admin.campaignDetails.campaignDetailsBaseReponse
import com.charityright.charityauthority.model.admin.campaignList.campaignBaseResponse
import com.charityright.charityauthority.model.admin.classDetails.classDetailsBaseResponse
import com.charityright.charityauthority.model.admin.classList.ClassListBaseResponse
import com.charityright.charityauthority.model.admin.dashboard.dashboardBaseResponse
import com.charityright.charityauthority.model.admin.donationList.DonationListBaseResponse
import com.charityright.charityauthority.model.admin.donorDetails.DonorDetailsBaseResponse
import com.charityright.charityauthority.model.admin.donorList.DonorListBaseModel
import com.charityright.charityauthority.model.admin.filteredStudentList.FilteredStudentList
import com.charityright.charityauthority.model.admin.schoolList.SchoolListBaseResponse
import com.charityright.charityauthority.model.admin.schoolWiseClass.SchoolWiseClassList
import com.charityright.charityauthority.model.admin.studentList.StudentDetailsBaseResponse
import com.charityright.charityauthority.model.admin.zoneList.ZoneBaseResponse
import com.charityright.charityauthority.model.admin.zoneWiseSchool.ZoneWiseSchoolList
import com.charityright.charityauthority.model.notificationModel.NotificationBaseModel
import com.charityright.charityauthority.model.schoolReport.SchoolReportBaseModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AdminRequestInterface {

    @FormUrlEncoded
    @POST("api/login")
    fun loginApi(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") user_type: String,
        @Field("device_token") device_token: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("api/forget_password")
    fun forgetApi(@Field("email") email: String): Call<BaseResponse>

    @FormUrlEncoded
    @POST("api/confirm_password")
    fun confirmPassApi(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("otp") otp: String
    ): Call<BaseResponse>

    @GET("api/dashboard")
    fun dashBoardAPi(@Header("Authorization") token: String): Call<dashboardBaseResponse>

    @GET("api/zone_list")
    fun zoneListApi(@Header("Authorization") token: String): Call<ZoneBaseResponse>

    @GET("api/zone_list_ws")
    fun addZoneListApi(@Header("Authorization") token: String): Call<ZoneBaseResponse>

    @FormUrlEncoded
    @POST("api/campaign_list")
    fun campaignList(
        @Header("Authorization") token: String,
        @Field("zone_id") zone_id: String
    ): Call<campaignBaseResponse>

    @FormUrlEncoded
    @POST("api/campaign_details")
    fun campaignDetails(
        @Header("Authorization") token: String,
        @Field("id") id: String
    ): Call<campaignDetailsBaseReponse>

    @FormUrlEncoded
    @POST("api/add_campaign")
    fun addCampaignData(
        @Header("Authorization") token: String,
        @Field("zone_id") zone_id: String,
        @Field("title") title: String,
        @Field("date") date: String,
        @Field("location") location: String,
        @Field("details") details: String,
        @Field("objective") objective: String,
        @Field("beneficiary") beneficiary: String,
        @Field("m_name") m_name: String,
        @Field("m_contact") m_contact: String,
        @Field("images") images: String,
    ): Call<campaignDetailsBaseReponse>

    @FormUrlEncoded
    @POST("api/school_list")
    fun schoolList(
        @Header("Authorization") token: String,
        @Field("zone_id") id: String
    ): Call<SchoolListBaseResponse>

    @FormUrlEncoded
    @POST("api/school_details")
    fun schoolDetailsList(
        @Header("Authorization") token: String,
        @Field("school_id") id: String
    ): Call<ClassListBaseResponse>

    @FormUrlEncoded
    @POST("api/avg_std")
    fun schoolReport(
        @Header("Authorization") token: String,
        @Field("school_id") id: String
    ): Call<SchoolReportBaseModel>

    @FormUrlEncoded
    @POST("api/class_details")
    fun classDetails(
        @Header("Authorization") token: String,
        @Field("class_id") id: String
    ): Call<classDetailsBaseResponse>

    @FormUrlEncoded
    @POST("api/student_details")
    fun studentDetails(
        @Header("Authorization") token: String,
        @Field("student_id") id: String
    ): Call<StudentDetailsBaseResponse>

    @Multipart
    @POST("api/image_upload_school")
    fun addSchoolImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
    ): Call<AddSchoolImageBaseResponse>

    @FormUrlEncoded
    @POST("api/add_school")
    fun addSchool(
        @Header("Authorization") token: String,
        @Field("zone_id") zone_id: String,
        @Field("school_name") school_name: String,
        @Field("address") address: String,
        @Field("details") details: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("class_list") class_list: String,
        @Field("images") images: String,
    ): Call<AddSchoolBaseResponse>

    @GET("api/donar_list")
    fun getDonorList(@Header("Authorization") token: String): Call<DonorListBaseModel>

    @GET("api/admin_notification")
    fun getNotificationList(@Header("Authorization") token: String): Call<NotificationBaseModel>

    @GET("api/auditor_report_status")
    fun getAuditorReportList(@Header("Authorization") token: String): Call<AuditorReportBaseResponse>

    @FormUrlEncoded
    @POST("api/auditor_report_list")
    fun getAllAuditReportList(@Header("Authorization") token: String, @Field("start_date") start_date: String, @Field("end_date") end_date: String): Call<AllAuditReportBaseResponse>

    @FormUrlEncoded
    @POST("api/get_auditor_report_list")
    fun getAuditorReportList(@Header("Authorization") token: String, @Field("auditor_id") auditor_id: String): Call<AllAuditReportBaseResponse>


    @FormUrlEncoded
    @POST("api/donar_details")
    fun getDonorDetails(
        @Header("Authorization") token: String,
        @Field("donar_id") id: String
    ): Call<DonorDetailsBaseResponse>

    @FormUrlEncoded
    @POST("api/donation_list")
    fun getDonationList(
        @Header("Authorization") token: String,
        @Field("start_date") start_date: String,
        @Field("end_date") end_date: String,
        @Field("event_type") event_type: String,
    ): Call<DonationListBaseResponse>

    @FormUrlEncoded
    @POST("api/school_list_dropdown")
    fun getZoneWiseSchool(
        @Header("Authorization") token: String,
        @Field("zone_id") id: String
    ): Call<ZoneWiseSchoolList>

    @FormUrlEncoded
    @POST("api/class_list")
    fun getSchoolWiseClass(
        @Header("Authorization") token: String,
        @Field("school_id") id: String
    ): Call<SchoolWiseClassList>

    @FormUrlEncoded
    @POST("api/student_list")
    fun getFilteredStudentList(
        @Header("Authorization") token: String,
        @Field("zone_id") zone_id: String,
        @Field("school_id") school_id: String,
        @Field("class_id") class_id: String
    ): Call<FilteredStudentList>


    companion object {

        val httpClient = OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.MINUTES)
            .writeTimeout(60, TimeUnit.MINUTES).build()

        operator fun invoke(applicationContext: Context): AdminRequestInterface {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl("http://192.168.1.225/charity/")
                .build()
                .create(AdminRequestInterface::class.java)
        }
    }
}