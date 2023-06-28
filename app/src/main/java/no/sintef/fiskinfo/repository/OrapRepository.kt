package no.sintef.fiskinfo.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import no.sintef.fiskinfo.BuildConfig
import no.sintef.fiskinfo.api.createService
import no.sintef.fiskinfo.api.orap.OrapService
import no.sintef.fiskinfo.model.sprice.*
import no.sintef.fiskinfo.util.SpriceUtils.Companion.getPostRequestContentTypeValueWithWebKitBoundaryIdAsString
import no.sintef.fiskinfo.util.SpriceUtils.Companion.getPostRequestReferrerAsString
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrapRepository(context: Context, private var username: String, private var password: String, private var webKitFormBoundaryId: String) {
    private var orapService: OrapService? = null
    private var orapServerUrl: String = BuildConfig.SPRICE_ORAP_SERVER_URL
    private var mFirebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    internal var icingReports = MutableLiveData<List<Any>>()

    fun getEarlierReports(info: GetReportsRequestBody): LiveData<SendResult> {
        if (orapService == null)
            initService()

        val result = MutableLiveData<SendResult>()

        orapService?.getReports(info)?.enqueue(object : Callback<okhttp3.Response> {
            override fun onResponse(call: Call<okhttp3.Response>, response: Response<okhttp3.Response>) {
                Log.d("ORAP", "Fetching icing reports ok!")

                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<okhttp3.Response>, t: Throwable) {
                Log.d("ORAP", "Fetching icing reports failed!")
                t.printStackTrace()
                TODO("Not yet implemented")
            }
        })

        return result
    }

    fun sendIcingReport(info: ReportIcingRequestBody): LiveData<SendResult> {
        initService()

        val result = MutableLiveData<SendResult>()
        val requestBody = info.getRequestBodyForSpriceEndpointReportSubmissionAsRequestBody(webKitFormBoundaryId)

        orapService?.sendIcingReport(requestBody, info.Username, info.Password)
            ?.enqueue(object : Callback<Void?> {
                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Log.e("ORAP", "Icing report failed!")
                    Log.e("ORAP", t.message.toString())
                    result.value = SendResult(false, 0, t.stackTrace.toString())
                }

                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    Log.e("ORAP", "Icing report response!")
                    if (response.code() == 200) {
                        Log.e("ORAP", "Icing reported OK!")
                        result.value = SendResult(true, response.code(), "")
                    }
                    else {
                        // TODO: Replace by more readable error messages
                        var errorMsg = "Response code " + response.code()
                        if (response.body() != null)
                            errorMsg += " " + response.body()
                        result.value = SendResult(false, response.code(), errorMsg)
                        Log.e("ORAP", "Icing report response failed!")
                    }
                }
            })

        return result
    }

    fun initService() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.VALUE, "Init Sprice service")
        }

        val referrer = getPostRequestReferrerAsString(username, password)
        val headers = listOf(
            Pair("Referer", referrer)
        )

        val interceptors = listOf<Interceptor>(OrapInterceptor(headers))

        orapService =
            createService(OrapService::class.java, orapServerUrl, false, interceptors)
    }

    private class OrapInterceptor(var headers: List<Pair<String, String>>) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response = chain.run {
            val builder = request()
                .newBuilder()

            headers.forEach { header -> builder.addHeader(header.first, header.second) }

            proceed(builder.build())
        }
    }

    data class SendResult(val success: Boolean, val responseCode: Int, val errorMsg: String)

    companion object {
        var instance: OrapRepository? = null

        fun getInstance(context: Context, username: String, password: String, webKitFormBoundaryId: String): OrapRepository {
            if (instance == null)
                instance = OrapRepository(context, username, password, webKitFormBoundaryId)
            return instance!!
        }
    }
}