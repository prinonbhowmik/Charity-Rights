package com.charityright.charityauthority.model.admin.donorDetails

data class Data(
    var address: String?,
    var amount: String?,
    var country: String?,
    var donation_list: List<Donation?>,
    var email: String?,
    var id: String?,
    var image_url: String?,
    var mobile: String?,
    var name: String?,
    var post: String?
)