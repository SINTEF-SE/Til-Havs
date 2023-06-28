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

import android.location.Location
import android.location.Location.*
import androidx.lifecycle.MutableLiveData
import no.sintef.fiskinfo.util.DDMLocation
import kotlin.math.abs
import kotlin.math.floor


class LocationDdmViewModel : LocationViewModel() {
    var format = FORMAT_MINUTES
    var ddmLocation = MutableLiveData<DDMLocation>();

    override fun setNewLocation(location : Location) {
        ddmLocation.value = DDMLocation.fromLocation(location)
    }

    private fun validateLocation():Boolean {
        return (true)
    }

    override fun getLocation():Location? {
        if (!validateLocation())
            return null

        return ddmLocation.value!!.toLocation()
    }
}