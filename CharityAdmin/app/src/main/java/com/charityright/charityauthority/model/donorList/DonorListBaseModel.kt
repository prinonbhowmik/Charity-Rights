package com.charityright.charityauthority.model.admin.donorList

data class DonorListBaseModel(
    var donar_list: List<Donar?>,
    var message: String?,
    var response_status: String?,
    var total_donation: String?
)