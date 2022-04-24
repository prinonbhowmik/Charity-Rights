package com.charityright.charityauthority.retrofit

import android.content.Context
import com.charityright.charityauthority.R
import com.charityright.charityauthority.auditor.model.AssignedReport.AssignedReportBaseModel
import com.charityright.charityauthority.auditor.model.ImageUploadModel
import com.charityright.charityauthority.auditor.model.Notification.AuditorNotificationBaseResponse
import com.charityright.charityauthority.auditor.model.SchoolListBaseResponse.SchoolListBaseResponse
import com.charityright.charityauthority.model.admin.BaseResponse
import com.charityright.charityauthority.model.admin.addSchool.AddSchoolBaseResponse
import com.charityright.charityauthority.model.admin.addSchool.AddSchoolImageBaseResponse
import com.charityright.charityauthority.model.admin.zoneList.ZoneBaseResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AuditorRequestInterface {

    @FormUrlEncoded
    @POST("api/add_beneficiary_remarks")
    fun addBeneficiary(
        @Header("Authorization") token: String,
        @Field("project_title") project_title: String,
        @Field("name") name: String,
        @Field("age") age: String,
        @Field("location") location: String,
        @Field("additional_info") additional_info: String,
        @Field("remarks") remarks: String,
        @Field("images") images: String
    ): Call<BaseResponse>

    @Multipart
    @POST("api/image_upload")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
    ): Call<ImageUploadModel>

    @Multipart
    @POST("api/add_circumstance")
    fun addCircumstance(
        @Header("Authorization") token: String,
        @Part("location") location: RequestBody,
        @Part("date") date: RequestBody,
        @Part("incident") incident: RequestBody,
        @Part("affected_area") affected_area: RequestBody,
        @Part("situation_beneficiaries") situation_beneficiaries: RequestBody,
        @Part("support_req") support_req: RequestBody,
        @Part("remarks") remarks: RequestBody,
    ): Call<BaseResponse>

    @GET("api/task_list")
    fun assignedReportList(@Header("Authorization") token: String): Call<AssignedReportBaseModel>

    @GET("api/notification_auditor")
    fun getNotification(@Header("Authorization") token: String): Call<AuditorNotificationBaseResponse>

    @GET("api/school_list_auditor")
    fun schoolList(@Header("Authorization") token: String): Call<SchoolListBaseResponse>

    @Multipart
    @POST("api/add_site_visit")
    fun addFieldVisit(
        @Header("Authorization") token: String,
        @Part("zone_id") zone_id: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part("school_time") school_time: RequestBody,
        @Part("date") date: RequestBody,
        @Part("total_std") total_std: RequestBody,
        @Part("present_std") present_std: RequestBody,
        @Part("absent_std") absent_std: RequestBody,
        @Part("reason_absent") reason_absent: RequestBody,
        @Part("avg_age_std") avg_age_std: RequestBody,
        @Part("edu_type") edu_type: RequestBody,
        @Part("perfomance") perfomance: RequestBody,
        @Part("efficiency_other") efficiency_other: RequestBody,
        @Part("approx_meal_price") approx_meal_price: RequestBody,
        @Part("meal_quality") meal_quality: RequestBody,
        @Part("food_list") food_list: RequestBody,
        @Part("total_teacher") total_teacher: RequestBody,
        @Part("present_teacher") present_teacher: RequestBody,
        @Part("absent_teacher") absent_teacher: RequestBody,
        @Part("suggestion") suggestion: RequestBody,
        @Part("nutrition_list") nutrition_list: RequestBody,
        @Part("complaints_list") complaints_list: RequestBody,
        @Part("remarks") remarks: RequestBody,
        @Part("images") images: RequestBody,
        @Part("task_id") task_id: RequestBody,
        @Part("class_list") class_list: RequestBody
    ): Call<BaseResponse>

    companion object {

        val httpClient = OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build()

        operator fun invoke(applicationContext: Context): AuditorRequestInterface {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                //.baseUrl(applicationContext.resources.getString(R.string.base_url))
                .baseUrl(
                   "http://192.168.1.225/charity/")
                .build()
                .create(AuditorRequestInterface::class.java)
        }
    }
}