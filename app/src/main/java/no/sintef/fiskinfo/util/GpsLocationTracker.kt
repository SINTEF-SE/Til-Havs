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
package no.sintef.fiskinfo.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import no.sintef.fiskinfo.MainActivity
import no.sintef.fiskinfo.R

/**
 * Gps location tracker class to get users location and other information
 * related to location
 */
class GpsLocationTracker(
    /**
     * context of calling class
     */
    private val mContext: Context
) : Service(),
    LocationListener {

    /**
     * flag for gps
     */
    private var canGetLocation = false

    /**
     * location
     */
    private var mLocation: Location? = null

    /**
     * latitude
     */
    private var mLatitude = 0.0

    /**
     * longitude
     */
    private var mLongitude = 0.0

    /**
     * location manager
     */
    private var mLocationManager: LocationManager? =
        null/* if gps is enabled then get location using gps *//* getting location from network provider *//* no location provider enabled *//* getting status of the gps */
    /*
flag for gps status
*/
    /* getting status of network provider */
    /*
flag for network status
*/

    /**
     * @return location
     */
    val location: Location?
        get() {
            try {
                mLocationManager =
                    mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                /* getting status of the gps */
                /*
          flag for gps status
         */
                val isGpsEnabled =
                    mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                /* getting status of network provider */
                /*
          flag for network status
         */
                val isNetworkEnabled =
                    mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!isGpsEnabled && !isNetworkEnabled) {
                    /* no location provider enabled */
                    Log.i("GPS", "No location provider")
                } else {
                    canGetLocation = true
                    /* getting location from network provider */
                    if (isNetworkEnabled) {
/*                        ContextCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // You can use the API that requires the permission.
                        }
  */

                        if (ActivityCompat.checkSelfPermission(
                                mContext,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                mContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(mContext as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),1)
                            return null
                        }
                        mLocationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_FOR_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATE.toFloat(),
                            this
                        )
                        if (mLocationManager != null) {
                            mLocation =
                                mLocationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if (mLocation != null) {
                                mLatitude = mLocation!!.latitude
                                mLongitude = mLocation!!.longitude
                            }
                        }
                        /* if gps is enabled then get location using gps */if (isGpsEnabled) {
                            if (mLocation == null) {
                                mLocationManager!!.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_FOR_UPDATE,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATE.toFloat(),
                                    this
                                )
                                if (mLocationManager != null) {
                                    mLocation =
                                        mLocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                    if (mLocation != null) {
                                        mLatitude = mLocation!!.latitude
                                        mLongitude = mLocation!!.longitude
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mLocation
        }

    /**
     * call this function to stop using gps in your application
     */
    fun stopUsingGps() {
        if (mLocationManager != null) {
            mLocationManager!!.removeUpdates(this@GpsLocationTracker)
        }
    }

    /**
     * @return latitude
     *
     *
     * function to get latitude
     */
    val latitude: Double
        get() {
            if (mLocation != null) {
                mLatitude = mLocation!!.latitude
            }
            return mLatitude
        }

    /**
     * @return longitude function to get longitude
     */
    val longitude: Double
        get() {
            if (mLocation != null) {
                mLongitude = mLocation!!.longitude
            }
            return mLongitude
        }

    /**
     * @return to check gps or wifi is enabled or not
     */
    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    /**
     * function to prompt user to open dialog_settings to enable gps
     */
    fun showSettingsAlert() {
        val mAlertDialog = AlertDialog.Builder(
            ContextThemeWrapper(
                mContext,
                R.style.AppTheme
            )
        )
        mAlertDialog.setTitle(getString(R.string.util_gps_settings_disabled))
        mAlertDialog.setMessage(getString(R.string.util_gps_enable_question))
        mAlertDialog.setPositiveButton(
            "dialog_settings"
        ) { _, _ ->
            val mIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(mIntent)
        }
        mAlertDialog.setNegativeButton(
            "cancel"
        ) { dialog, _ -> dialog.cancel() }
        val mcreateDialog = mAlertDialog.create()
        mcreateDialog.show()
    }

    /**
     * These functions should be implemented if caching of geolocations should
     * be used, however they have not been at this moment to avoid confusion for
     * the user when they select use my position
     */
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        location
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(
        provider: String,
        status: Int,
        extras: Bundle
    ) {
    }

    companion object {
        /**
         * min distance change to get location update
         */
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATE: Long = 5

        /**
         * min time for location update 60000 = 1min
         */
        private const val MIN_TIME_FOR_UPDATE: Long = 20000
    }

    /**
     * @param mContext
     * constructor of the class
     */
    init {
        location
    }
}