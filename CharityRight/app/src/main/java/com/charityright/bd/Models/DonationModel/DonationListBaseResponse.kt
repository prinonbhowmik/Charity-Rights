package com.charityright.bd.Models.DonationModel

data class DonationListBaseResponse(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)