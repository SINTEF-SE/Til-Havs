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

import no.sintef.fiskinfo.model.barentswatch.PropertyDescription
import no.sintef.fiskinfo.model.barentswatch.Subscription
import no.sintef.fiskinfo.model.barentswatch.SubscriptionSubmitObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface to REST API services from Barentswatch
 */
/*
object BarentswatchServicePaths {
    val subscribable = "service/subscribable/"
    val geoDataSubscription = "subscription/"
    val geoDataSubscriptionManagement = "subscription/{Id}"
    val geoDataDownload = "download/{ApiName}"
    val authorization = "authorization"
}
*/
interface BarentswatchService {
    companion object {
        const val prefix = "bwapi/v1/geodata/"
        const val subscribable = prefix + "service/subscribable/"
        const val geoDataSubscription = prefix + "subscription/"
        const val geoDataSubscriptionManagement = prefix + "subscription/{Id}"
        const val geoDataDownload = prefix + "download/{ApiName}"
        const val authorization = prefix + "authorization"
    }

    @GET( subscribable)
    fun getSubscribable(): Call<List<PropertyDescription>>
    //    @GET("users/{user}/repos")
    //    Call<List<SnapMessage>> listRepos(@Path("user") String user);

    @GET(geoDataSubscription)
    fun getSubscriptions(): Call<List<Subscription>>

    @POST(geoDataSubscription)
    fun setSubscription(@Body subscription: SubscriptionSubmitObject): Call<Subscription>

}
