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
package no.sintef.fiskinfo.api.snapfish

import no.sintef.fiskinfo.model.SnapMetadata
import no.sintef.fiskinfo.model.SnapMessage
import no.sintef.fiskinfo.model.SnapMessageDraft
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface to REST API service for getting and creating echogram snap messages
 * and metainformation for shared echograms
 */
interface SnapMessageService {

    @GET("api/snapmetadata")
    fun getSnapMetadata(@Query("ownerId") ownerId: Int): Call<List<SnapMetadata>>
    //    @GET("users/{user}/repos")
    //    Call<List<SnapMessage>> listRepos(@Path("user") String user);

    @GET("api/snapmessages")
    fun getSnapMessages(@Query("userId") userId: Int, @Query("inbox") inbox: Boolean, @Query("snapmetadata") snapmetadata: Boolean): Call<List<SnapMessage>>

    @POST("api/snapmessages")
    fun sendSnapMessage(@Body message: SnapMessageDraft): Call<SnapMessage>

}
