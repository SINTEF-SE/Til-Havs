//@file:JvmName("DoubleConverter")
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
package no.sintef.fiskinfo.util

import android.content.res.Configuration
import android.os.Build
import android.view.View

import android.widget.TextView

import androidx.databinding.InverseMethod
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
/*

object DoubleConverter {
@InverseMethod("stringToDouble")
@JvmStatic fun doubleToString(
    oldValue: Double,
    value: Double
): String {
    return value.toString()
    val numberFormat: NumberFormat = getNumberFormat(view)
    try {
        // Don't return a different value if the parsed value
        // doesn't change
        val inView = view.text.toString()
        val parsed: Double = numberFormat.parse(inView).toDouble()
        if (parsed == value) {
            return view.text.toString()
        }
    } catch (e: ParseException) {
        // Old number was broken
    }
    return numberFormat.format(value)
}

@JvmStatic fun  stringToDouble(
    oldValue: Double,
    value: String
): Double {
    val numberFormat: NumberFormat = NumberFormat.getNumberInstance()
    return try {
        numberFormat.parse(value).toDouble()
    } catch (e: ParseException) {
        //val resources: Resources = view.resources
        //val errStr: String = "not a valid number" //resources.getString(R.string.badNumber)
        //view.error = errStr
        oldValue
    }
}


fun getNumberFormat(view: View): NumberFormat {
    val resources: Resources = view.getResources()
    val locale: Locale = resources.getConfiguration().locale
    val format: NumberFormat = NumberFormat.getNumberInstance(locale)
    if (format is DecimalFormat) {
        val decimalFormat: DecimalFormat = format as DecimalFormat
        decimalFormat.setGroupingUsed(false)
    }
    return format
}
*/
/*
    object DoubleConverter {
        @InverseMethod("stringToDouble")
        @JvmStatic fun doubleToString(
            //view: EditText,
            value: Double
        ): String {
            val numberFormat: NumberFormat = NumberFormat.getNumberInstance(); //getNumberFormat(view)*/
/*            try {
                // Don't return a different value if the parsed value
                // doesn't change
                val inView = view.text.toString()
                val parsed: Double = numberFormat.parse(inView).toDouble()
                if (parsed == value) {
                    return view.text.toString()
                }
            } catch (e: ParseException) {
                // Old number was broken
            }*/
/*            return numberFormat.format(value)
        }

        @JvmStatic fun  stringToDouble(
            //view: EditText,
            value: String
        ): Double {
            val numberFormat: NumberFormat =  NumberFormat.getNumberInstance(); //getNumberFormat(view)
            return try {
                numberFormat.parse(value).toDouble()
            } catch (e: ParseException) {
                //val resources: Resources = view.resources
                //val errStr: String = "not a valid number" //resources.getString(R.string.badNumber)
                //view.error = errStr
                0.0
                //oldValue.toDouble()
            }
        }


        @JvmStatic  fun getNumberFormat(view: View): NumberFormat {
            val resources: Resources = view.getResources()
            val locale: Locale = resources.getConfiguration().locale
            val format: NumberFormat = NumberFormat.getNumberInstance(locale)
            if (format is DecimalFormat) {
                val decimalFormat: DecimalFormat = format as DecimalFormat
                decimalFormat.setGroupingUsed(false)
            }
            return format
        }

}
*/


object DoubleConverter {
    @InverseMethod("stringToDouble")
    @JvmStatic fun doubleToString(
        view: TextView,
        value: Double
    ): String {
        val numberFormat: NumberFormat = getNumberFormat(view)
        try {
            // Don't return a different value if the parsed value
            // doesn't change
            val inView = view.text.toString()
            val parsed: Double = numberFormat.parse(inView)!!.toDouble()
            if (parsed == value) {
                return view.text.toString()
            }
        } catch (e: ParseException) {
            // Old number was broken
        }
        return numberFormat.format(value)
    }

    @JvmStatic fun stringToDouble(
        view: TextView, oldValue: Double,
        value: String
    ): Double {
        val numberFormat: NumberFormat = getNumberFormat(view)
        return try {
            numberFormat.parse(value)!!.toDouble()
        } catch (e: ParseException) {
            val errStr = "not a valid number" //resources.getString(R.string.badNumber)
            view.error = errStr
            oldValue
        }
    }

    private fun getNumberFormat(view: View): NumberFormat {
        val configuration: Configuration = view.resources.configuration

        val locale: Locale = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }

        val format: NumberFormat = NumberFormat.getNumberInstance(locale)
        if (format is DecimalFormat) {
            val decimalFormat: DecimalFormat = format
            decimalFormat.isGroupingUsed = false
        }
        return format
    }
}

