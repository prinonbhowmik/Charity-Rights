package com.charityright.charityauthority.model.admin.schoolList

data class SchoolListBaseResponse(
    val data: List<Data?>,
    val message: String?,
    val response_status: String?
)