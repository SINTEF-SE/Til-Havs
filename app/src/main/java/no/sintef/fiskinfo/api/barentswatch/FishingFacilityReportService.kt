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
package no.sintef.fiskinfo.api.barentswatch

import no.sintef.fiskinfo.model.fishingfacility.DeploymentInfo
import no.sintef.fiskinfo.model.fishingfacility.FishingFacilityChanges
import no.sintef.fiskinfo.model.fishingfacility.FiskInfoProfileDTO
import no.sintef.fiskinfo.model.fishingfacility.RetrievalInfoDto
import retrofit2.Call
import retrofit2.http.*

// Documentation of API:  https://code.barentswatch.net/openapi/?url=https://www.barentswatch.no/api/documentation/fiskinfo-reporting.yaml

interface FishingFacilityReportService {
    companion object {
        const val v1prefix = "bwapi/v1/geodata/"
        const val v2prefix = "bwapi/v2/geodata/"
        const val v3prefix = "bwapi/v3/geodata/"
        const val fishingfacilityprofile = v2prefix + "fishingfacilityprofile"
        const val fishingfacilitychanges = v3prefix + "fishingfacilitychanges"
        const val deployed = v3prefix + "fishingfacilitychange/deployed"
        const val retrieved = v3prefix + "fishingfacilitychange/retrieved"
        //const val geoDataSubscription = prefix + "subscription/"
        //const val geoDataSubscriptionManagement = prefix + "subscription/{Id}"
        //const val geoDataDownload = prefix + "download/{ApiName}"
        const val authorization = v1prefix + "authorization"
    }

    @GET(fishingfacilityprofile)
//    fun getFishingFacilityProfile(): Call<JsonElement>
    fun getFishingFacilityProfile(): Call<FiskInfoProfileDTO>

    @GET(fishingfacilitychanges)
    fun getFishingFacilityChanges(): Call<FishingFacilityChanges>

    @POST(deployed)
    fun sendDeploymentInfo(@Body deploymentInfo: DeploymentInfo):Call<Void?> // Call<JsonElement?>

    @POST(retrieved)
    fun sendRetrieved(@Body retrievalInfo: RetrievalInfoDto):Call<Void?>

    /*
    @GET(geoDataSubscription)
    fun getSubscriptions(): Call<List<Subscription>>

    @POST(geoDataSubscription)
    fun setSubscription(@Body subscription: SubscriptionSubmitObject): Call<Subscription>*/


}