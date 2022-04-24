package com.charityright.charityauthority.auditor.model.Notification

data class AuditorNotificationBaseResponse(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)