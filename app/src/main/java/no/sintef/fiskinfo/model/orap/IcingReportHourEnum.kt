package no.sintef.fiskinfo.model.orap

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

enum class IcingReportHourEnum(val code : String, val stringResource : Int) : IDropDownMenu {
    @SerializedName("NOT_SELECTED")
    NOT_SELECTED("NOT_SELECTED", R.string.sprice_icing_reporting_hour_not_selected),

    @SerializedName("00:00")
    HOUR_00("01:00", R.string.sprice_icing_reporting_hour_00),
    @SerializedName("01:00")
    HOUR_01("01:00", R.string.sprice_icing_reporting_hour_01),
    @SerializedName("02:00")
    HOUR_02("02:00", R.string.sprice_icing_reporting_hour_02),
    @SerializedName("03:00")
    HOUR_03("03:00", R.string.sprice_icing_reporting_hour_03),
    @SerializedName("04:00")
    HOUR_04("04:00", R.string.sprice_icing_reporting_hour_04),
    @SerializedName("05:00")
    HOUR_05("05:00", R.string.sprice_icing_reporting_hour_05),
    @SerializedName("06:00")
    HOUR_06("06:00", R.string.sprice_icing_reporting_hour_06),
    @SerializedName("07:00")
    HOUR_07("07:00", R.string.sprice_icing_reporting_hour_07),
    @SerializedName("08:00")
    HOUR_08("08:00", R.string.sprice_icing_reporting_hour_08),
    @SerializedName("09:00")
    HOUR_09("09:00", R.string.sprice_icing_reporting_hour_09),
    @SerializedName("10:00")
    HOUR_10("10:00", R.string.sprice_icing_reporting_hour_10),
    @SerializedName("11:00")
    HOUR_11("11:00", R.string.sprice_icing_reporting_hour_11),
    @SerializedName("12:00")
    HOUR_12("12:00", R.string.sprice_icing_reporting_hour_12),
    @SerializedName("13:00")
    HOUR_13("13:00", R.string.sprice_icing_reporting_hour_13),
    @SerializedName("14:00")
    HOUR_14("14:00", R.string.sprice_icing_reporting_hour_14),
    @SerializedName("15:00")
    HOUR_15("15:00", R.string.sprice_icing_reporting_hour_15),
    @SerializedName("16:00")
    HOUR_16("16:00", R.string.sprice_icing_reporting_hour_16),
    @SerializedName("17:00")
    HOUR_17("17:00", R.string.sprice_icing_reporting_hour_17),
    @SerializedName("18:00")
    HOUR_18("18:00", R.string.sprice_icing_reporting_hour_18),
    @SerializedName("19:00")
    HOUR_19("19:00", R.string.sprice_icing_reporting_hour_19),
    @SerializedName("20:00")
    HOUR_20("20:00", R.string.sprice_icing_reporting_hour_20),
    @SerializedName("21:00")
    HOUR_21("21:00", R.string.sprice_icing_reporting_hour_21),
    @SerializedName("22:00")
    HOUR_22("22:00", R.string.sprice_icing_reporting_hour_22),
    @SerializedName("23:00")
    HOUR_23("23:00", R.string.sprice_icing_reporting_hour_23);

    override fun getLocalizedName(context : Context):String {
        return context.resources.getString(stringResource)
    }

    override fun getFormValue(): String {
        var retval = ""

        when (this) {
            NOT_SELECTED -> retval = ICING_REPORTING_HOUR_NOT_SELECTED_FORM_VALUE
            HOUR_00 -> retval = ICING_REPORTING_HOUR_0000
            HOUR_01 -> retval = ICING_REPORTING_HOUR_0100
            HOUR_02 -> retval = ICING_REPORTING_HOUR_0200
            HOUR_03 -> retval = ICING_REPORTING_HOUR_0300
            HOUR_04 -> retval = ICING_REPORTING_HOUR_0400
            HOUR_05 -> retval = ICING_REPORTING_HOUR_0500
            HOUR_06 -> retval = ICING_REPORTING_HOUR_0600
            HOUR_07 -> retval = ICING_REPORTING_HOUR_0700
            HOUR_08 -> retval = ICING_REPORTING_HOUR_0800
            HOUR_09 -> retval = ICING_REPORTING_HOUR_0900
            HOUR_10 -> retval = ICING_REPORTING_HOUR_1000
            HOUR_11 -> retval = ICING_REPORTING_HOUR_1100
            HOUR_12 -> retval = ICING_REPORTING_HOUR_1200
            HOUR_13 -> retval = ICING_REPORTING_HOUR_1300
            HOUR_14 -> retval = ICING_REPORTING_HOUR_1400
            HOUR_15 -> retval = ICING_REPORTING_HOUR_1500
            HOUR_16 -> retval = ICING_REPORTING_HOUR_1600
            HOUR_17 -> retval = ICING_REPORTING_HOUR_1700
            HOUR_18 -> retval = ICING_REPORTING_HOUR_1800
            HOUR_19 -> retval = ICING_REPORTING_HOUR_1900
            HOUR_20 -> retval = ICING_REPORTING_HOUR_2000
            HOUR_21 -> retval = ICING_REPORTING_HOUR_2100
            HOUR_22 -> retval = ICING_REPORTING_HOUR_2200
            HOUR_23 -> retval = ICING_REPORTING_HOUR_2300
        }

        return retval
    }

    companion object {
        const val ICING_REPORTING_HOUR_NOT_SELECTED_FORM_VALUE = ""
        const val ICING_REPORTING_HOUR_0000 = "00:00"
        const val ICING_REPORTING_HOUR_0100 = "01:00"
        const val ICING_REPORTING_HOUR_0200 = "02:00"
        const val ICING_REPORTING_HOUR_0300 = "03:00"
        const val ICING_REPORTING_HOUR_0400 = "04:00"
        const val ICING_REPORTING_HOUR_0500 = "05:00"
        const val ICING_REPORTING_HOUR_0600 = "06:00"
        const val ICING_REPORTING_HOUR_0700 = "07:00"
        const val ICING_REPORTING_HOUR_0800 = "08:00"
        const val ICING_REPORTING_HOUR_0900 = "09:00"
        const val ICING_REPORTING_HOUR_1000 = "10:00"
        const val ICING_REPORTING_HOUR_1100 = "11:00"
        const val ICING_REPORTING_HOUR_1200 = "12:00"
        const val ICING_REPORTING_HOUR_1300 = "13:00"
        const val ICING_REPORTING_HOUR_1400 = "14:00"
        const val ICING_REPORTING_HOUR_1500 = "15:00"
        const val ICING_REPORTING_HOUR_1600 = "16:00"
        const val ICING_REPORTING_HOUR_1700 = "17:00"
        const val ICING_REPORTING_HOUR_1800 = "18:00"
        const val ICING_REPORTING_HOUR_1900 = "19:00"
        const val ICING_REPORTING_HOUR_2000 = "20:00"
        const val ICING_REPORTING_HOUR_2100 = "21:00"
        const val ICING_REPORTING_HOUR_2200 = "22:00"
        const val ICING_REPORTING_HOUR_2300 = "23:00"
    }
}