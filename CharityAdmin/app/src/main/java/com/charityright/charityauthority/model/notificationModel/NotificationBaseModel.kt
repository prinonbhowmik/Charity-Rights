package com.charityright.charityauthority.model.notificationModel

data class NotificationBaseModel(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)