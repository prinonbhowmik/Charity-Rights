package com.charityright.bd.Models.SchoolListBaseModel

data class SchoolListBaseResponse(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)