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
package no.sintef.fiskinfo.model.sprice

import no.sintef.fiskinfo.util.SpriceUtils
import no.sintef.fiskinfo.util.SpriceUtils.Companion.getPostRequestContentTypeBoundaryValueAsString
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.time.ZoneId
import java.time.ZonedDateTime

class ReportIcingRequestBody internal constructor(
    internal val WebKitFormBoundaryId: String,
    internal val ReportingTime: ZonedDateTime,
    internal val Synop: ZonedDateTime,

    internal val Username: String,
    internal val Password: String,

    internal val VesselCallSign: String,
    internal val Latitude: String,
    internal val Longitude: String,

    internal val AirTemperature: String,
    internal val SeaTemperature: String,

    internal val MaxMiddleWindTime: MaxMiddleWindTimeEnum,
    internal val MaxMiddelWindInKnots: String,
    internal val StrongestWindGustInKnots: String,

    internal val HeightOfWindWavesInMeters: String,
    internal val PeriodForWindWavesInSeconds: String,

    internal val HeightForFirstSwellSystemInMeters: String,
    internal val PeriodForFirstSwellSystemInSeconds: String,
    internal val DirectionForFirstSwellSystemInDegrees: String,

    internal val ConcentrationOfSeaIce: String,
    internal val OceanIce: String,
    internal val DirectionToNearestIceEdge: String,
    internal val SeaIceConditionsAndDevelopmentTheLastThreeHours: SeaIceConditionsAndDevelopmentEnum,

    internal val CurrentIcingDegree: DegreeOfIcingEnum,
    internal val ReasonForIcing: ReasonForIcingOnVesselOrPlatformEnum,
    internal val IceThicknessInCm: Int,
    internal val ChangeInIce: ChangeInIcingOnVesselOrPlatformEnum
) {
    private constructor(builder: Builder) : this(
        builder.WebKitFormBoundaryId,
        builder.ReportingTime,
        builder.Synop,
        builder.Username,
        builder.Password,
        builder.VesselCallSign,
        builder.Latitude,
        builder.Longitude,
        builder.AirTemperature,
        builder.SeaTemperature,
        builder.MaxMiddleWindTime,
        builder.MaxMiddleWindInKnots,
        builder.StrongestWindGustInKnots,
        builder.HeightOfWindWavesInMeters,
        builder.PeriodForWindWavesInSeconds,
        builder.HeightForFirstSwellSystemInMeters,
        builder.PeriodForFirstSwellSystemInSeconds,
        builder.DirectionForFirstSwellSystemInDegrees,
        builder.ConcentrationOfSeaIce,
        builder.OceanIce,
        builder.DirectionToNearestIceEdge,
        builder.SeaIceConditionsAndDevelopmentTheLastThreeHours,
        builder.CurrentIcingDegree,
        builder.ReasonForIcing,
        builder.IceThicknessInCm,
        builder.ChangeInIce
    )

    data class Builder(
        var WebKitFormBoundaryId: String = SpriceUtils.getBoundaryIdString(),
        var ReportingTime: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
        var Synop: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
        var Username: String = "",
        var Password: String = "",
        var VesselCallSign: String = "",
        var Latitude: String = "",
        var Longitude: String = "",
        var AirTemperature: String = "",
        var SeaTemperature: String = "",
        var MaxMiddleWindTime: MaxMiddleWindTimeEnum = MaxMiddleWindTimeEnum.DURING_OBSERVATION,
        var MaxMiddleWindInKnots: String = "",
        var StrongestWindGustInKnots: String = "",
        var HeightOfWindWavesInMeters: String = "",
        var PeriodForWindWavesInSeconds: String = "",
        var HeightForFirstSwellSystemInMeters: String = "",
        var PeriodForFirstSwellSystemInSeconds: String = "",
        var DirectionForFirstSwellSystemInDegrees: String = "",
        var ConcentrationOfSeaIce: String = "",
        var OceanIce: String = "",
        var DirectionToNearestIceEdge: String = "",
        var SeaIceConditionsAndDevelopmentTheLastThreeHours: SeaIceConditionsAndDevelopmentEnum = SeaIceConditionsAndDevelopmentEnum.NOT_SELECTED,
        var ReasonForIcing: ReasonForIcingOnVesselOrPlatformEnum = ReasonForIcingOnVesselOrPlatformEnum.NOT_SELECTED,
        var IceThicknessInCm: Int = 0,
        var CurrentIcingDegree: DegreeOfIcingEnum = DegreeOfIcingEnum.NOT_SELECTED,
        var ChangeInIce: ChangeInIcingOnVesselOrPlatformEnum = ChangeInIcingOnVesselOrPlatformEnum.NOT_SELECTED
    ) {
        fun webKitFormBoundaryId(webKitFormBoundaryId: String) = apply { this.WebKitFormBoundaryId = webKitFormBoundaryId }
        fun reportingTime(reportingTime: ZonedDateTime) = apply { this.ReportingTime = reportingTime }
        fun synop(synop: ZonedDateTime) = apply { this.Synop = synop }
        fun username(username: String) = apply { this.Username = username }
        fun password(password: String) = apply { this.Password = password }
        fun callSign(callSign: String) = apply { this.VesselCallSign = callSign }
        fun latitude(latitude: String) = apply { this.Latitude = latitude }
        fun longitude(longitude: String) = apply { this.Longitude = longitude }
        fun airTemperature(airTemperature: String) = apply { this.AirTemperature = airTemperature }
        fun seaTemperature(seaTemperature: String) = apply { this.SeaTemperature = seaTemperature }
        fun maxMiddleWindTime(maxMiddleWindTime: MaxMiddleWindTimeEnum) = apply { this.MaxMiddleWindTime = maxMiddleWindTime }

        fun maxMiddleWindInKnots(maxMiddleWindInKnots: String) = apply { this.MaxMiddleWindInKnots = maxMiddleWindInKnots }
        fun strongestWindGustInKnots(strongestWindGustInKnots: String) = apply { this.StrongestWindGustInKnots = strongestWindGustInKnots }
        fun heightOfWindWavesInMeters(heightOfWindWavesInMeters: String) =
            apply { this.HeightOfWindWavesInMeters = heightOfWindWavesInMeters }

        fun periodForWindWavesInSeconds(periodForWindWavesInSeconds: String) =
            apply { this.PeriodForWindWavesInSeconds = periodForWindWavesInSeconds }

        fun heightForFirstSwellSystemInMeters(heightForFirstSwellSystemInMeters: String) =
            apply { this.HeightForFirstSwellSystemInMeters = heightForFirstSwellSystemInMeters }

        fun periodForFirstSwellSystemInSeconds(periodForFirstSwellSystemInSeconds: String) =
            apply { this.PeriodForFirstSwellSystemInSeconds = periodForFirstSwellSystemInSeconds }

        fun directionForFirstSwellSystemInDegrees(directionForFirstSwellSystemInDegrees: String) =
            apply { this.DirectionForFirstSwellSystemInDegrees = directionForFirstSwellSystemInDegrees }

        fun concentrationOfSeaIce(concentrationOfSeaIce: String) =
            apply { this.ConcentrationOfSeaIce = concentrationOfSeaIce }

        fun oceanIce(oceanIce: String) = apply { this.OceanIce = oceanIce }
        fun directionToNearestIceEdge(directionToNearestIceEdge: String) =
            apply { this.DirectionToNearestIceEdge = directionToNearestIceEdge }

        fun seaIceConditionsAndDevelopmentTheLastThreeHours(
            seaIceConditionsAndDevelopmentTheLastThreeHours: SeaIceConditionsAndDevelopmentEnum
        ) = apply {
            this.SeaIceConditionsAndDevelopmentTheLastThreeHours =
                seaIceConditionsAndDevelopmentTheLastThreeHours
        }

        fun reasonForIcing(reasonForIcing: ReasonForIcingOnVesselOrPlatformEnum) = apply { this.ReasonForIcing = reasonForIcing }
        fun currentIcingDegree(currentIcingDegree: DegreeOfIcingEnum) = apply { this.CurrentIcingDegree = currentIcingDegree}
        fun iceThicknessInCm(iceThicknessInCm: String) =
            apply {
                try {
                    this.IceThicknessInCm = iceThicknessInCm.toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

        fun changeInIce(changeInIce: ChangeInIcingOnVesselOrPlatformEnum) = apply { this.ChangeInIce = changeInIce }

        fun build(): ReportIcingRequestBody {
            return ReportIcingRequestBody(this)
        }
    }

    fun getRequestBodyForSpriceEndpointReportSubmissionAsString(): String {
        val reportingZonedTime = ReportingTime
        val reportingTimeEpoch = reportingZonedTime.toEpochSecond()
        val observationEpoch = Synop.toEpochSecond()
        val stringBuilder = StringBuilder()

        val messageTag =
            SpriceUtils.getOrapMessageTag(reportingZonedTime, Username)

        val hiddenMessage = SpriceUtils.getFormattedHiddenMessageForSpriceEndpoint(Username, VesselCallSign, Latitude, Longitude, ReasonForIcing.getFormIndex(), SeaIceConditionsAndDevelopmentTheLastThreeHours.getFormIndex(), IceThicknessInCm, ChangeInIce.getFormIndex(), CurrentIcingDegree.getFormIndex())
        val hiddenKlMessage = SpriceUtils.getFormattedHiddenKlMessageForSpriceEndpoint(Username, VesselCallSign, Latitude, Longitude, ReasonForIcing.getFormIndex(), CurrentIcingDegree.getFormIndex(), SeaIceConditionsAndDevelopmentTheLastThreeHours.getFormIndex(), IceThicknessInCm, ChangeInIce.getFormIndex(), reportingZonedTime, Synop, reportingTimeEpoch)
        val hiddenKlStatus = SpriceUtils.getFormattedHiddenKlStatusForSpriceEndpoint(observationEpoch, Username, VesselCallSign, Latitude, Longitude, ReasonForIcing.getFormIndex(), CurrentIcingDegree.getFormIndex(), SeaIceConditionsAndDevelopmentTheLastThreeHours.getFormIndex(), IceThicknessInCm, ChangeInIce.getFormIndex())

        stringBuilder.append(
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.ACTION, OrapConstants.FormValues.ACTION_SEND_REPORT),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.TAG, messageTag),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.REG_EPOC, reportingTimeEpoch),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.USER, Username),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.PASSWORD, Password),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.MESSAGE_TYPE, OrapConstants.FormValues.REPORT_MESSAGE_TYPE),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.LAT, ""),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.LON, ""),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.CAL, ""),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_TERMIN, observationEpoch),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_MESS, hiddenMessage),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_KL_MESS, hiddenKlMessage),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_KL_STATUS, hiddenKlStatus),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_FILE, OrapConstants.FormValues.HIDDEN_FILE_VALUE),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_TYPE, OrapConstants.FormValues.HIDDEN_TYPE),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.LSTEP, OrapConstants.FormValues.LSTEP),
            SpriceUtils.getWebFormKitEndTag(WebKitFormBoundaryId)
        )

        return stringBuilder.toString()
    }

    fun getRequestBodyForSpriceEndpointReportSubmissionAsRequestBody(boundary: String): MultipartBody {
        val reportingZonedTime = ReportingTime
        val reportingTimeEpoch = reportingZonedTime.toEpochSecond()
        val observationEpoch = Synop.toEpochSecond()

        val messageTag =
            SpriceUtils.getOrapMessageTag(reportingZonedTime, Username)

        val hiddenMessage = SpriceUtils.getFormattedHiddenMessageForSpriceEndpoint(Username, VesselCallSign, Latitude, Longitude, ReasonForIcing.getFormIndex(), SeaIceConditionsAndDevelopmentTheLastThreeHours.getFormIndex(), IceThicknessInCm, ChangeInIce.getFormIndex(), CurrentIcingDegree.getFormIndex())
        val hiddenKlMessage = SpriceUtils.getFormattedHiddenKlMessageForSpriceEndpoint(Username, VesselCallSign, Latitude, Longitude, ReasonForIcing.getFormIndex(), CurrentIcingDegree.getFormIndex(), SeaIceConditionsAndDevelopmentTheLastThreeHours.getFormIndex(), IceThicknessInCm, ChangeInIce.getFormIndex(), reportingZonedTime, Synop, reportingTimeEpoch)
        val hiddenKlStatus = SpriceUtils.getFormattedHiddenKlStatusForSpriceEndpoint(observationEpoch, Username, VesselCallSign, Latitude, Longitude, ReasonForIcing.getFormIndex(), CurrentIcingDegree.getFormIndex(), SeaIceConditionsAndDevelopmentTheLastThreeHours.getFormIndex(), IceThicknessInCm, ChangeInIce.getFormIndex())

        val requestBody =  MultipartBody.Builder(getPostRequestContentTypeBoundaryValueAsString(boundary))
            .setType(MediaType.parse("multipart/form-data")!!)
            .addFormDataPart(OrapConstants.FormDataNames.ACTION, OrapConstants.FormValues.ACTION_SEND_REPORT)
            .addFormDataPart(OrapConstants.FormDataNames.TAG, messageTag)
            .addFormDataPart(OrapConstants.FormDataNames.REG_EPOC, reportingTimeEpoch.toString())
            .addFormDataPart(OrapConstants.FormDataNames.USER, Username)
            .addFormDataPart(OrapConstants.FormDataNames.PASSWORD, Password)
            .addFormDataPart(OrapConstants.FormDataNames.MESSAGE_TYPE, OrapConstants.FormValues.REPORT_MESSAGE_TYPE)
            .addFormDataPart(OrapConstants.FormDataNames.LAT, "")
            .addFormDataPart(OrapConstants.FormDataNames.LON, "")
            .addFormDataPart(OrapConstants.FormDataNames.CAL, "")
            .addFormDataPart(OrapConstants.FormDataNames.HIDDEN_TERMIN, observationEpoch.toString())
            .addFormDataPart(OrapConstants.FormDataNames.HIDDEN_MESS, hiddenMessage)
            .addFormDataPart(OrapConstants.FormDataNames.HIDDEN_KL_MESS, hiddenKlMessage)
            .addFormDataPart(OrapConstants.FormDataNames.HIDDEN_KL_STATUS, hiddenKlStatus)
            .addFormDataPart(OrapConstants.FormDataNames.HIDDEN_FILE, OrapConstants.FormValues.HIDDEN_FILE_VALUE)
            .addFormDataPart(OrapConstants.FormDataNames.HIDDEN_TYPE, OrapConstants.FormValues.HIDDEN_TYPE)
            .addFormDataPart(OrapConstants.FormDataNames.LSTEP, OrapConstants.FormValues.LSTEP.toString())

        return requestBody.build()
    }

    fun getRequestBodyForReportSubmissionAsString(): String {
        val reportingZonedTime = ReportingTime
        val reportingTimeEpoch = reportingZonedTime.toEpochSecond()
        val observationEpoch = Synop.toEpochSecond()
        val stringBuilder = StringBuilder()

        val messageTag =
            SpriceUtils.getOrapMessageTag(reportingZonedTime, Username)

        val hiddenMessage = SpriceUtils.getFormattedHiddenMessage(Username, HeightOfWindWavesInMeters, PeriodForWindWavesInSeconds, IceThicknessInCm, Latitude, Longitude, AirTemperature)
        val hiddenKlMessage = SpriceUtils.getFormattedHiddenKlMessage(Username, reportingZonedTime, Synop, HeightOfWindWavesInMeters, PeriodForWindWavesInSeconds, IceThicknessInCm, Latitude, Longitude, AirTemperature, reportingTimeEpoch)
        val hiddenKlStatus = SpriceUtils.getFormattedHiddenKlStatus(observationEpoch, Username, HeightOfWindWavesInMeters, PeriodForWindWavesInSeconds, IceThicknessInCm, Latitude, Longitude, AirTemperature)

        stringBuilder.append(
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.ACTION, OrapConstants.FormValues.ACTION_SEND_REPORT),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.TAG, messageTag),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.REG_EPOC, reportingTimeEpoch),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.USER, Username),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.PASSWORD, Password),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.MESSAGE_TYPE, OrapConstants.FormValues.REPORT_MESSAGE_TYPE),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_TERMIN, observationEpoch),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_MESS, hiddenMessage),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_KL_MESS, hiddenKlMessage),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_KL_STATUS, hiddenKlStatus),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_FILE, OrapConstants.FormValues.HIDDEN_FILE_VALUE),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.HIDDEN_TYPE, OrapConstants.FormValues.HIDDEN_TYPE),
            SpriceUtils.getValueAsWebKitForm(WebKitFormBoundaryId, OrapConstants.FormDataNames.LSTEP, OrapConstants.FormValues.LSTEP),
            SpriceUtils.getWebFormKitEndTag(WebKitFormBoundaryId)
        )

        return stringBuilder.toString()
    }
}