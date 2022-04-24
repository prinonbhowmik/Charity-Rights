package com.charityright.charityauthority.model.admin.donationList

data class DonationListBaseResponse(
    var data: List<Data?>,
    var message: String?,
    var response_status: String?
)