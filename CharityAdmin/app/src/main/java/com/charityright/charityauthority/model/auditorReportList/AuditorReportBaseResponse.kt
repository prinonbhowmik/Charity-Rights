package com.charityright.charityauthority.model.admin.auditorReportList

data class AuditorReportBaseResponse(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)