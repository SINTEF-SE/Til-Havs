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

import android.app.Dialog
import android.content.Context
import android.view.WindowManager

class UtilityDialogs : DialogInterface {
    override fun getDialog(
        context: Context?,
        layoutId: Int,
        titleId: Int
    ): Dialog? {
        if (context == null)
            return null
        val dialog = Dialog(context)
        dialog.setContentView(layoutId)
        dialog.setTitle(titleId)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return dialog
    }

    override fun getDialog(
        context: Context?,
        layoutId: Int,
        title: String?
    ): Dialog? {
        if (context == null)
            return null
        val dialog = Dialog(context)
        dialog.setContentView(layoutId)
        dialog.setTitle(title)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return dialog
    }
}