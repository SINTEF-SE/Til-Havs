package no.sintef.fiskinfo.ui.sprice

import android.content.Context

interface IDropDownMenu {
    fun getLocalizedName(context : Context):String
    fun getFormValue(): String
    fun getFormIndex(): String
}