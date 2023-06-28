package no.sintef.fiskinfo.model.sprice

import no.sintef.fiskinfo.model.fishingfacility.GeoJsonGeometry
import java.util.*

data class IcingReport(

    var geometry: GeoJsonGeometry,
    var reportingTime: Date
)
