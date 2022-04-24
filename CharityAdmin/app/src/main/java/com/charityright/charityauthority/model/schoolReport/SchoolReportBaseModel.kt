package com.charityright.charityauthority.model.schoolReport

data class SchoolReportBaseModel(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)