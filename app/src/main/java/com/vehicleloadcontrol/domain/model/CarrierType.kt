package com.vehicleloadcontrol.domain.model

enum class CarrierType(val displayName: String, val url: String) {
    CMA_CGM("CMA CGM", "https://www.cma-cgm.com/"),
    ZIM("ZIM", "https://www.zim.com/"),
    MAERSK("Maersk", "https://www.maersk.com/"),
    OTHER("Otro", "")
}
