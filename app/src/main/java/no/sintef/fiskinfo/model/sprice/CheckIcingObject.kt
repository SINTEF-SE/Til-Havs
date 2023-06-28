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

import java.time.LocalDateTime

data class CheckIcingObject (
    var Id : Int = 0,
    var ObservationTime: LocalDateTime? = null,
    var Synop: String? = null,

    var CallSign: String? = null,
    var Latitude: Float? = 0.0F,
    var Longitude: Float? = 0.0F,

    var AirTemperature: Float? = 0.0F,
    var SeaTemperature: Float? = 0.0F,

    var MaxMiddleWindTime: String? = null,
    var MaxMiddelWindInKnots: Float? = null,
    var StrongestWindGustInKnots: Float? = null,

    var HeightOfWindWavesInMeters: Float? = 0.0F,
    var PeriodForWindWavesInSeconds: Float? = 0.0F,

    var HeightForFirstSwellSystemInMeters: Float? = 0.0F,
    var PeriodForFirstSwellSystemInSeconds: Float? = 0.0F,
    var DirectionForFirstSwellSystemInDegrees: Float? = 0.0F,

    var ConcentrationOfSeaIce: String? = null,
    var SeaIceStageOfDevelopment: String? = null,
    var OceanIce: String? = null,
    var DirectionToNearestIceEdge: Float? = null,
    var SeaIceConditionsAndDevelopmentTheLastThreeHours: String? = null,

    var ReasonForIcing: String? = null,
    var IceThicknessInCm: Float? = 0.0F,
    var ChangeInIce: String? = null
)