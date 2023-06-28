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
package no.sintef.fiskinfo.utilities.ui

import android.widget.TextView
import no.sintef.fiskinfo.R

class IntRangeValidator(
    textView: TextView,
    private val low: Int,
    private val includeLow: Boolean,
    private val high: Int,
    private val includeHigh: Boolean,
    private val updateAction: (Double) -> (Unit)
) : TextValidator(textView) {
    override fun validateAndUpdate(textView: TextView?, text: String?):Boolean {
        val num: Double;

        try {
            // TODO: Consider locale adapted parsing
            num = text.toString().toDouble()
            val withinLow = if (includeLow) num >= low else num > low
            val withinHigh = if (includeHigh) num <= high else num < high

            if (! (withinHigh && withinLow))
                textView?.error = "${textView?.context?.getString(R.string.util_value_must_be_in_range)} ${low}-${high}"
            else {
                textView?.error = null
                updateAction(num)
            }
        } catch (nfe: NumberFormatException) {
            textView?.error = textView?.context?.getString(R.string.util_value_must_be_in_range) + low +
                    textView?.context?.getString(R.string.util_range_to_separator) + high
            //println("Could not parse $nfe")
        }
        return (textView?.error == null)
    }
}
