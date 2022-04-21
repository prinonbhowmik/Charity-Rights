package com.charityright.bd.Models.SchoolDetails

data class Data(
    var class_list: List<Class?>,
    var details: String?,
    var id: String?,
    var images: List<Image?>,
    var school_name: String?,
    var zone: String?
)