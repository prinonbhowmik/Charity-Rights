package com.charityright.charityauthority.auditor.model.AssignedReport

data class AssignedReportBaseModel(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)