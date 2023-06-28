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
import android.location.Location
import android.location.Location.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.sintef.fiskinfo.utilities.ui.ObservableAndroidViewModel
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.floor


abstract class LocationViewModel : ViewModel() {
    var listPosition = 0;

    open fun initWithLocation(location : Location, index : Int) {
        listPosition = index;
        setNewLocation(location)
    }

    abstract fun setNewLocation(location : Location)
    abstract fun getLocation():Location?

}