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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TableRow

abstract class BaseTableRow(context: Context?, layoutId: Int) {
    /**
     * Return the table row as a View
     *
     * @return The table row
     */
    val view: View

    var tag: Any?
        get() = view.tag
        set(tag) {
            view.tag = tag
        }

    fun setVisibility(visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected val context: Context
        get() = view.context

    abstract fun setEnabled(enabled: Boolean)

    init {
        val tableRow = TableRow(context)
        view = LayoutInflater.from(context).inflate(layoutId, tableRow, false)
    }
}