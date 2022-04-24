package com.charityright.charityauthority.model.admin.allAuditReport

data class AllAuditReportBaseResponse(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)