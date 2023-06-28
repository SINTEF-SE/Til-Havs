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
package no.sintef.fiskinfo.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import net.openid.appauth.AuthorizationService
import no.sintef.fiskinfo.BuildConfig
import no.sintef.fiskinfo.api.barentswatch.FishingFacilityReportService
import no.sintef.fiskinfo.api.createService
import no.sintef.fiskinfo.model.fishingfacility.*
import no.sintef.fiskinfo.util.AuthStateManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FishingFacilityRepository(context: Context) {
    private var fishingFacilityService: FishingFacilityReportService? = null
    private val bwServerUrl = BuildConfig.SERVER_URL
    private var mFirebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    internal var confirmedTools = MutableLiveData<List<ToolViewModel>>()
    internal val unconfirmedTools = MutableLiveData<List<ToolViewModel>>()
    internal val fiskInfoProfileDTO = MutableLiveData<FiskInfoProfileDTO>()
    internal val authStateManager = AuthStateManager.getInstance(context)
    internal val authService = AuthorizationService(context)

    init {
        updateFromPreferences()
    }

    fun updateFromPreferences() {}


    fun getConfirmedTools(): LiveData<List<ToolViewModel>> {
        refreshFishingFacilityChanges()
        return confirmedTools
    }


    fun getUnconfirmedTools(): LiveData<List<ToolViewModel>> {
        refreshFishingFacilityChanges()
        return unconfirmedTools
    }

    fun getFiskInfoProfileDTO():LiveData<FiskInfoProfileDTO> {
        refreshFiskInfoProfileDTO()
        return fiskInfoProfileDTO
    }

    data class SendResult(val success : Boolean, val responseCode : Int, val errorMsg : String )

    fun sendRetrieved(info : RetrievalInfoDto):LiveData<SendResult> {
        val result = MutableLiveData<SendResult>()
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { _, _, ex ->
            if (ex == null) {
                fishingFacilityService?.sendRetrieved(info)?.enqueue(object : Callback<Void?> {
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        result.value = SendResult(false, 0, t.stackTrace.toString())
                    }

                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.code() == 200)
                            result.value = SendResult(true, response.code(),"")
                        else {
                            // TODO: Replace by more readable error messages
                            var errorMsg = "Response code " + response.code()
                            if (response.body() != null)
                                errorMsg += " " + response.body()
                            result.value = SendResult(false, response.code(), errorMsg)
                        }
                    }
                })
            }
        }
        return result
    }

    fun sendDeploymentInfo(info : DeploymentInfo):LiveData<SendResult> {

        val result = MutableLiveData<SendResult>()
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { _, _, ex ->
            if (ex == null) {
                fishingFacilityService?.sendDeploymentInfo(info)?.enqueue(object : Callback<Void?> {
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        result.value = SendResult(false, 0, t.stackTrace.toString())
                    }

                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.code() == 200)
                            result.value = SendResult(true, response.code(), "")
                        else {
                            // TODO: Replace by more readable error messages
                            var errorMsg = "Response code " + response.code()
                            if (response.body() != null)
                                errorMsg += " " + response.body()
                            result.value = SendResult(false, response.code(), errorMsg)
                        }
                    }
                })
            }
        }
        return result
    }


    fun initService() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.VALUE, "Init Service")
            param(FirebaseAnalytics.Param.CONTENT, "Auth state authorized: ${authStateManager.current.isAuthorized}")
            param(FirebaseAnalytics.Param.CONTENT, "access token: ${if(authStateManager.current.accessToken == null) "Null" else "Not null"}")
            param(FirebaseAnalytics.Param.CONTENT, "refresh token: ${if(authStateManager.current.refreshToken == null) "Null" else "Not null"}")
        }

        if(authStateManager.current.accessToken == null) {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.VALUE, "Init Service")
                param(FirebaseAnalytics.Param.CONTENT, "Access token is null")
            }
        }


        if(authStateManager.current.accessToken == null) {
        }

        fishingFacilityService =
            createService(FishingFacilityReportService::class.java,bwServerUrl , authService, authStateManager.current)
    }

    private fun refreshFiskInfoProfileDTO() {
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { _, _, ex ->
            if (ex == null) {
                fishingFacilityService?.getFishingFacilityProfile()
                    ?.enqueue(object : Callback<FiskInfoProfileDTO> {
                        override fun onResponse(
                            call: Call<FiskInfoProfileDTO>,
                            response: Response<FiskInfoProfileDTO>
                        ) {
                            if (response.body() != null) {
                                fiskInfoProfileDTO.value = response.body()
                            }
                        }

                        override fun onFailure(call: Call<FiskInfoProfileDTO>, t: Throwable) {
                            t.stackTrace
                            // TODO: log problem?
                        }
                    })
            }
        }
    }


    fun toUnconfirmedToolModels(changes : FishingFacilityChanges):List<ToolViewModel> {
        val unconfirmed = ArrayList<ToolViewModel>()

        unconfirmed.addAll(changes.pendingChangeReports.map { toToolModel(it) })
        unconfirmed.addAll(changes.failedChangeReports.map { toToolModel(it) })

        for (facility in changes.pendingChangeReports) {
            val vm = unconfirmed.find { it.toolId == facility.toolId }
            vm?.setupDateTime = facility.setupDateTime
            vm?.lastChangedDateTime = facility.lastChangedDateTime
        }

        return unconfirmed
    }

    fun toConfirmedToolModels(changes : FishingFacilityChanges):List<ToolViewModel> {
        val confirmed = ArrayList<ToolViewModel>()
        confirmed.addAll(changes.confirmedTools.map { toToolModel(it) })
        return confirmed
    }

    private fun toToolModel(facility : FishingFacility):ToolViewModel {
        with (facility) {
            return ToolViewModel(toolId, null, toolTypeCode, geometry, comment, FishingFacilityChangeType.DEPLOYED, true, setupDateTime = setupDateTime, lastChangedDateTime = lastChangedDateTime)
        }
    }

    fun refreshFishingFacilityChanges() {
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { _, _, ex ->
            if (ex == null) {
                fishingFacilityService?.getFishingFacilityChanges()
                    ?.enqueue(object : Callback<FishingFacilityChanges> {
                        override fun onResponse(
                            call: Call<FishingFacilityChanges>,
                            response: Response<FishingFacilityChanges>
                        ) {
                            if (response.body() != null) {
                                confirmedTools.value = toConfirmedToolModels(response.body()!!)
                                unconfirmedTools.value =  toUnconfirmedToolModels(response.body()!!)
                            }
                        }

                        override fun onFailure(call: Call<FishingFacilityChanges>, t: Throwable) {
                            confirmedTools.value = ArrayList()
                            unconfirmedTools.value = ArrayList()
                            // TODO: log problem?
                        }
                    })
            }
        }
    }

    companion object {
        var instance: FishingFacilityRepository? = null

        fun getInstance(context: Context): FishingFacilityRepository {
            if (instance == null)
                instance = FishingFacilityRepository(context)
            return instance!!
        }
    }
}