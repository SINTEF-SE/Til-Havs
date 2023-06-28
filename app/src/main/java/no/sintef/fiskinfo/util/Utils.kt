/**
 * Copyright (C) 2020 SINTEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.sintef.fiskinfo.util

import android.content.Context
import android.location.Location
import androidx.preference.PreferenceManager
import no.sintef.fiskinfo.model.fishingfacility.GeoJsonGeometry
import no.sintef.fiskinfo.model.fishingfacility.GeometryType
import java.text.SimpleDateFormat
import java.util.*

var sdf = SimpleDateFormat("yyyy-MM-dd")
var stf = SimpleDateFormat("HH:mm")

fun formatDate(date: Date?):String {
    if (date == null)
        return "not set"
    return sdf.format(date)
}

fun formatTime(date: Date?):String {
    if (date == null)
        return "00:00"
    return stf.format(date)
}

const val COORDINATE_FORMAT_DMS = "D_M_S"
const val COORDINATE_FORMAT_DDM = "D_DM"

val COORDINATE_FORMAT_MAP = mapOf(
    COORDINATE_FORMAT_DMS to Location.FORMAT_SECONDS,
    COORDINATE_FORMAT_DDM to Location.FORMAT_MINUTES
)


fun formatLocation(location : Location, context: Context):String {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val formatCodeStr = prefs.getString("coordinate_format", COORDINATE_FORMAT_DMS)

    if (formatCodeStr == COORDINATE_FORMAT_DDM) {
        val ddm = DDMLocation.fromLocation(location)
        val latStr = "${ddm.latitudeDegrees}\u00B0" + "%.4f".format(ddm.latitudeDecimalMinutes) + "\'${if (ddm.latitudeSouth) "S" else "N"}"
        val longStr = "${ddm.longitudeDegrees}\u00B0" + "%.4f".format(ddm.latitudeDecimalMinutes) + "\'${if (ddm.longitudeWest) "W" else "E"}"
        return "$latStr $longStr"
    } else {
        val dms = DMSLocation.fromLocation(location)
        val latStr = "${dms.latitudeDegrees}\u00B0${dms.latitudeMinutes}\'" + "%.2f".format(dms.latitudeSeconds) + "\"${if (dms.latitudeSouth) "S" else "N"}"
        val longStr = "${dms.longitudeDegrees}\u00B0${dms.longitudeMinutes}\'" + "%.2f".format(dms.longitudeSeconds) + "\"${if (dms.longitudeWest) "W" else "E"}"
        return "$latStr $longStr"
    }
/*
    if (formatCodeStr == null)
        return location.toString()

    val formatCode = COORDINATE_FORMAT_MAP[formatCodeStr]!!
    val latStr = Location.convert(location.latitude, formatCode)
    val longStr = Location.convert(location.longitude, formatCode)
    return latStr + " " + longStr*/
}

// Geometry in WKT (WellKnownText) format. Coordinates in latlong (epsg:4326). Points and LineStrings are valid. Example linestring with two points LINESTRING(5.592542 62.573817,5.593198 62.574123) example: POINT(5.7348 62.320717)

fun locationsToWTK(locations : List<Location>):String {
    if (locations.isEmpty()) {
        return ""
    } else if (locations.size == 1) {
        return "POINT(${locations[0].longitude} ${locations[0].latitude})"
//        return "POINT(${locations[0].latitude} ${locations[0].longitude})"
    } else {
        var result = "LINESTRING("
        var first = true
        locations.forEach {
            if (!first)
                result += ","
            result += it.longitude.toString() + " " + it.latitude.toString()
//            result += it.latitude.toString() + " " + it.longitude.toString()
            first = false
        }
        result += ")"
        return result
    }
}

fun wktToLocations(wkt : String?):List<Location> {
    val result = ArrayList<Location>()
    if (wkt != null) {
        try {
            val numString = wkt.substring( wkt.indexOf("(")+1, wkt.lastIndexOf(")"))
//                .replace("\\(", "")
//                .replace("\\)", "")
            val coordinateStrs = numString.split("[, ]".toRegex())
            for (i in coordinateStrs.indices step 2) {
                val loc = Location("")
                loc.latitude = coordinateStrs[i].toDouble()
                loc.longitude = coordinateStrs[i+1].toDouble()
                result.add(loc)
            }
        } catch (e: Exception) {
        }
    }
    return result
}


fun locationsToGeoJsonGeometry(locations : List<Location>): GeoJsonGeometry {
    val retval: GeoJsonGeometry?

    if (locations.isEmpty()) {
        retval = GeoJsonGeometry(
            type = GeometryType.POINT.value,
            coordinates = arrayOf(0, 0)
        )
    } else if (locations.size == 1) {
        retval = GeoJsonGeometry(
            type = GeometryType.POINT.value,
            coordinates = arrayOf(locations[0].longitude, locations[0].latitude)
        )
    } else {
        val coordinates: Array<DoubleArray> = Array(locations.size) { DoubleArray(2)}

        for (i in locations.indices) {
            coordinates[i] = doubleArrayOf(locations[i].longitude, locations[i].latitude)
        }

        retval = GeoJsonGeometry(
            type = GeometryType.LINESTRING.value,
            coordinates = coordinates
        )
    }

    return retval
}

fun geoJsonGeometryToLocations(geometry : GeoJsonGeometry):List<Location> {
    val result = ArrayList<Location>()
    try {
        if(GeometryType.POINT.value == geometry.type) {
            val location = Location("")
            location.latitude = geometry.coordinates[1] as Double
            location.longitude = geometry.coordinates[0] as Double

            result.add(location)
        } else {
            for(i in geometry.coordinates) {
                val location = Location("")
                location.latitude =(i as DoubleArray)[1]
                location.longitude = i[0]

                result.add(location)
            }
        }
    } catch (e: Exception) {
    }
    return result
}