package com.charityright.bd.Models.StudentDetailsModel

import com.google.gson.annotations.SerializedName

data class StudentDetails(
    var address: String?,
    var age: String?,
    var bio: String?,
    var bmi: String?,
    @SerializedName("class")
    var class_name: String?,
    var food_like: String?,
    var growth_chart: Any?,
    var height: String?,
    var hobby: String?,
    var id: String?,
    var image: String?,
    var interest: String?,
    var local_gurdian: String?,
    var nutrition: Any?,
    var school: String?,
    var speech: String?,
    var std_name: String?,
    var weight: String?
)