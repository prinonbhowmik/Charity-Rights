package com.charityright.charityauthority.model.admin.campaignList

data class campaignBaseResponse(
    var data: List<campaignData?>,
    var message: String?,
    var response_status: String?
)