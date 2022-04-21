package com.charityright.bd.Models.ZoneList

data class ZoneListBaseResponse(
    val data: List<Data?>,
    val message: String?,
    val response_status: String?
)