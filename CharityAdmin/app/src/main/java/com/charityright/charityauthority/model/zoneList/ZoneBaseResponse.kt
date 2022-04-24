package com.charityright.charityauthority.model.admin.zoneList

data class ZoneBaseResponse(
    var data: List<ZoneData?>,
    var message: String,
    var response_status: String
)