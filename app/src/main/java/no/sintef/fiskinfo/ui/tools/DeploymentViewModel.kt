/**
 * Copyright (C) 2020 SINTEF
 *
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
package no.sintef.fiskinfo.ui.tools

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.preference.PreferenceManager
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.model.fishingfacility.DeploymentInfo
import no.sintef.fiskinfo.model.fishingfacility.FiskInfoProfileDTO
import no.sintef.fiskinfo.model.fishingfacility.ToolTypeCode
import no.sintef.fiskinfo.repository.FishingFacilityRepository
import no.sintef.fiskinfo.util.locationsToGeoJsonGeometry
import no.sintef.fiskinfo.utilities.ui.ObservableAndroidViewModel
import java.util.*

class DeploymentViewModel(application: Application) : ObservableAndroidViewModel(application) {
    val toolTypeCode = MutableLiveData<ToolTypeCode>()
    val setupTime = MutableLiveData<Date>()
    val locations = MutableLiveData<MutableList<Location>>()
    val toolCount = MutableLiveData<String>()
    var initialized = false

    private fun createDeploymentInfo(): DeploymentInfo {
        getContactInfoFromPreferences()

        return DeploymentInfo(
            toolId = UUID.randomUUID().toString(),
            setupTime = setupTime.value!!,
            contactEmail = contactPersonEmail!!,
            contactPhone = contactPersonPhone!!,
            toolTypeCode = toolTypeCode.value!!,
            toolCount = toolCount.value!!.toInt(),
            geometry = locationsToGeoJsonGeometry(locations.value!!)
        )
    }

    private var contactPersonEmail: String? = null
    private var contactPersonPhone: String? = null

    private fun getContactInfoFromPreferences() {
        val context: Context = getApplication()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        contactPersonEmail =
            prefs.getString(context.getString(R.string.pref_contact_person_email), "")
        contactPersonPhone =
            prefs.getString(context.getString(R.string.pref_contact_person_phone), "")
    }


    fun clear() {
        initialized = false
    }

    fun initContent() {
        if (!initialized) {
            val context: Context = getApplication()
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)

            val preferenceToolType = prefs.getString(
                context.getString(R.string.pref_tool_type),
                ToolTypeCode.NETS.code
            )!!

            toolTypeCode.value = ToolTypeCode.valueOf(preferenceToolType)

            val preferenceToolCount = prefs.getString(
                context.getString(R.string.pref_tool_count),
                10.toString()
            )!!
            toolCount.value = preferenceToolCount
            setupTime.value = Date()
            val defaultLoc = Location("")
            // TODO: Default locations
            defaultLoc.latitude = 0.0  //your coords of course
            defaultLoc.longitude = 0.0
            locations.value = mutableListOf(defaultLoc)
        }
    }

    private var fiskInfoProfileDTO: LiveData<FiskInfoProfileDTO>? = null

    fun getFiskInfoProfileDTO(): LiveData<FiskInfoProfileDTO>? {
        if (fiskInfoProfileDTO == null) {
            val fishingFacilityRepository =
                FishingFacilityRepository.getInstance(this.getApplication())
            fiskInfoProfileDTO = fishingFacilityRepository.getFiskInfoProfileDTO()
        }
        return fiskInfoProfileDTO
    }

    fun isProfileValid(): Boolean {
        getFiskInfoProfileDTO()?.value?.let {
            return it.fiskinfoProfile?.ircs != null
        }
        return false
    }

    fun canSendReport(): Boolean {
        // TODO: Check that all required fields of report are present
        return isProfileValid()
    }

    fun sendReport(): LiveData<FishingFacilityRepository.SendResult> {
        val info = createDeploymentInfo()
        return FishingFacilityRepository.getInstance(getApplication()).sendDeploymentInfo(info)
        // TODO: How to handle feedback. Use a kind of notification object with Livedata?
        // Could also use error mechanism in views similar to:
        // https://www.journaldev.com/22561/android-mvvm-livedata-data-binding
    }

    /*
        fun setToolTypeCode(code: ToolTypeCode) {
            if ((deploymentInfo.value?.toolTypeCode != code) && (code != null)) {
                deploymentInfo.value?.toolTypeCode != code
                selectedToolTypeCodeName.value = code.getLocalizedName(getApplication())
            }
        }
    */
    fun setSetupDate(date: Date) {
        // TODO: pick out only date part (not time)
        val c = Calendar.getInstance()
        c.time = setupTime.value!!

        val newDateC = Calendar.getInstance()
        newDateC.time = date
        c.set(Calendar.YEAR, newDateC.get(Calendar.YEAR))
        c.set(Calendar.DAY_OF_YEAR, newDateC.get(Calendar.DAY_OF_YEAR))

        setupTime.value = c.time
    }

    fun setSetupTime(hourOfDay: Int, minutes: Int) {
        val c = Calendar.getInstance()
        c.time = setupTime.value!!
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minutes)
        setupTime.value = c.time
    }

    fun addLocation() {
        val newLoc = Location("")
        newLoc.latitude = 0.0  //your coords of course
        newLoc.longitude = 0.0
        locations.value?.add(newLoc)
        locations.postValue(locations.value)
    }

    fun removeLastLocation() {
        if (locations.value?.size!! > 1) {
            locations.value?.removeAt(locations.value!!.size - 1)
            locations.postValue(locations.value)
        }
    }


    val toolTypeCodeName: LiveData<String> = Transformations.map(toolTypeCode) { code ->
        code.getLocalizedName(getApplication())
    }

    //val toolTypeCodeName: LiveData<String>
    //    get() = this.selectedToolTypeCodeName

}