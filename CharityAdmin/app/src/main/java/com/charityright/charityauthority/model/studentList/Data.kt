package com.charityright.charityauthority.model.admin.studentList

data class Data(
    var age: String?,
    var health_status: List<HealthStatus?>,
    var image_url: String?,
    var speech: String?,
    var student_details: StudentDetails?
)