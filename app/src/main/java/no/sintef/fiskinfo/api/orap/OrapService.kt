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
package no.sintef.fiskinfo.api.orap

import no.sintef.fiskinfo.model.sprice.GetReportsRequestBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface to API service from Orap
 */
interface OrapService {
    companion object {
        const val prefix = "cgi-bin/"
        const val ObsReport = prefix + "ObsRaport.cgi/"
    }

    @POST( ObsReport)
    fun checkReport(@Body body : String, @Query("user") user: String, @Query("password") password: String): Call<Response>

    @POST( ObsReport)
    @Headers(
        "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "accept-language: en,nb-NO;q=0.9,nb;q=0.8,no;q=0.7,nn;q=0.6,en-US;q=0.5,en-GB;q=0.4",
        "cache-control: max-age=0",
        "sec-ch-ua: \".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"",
        "sec-ch-ua-mobile: ?0",
        "sec-ch-ua-platform: \"Windows\"",
        "sec-fetch-dest: document",
        "sec-fetch-mode: navigate",
        "sec-fetch-site: same-origin",
        "sec-fetch-user: ?1",
        "upgrade-insecure-requests: 1",
        "referrerPolicy: strict-origin-when-cross-origin"
    )
    fun sendIcingReport(@Body body : RequestBody, @Query("user") user: String, @Query("password") password: String): Call<Void?>

    @GET( ObsReport)
    fun getReports(@Body body : GetReportsRequestBody): Call<Response>

}
