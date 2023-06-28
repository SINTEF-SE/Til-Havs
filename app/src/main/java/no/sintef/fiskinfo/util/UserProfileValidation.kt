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

import android.content.Context
import androidx.preference.PreferenceManager
import no.sintef.fiskinfo.R


    // TODO: Add better validations than only on empty string

    private fun isEmailValid(email : String) :Boolean {
        return email.trim() != ""
    }

    private fun isPhoneValid(phone : String) :Boolean {
        return phone.trim() != ""
    }

    private fun isNameValid(name : String) :Boolean {
        return name.trim() != ""
    }

    fun isUserProfileValid(context: Context):Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var email = prefs.getString(context.getString(R.string.pref_contact_person_email), "")!!
        var phone = prefs.getString(context.getString(R.string.pref_contact_person_phone), "")!!
        var name = prefs.getString(context.getString(R.string.pref_contact_person_name), "")!!
        return isEmailValid(email) && isNameValid(name) && isPhoneValid(phone)
    }


