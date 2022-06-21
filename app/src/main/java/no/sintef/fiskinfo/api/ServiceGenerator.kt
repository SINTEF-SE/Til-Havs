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
package no.sintef.fiskinfo.api

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.gson.*
import java9.util.concurrent.CompletableFuture
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Create a REST service without authentication
 */
fun <S> createService(serviceClass : Class<S>, baseUrl : String) : S {
    val client =  OkHttpClient.Builder()
        .addInterceptor(JSONHeaderInterceptor("FiskInfo/2.0 (Android)"))
        .build()
    // TODO: Consider how to handle field name policy. At the moment this function is used
    // by snapfish, while the authorization service one is used by BW services
    return createService(serviceClass, baseUrl, client, FieldNamingPolicy.UPPER_CAMEL_CASE)
}

/**
 * Create a REST service with authentication
 */
fun <S> createService(serviceClass: Class<S>, baseUrl : String, authToken : String) : S {
    val client =  OkHttpClient.Builder()
        .addInterceptor(OAuthInterceptor("Bearer", authToken,  "FiskInfo/2.0 (Android)"))
        .build()

    return createService(serviceClass, baseUrl, client, FieldNamingPolicy.IDENTITY)
}

/**
 * Create a REST service with AuthApp-based authentication
 */
fun <S> createService(serviceClass: Class<S>, baseUrl : String, authService: AuthorizationService, authState: AuthState) : S {
    val in2 = HttpLoggingInterceptor()
    in2.setLevel(HttpLoggingInterceptor.Level.BODY)

    if(authState.accessToken == null) {

    }

    val client =  OkHttpClient.Builder()
        //.authenticator(OIDCAuthenticator(authService, authState, "FiskInfo/3.0 (Android)"))
        //.addInterceptor(OAuthInterceptor("Bearer", authToken,  "FiskInfo/2.0 (Android)"))
        .addInterceptor(in2)
        .addInterceptor(OAuthInterceptor("bearer", authState.accessToken!!,  "FiskInfo/3.0 (Android)")) // TODO: handle renewal of access tokens
        .build()

    return createService(serviceClass, baseUrl, client, FieldNamingPolicy.IDENTITY)
}


private fun <S> createService(serviceClass: Class<S>, baseUrl : String, client : OkHttpClient, namingPolicy: FieldNamingPolicy) : S {
    val gson = GsonBuilder()
        .setLenient() // consider to remove
        // .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") //Consider to add
        // .excludeFieldsWithoutExposeAnnotation() Consider to add
        .setFieldNamingPolicy(namingPolicy)
        .registerTypeAdapter(
            Date::class.java,  DateTypeDeserializer())
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    return retrofit.create(serviceClass)
}


class DateTypeDeserializer : JsonDeserializer<Date> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        typeOF: Type,
        context: JsonDeserializationContext
    ): Date {
        for (format in DATE_FORMATS) {
            try {
                return SimpleDateFormat(format).parse(jsonElement.asString) //SimpleDateFormat(format, Locale.US).parse(jsonElement.asString)
            } catch (e: ParseException) {
            }
        }
        throw JsonParseException(
            "Unparseable date: \"" + jsonElement.asString
                    + "\". Supported formats: \n" + Arrays.toString(DATE_FORMATS)
        )
    }

    companion object {
        private val DATE_FORMATS = arrayOf(
            "yyyy-MM-dd'T'HH:mm:ssz",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ssX",
            "yyyy-MM-dd'T'HH:mm:ss"
        )
    }
}


private class JSONHeaderInterceptor(private val userAgent : String = ""):
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("User-Agent", "$userAgent")
            .header("Accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}



// TODO: Figure out what parameters are needed in new API for Authorization
// TODO: Figure out if the call can be done without involving futures
// TODO: Test call on a simple API call


private class OIDCAuthenticator(private val authService: AuthorizationService, private val authState: AuthState, private val userAgent : String = "") :
    Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val future = CompletableFuture<Request?>()

        authState.performActionWithFreshTokens(authService) { accessToken, _, ex ->
            if (ex != null) {
                //Log.e("AppAuthAuthenticator", "Failed to authorize = $ex")
            }

            if (response.request().header("Authorization") != null) {
                future.complete(null) // Give up, we've already failed to authenticate.
            }

            val response = response.request().newBuilder()
                .header("Authorization", "bearer $accessToken")
                .header("User-Agent", "$userAgent")
                .build()

            future.complete(response)
        }

        return future.get()
    }

}



private class OAuthInterceptor(private val tokenType: String, private val accessToken: String, private val userAgent : String = ""):
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("Authorization", "$tokenType $accessToken")
            .header("User-Agent", "$userAgent")
            .build()

        return chain.proceed(request)
    }
}

