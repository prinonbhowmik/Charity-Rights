package com.charityright.bd.Models.NotificationModel

data class NotificationBaseModel(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)