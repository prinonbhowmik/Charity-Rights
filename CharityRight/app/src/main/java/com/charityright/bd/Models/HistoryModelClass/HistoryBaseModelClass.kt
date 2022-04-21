package com.charityright.bd.Models.HistoryModelClass

data class HistoryBaseModelClass(
    var donar_list: List<Donar?>,
    var message: String?,
    var response_status: String?
)