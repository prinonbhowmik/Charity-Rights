package com.charityright.bd.Models.StudentDetailsModel

data class Data(
    var age: String?,
    var health_status: List<HealthStatus?>,
    var image_url: String?,
    var speech: String?,
    var student_details: StudentDetails?
)