package com.charityright.charityauthority.model.admin.classList

data class Data(
    var class_list: List<Class?>,
    var details: String?,
    var id: String?,
    var images: List<Image?>,
    var school_name: String?,
    var avg_bmi: String?
)