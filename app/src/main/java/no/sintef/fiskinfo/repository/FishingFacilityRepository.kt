package no.sintef.fiskinfo.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.openid.appauth.AuthorizationService
import no.sintef.fiskinfo.api.FishingFacilityReportService
import no.sintef.fiskinfo.api.createService
import no.sintef.fiskinfo.model.fishingfacility.*
import no.sintef.fiskinfo.util.AuthStateManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FishingFacilityRepository(context: Context) {
    internal var fishingFacilityService: FishingFacilityReportService? = null
    internal var snapFishServerUrl: String? = DEFAULT_SNAP_FISH_SERVER_URL


    internal var confirmedTools = MutableLiveData<List<FishingFacility>>()
    internal val unconfirmedTools = MutableLiveData<List<FishingFacility>>()
    internal val fiskInfoProfileDTO = MutableLiveData<FiskInfoProfileDTO>()

    internal val authStateManager = AuthStateManager.getInstance(context)
    internal val authService = AuthorizationService(context)

    val bwServerUrl = "https://pilot.barentswatch.net/"

    init {
        updateFromPreferences(context)
    }

    fun updateFromPreferences(context: Context) {}


    fun getConfirmedTools(): LiveData<List<FishingFacility>> {
        refreshFishingFacilityChanges()
        return confirmedTools
    }


    fun getUnconfirmedTools(): LiveData<List<FishingFacility>> {
        refreshFishingFacilityChanges()
        return unconfirmedTools
    }

    fun getFiskInfoProfileDTO():LiveData<FiskInfoProfileDTO> {
        refreshFiskInfoProfileDTO()
        return fiskInfoProfileDTO
    }

    data class SendResult(val success : Boolean, val responseCode : Int, val errorMsg : String )

    fun sendRetrieved(toolId : String, info : RetrievalInfoDto):LiveData<SendResult> {
        var result = MutableLiveData<SendResult>()
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { accessToken, _, ex ->
            if (ex == null) {
                fishingFacilityService?.sendRetrieved(toolId, info)?.enqueue(object : Callback<Void?> {
                    //<JsonElement?> {
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        result.value = SendResult(false, 0, t.stackTrace.toString())
                    }

                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.code() == 200)
                            result.value = SendResult(true, response.code(),"")
                        else {
                            var errorMsg = "Response code " + response.code()
                            if (response.body() != null)
                                errorMsg += " " + response.body()
                            result.value = SendResult(false, response.code(), errorMsg)
                        }
                    }
                })
            }
        }
        return result;
    }


    fun sendDeploymentInfo(info : DeploymentInfo):LiveData<SendResult> {

        var result = MutableLiveData<SendResult>()
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { accessToken, _, ex ->
            if (ex == null) {
                fishingFacilityService?.sendDeploymentInfo(info)?.enqueue(object : Callback<Void?> {
                    //<JsonElement?> {
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        result.value = SendResult(false, 0, t.stackTrace.toString())
                    }

                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.code() == 200)
                            result.value = SendResult(true, response.code(), "")
                        else {
                            var errorMsg = "Response code " + response.code()
                            if (response.body() != null)
                                errorMsg += " " + response.body()
                            result.value = SendResult(false, response.code(), errorMsg)
                        }
                    }
                })
            }
        }
        return result;
    }


    fun initService() {
        fishingFacilityService =
            createService(FishingFacilityReportService::class.java,bwServerUrl , authService, authStateManager.current)
    }

    fun refreshFiskInfoProfileDTO() {
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { accessToken, _, ex ->
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


    fun refreshFishingFacilityChanges() {
        if (fishingFacilityService == null)
            initService()

        authStateManager.current.performActionWithFreshTokens(authService) { accessToken, _, ex ->
            if (ex == null) {
                fishingFacilityService?.getFishingFacilityChanges()
                    ?.enqueue(object : Callback<FishingFacilityChanges> {
                        override fun onResponse(
                            call: Call<FishingFacilityChanges>,
                            response: Response<FishingFacilityChanges>
                        ) {
                            if (response.body() != null) {
                                confirmedTools.value = response.body()!!.confirmedTools
                                unconfirmedTools.value = response.body()!!.unconfirmedTools
                                // TODO: Reports?
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


//            createService(BarentswatchService::class.java,bwServerUrl , AuthorizationService(this.requireActivity()), viewModel.appAuthState)
//            createService(BarentswatchService::class.java,bwServerUrl )
    }

    companion object {
        var instance: FishingFacilityRepository? = null

        internal val DEFAULT_SNAP_FISH_SERVER_URL = "http://129.242.16.123:37789/"
        fun getInstance(context: Context): FishingFacilityRepository {
            if (instance == null)
                instance = FishingFacilityRepository(context)
            return instance!!
        }
    }
}