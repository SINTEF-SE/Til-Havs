package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

enum class ReasonForIcingOnVesselOrPlatformEnum(val code : String, val stringResource : Int) : IDropDownMenu {
    @SerializedName("NOT_SELECTED")
    NOT_SELECTED("NOT_SELECTED", R.string.enum_not_selected),
    @SerializedName("ICING_FROM_SEA_SPRAY")
    ICING_FROM_SEA_SPRAY("ICING_FROM_SEA_SPRAY", R.string.reason_for_icing_on_vessel_or_platform_1),
    @SerializedName("ICING_FROM_FOG")
    ICING_FROM_FOG("ICING_FROM_FOG", R.string.reason_for_icing_on_vessel_or_platform_2),
    @SerializedName("ICING_FROM_SEA_SPRAY_AND_FOG")
    ICING_FROM_SEA_SPRAY_AND_FOG("ICING_FROM_SEA_SPRAY_AND_FOG", R.string.reason_for_icing_on_vessel_or_platform_3),
    @SerializedName("ICING_FROM_RAIN")
    ICING_FROM_RAIN("ICING_FROM_RAIN", R.string.reason_for_icing_on_vessel_or_platform_4),
    @SerializedName("ICING_FROM_SEA_SPRAY_AND_RAIN")
    ONLY_NEW_ICE("ICING_FROM_SEA_SPRAY_AND_RAIN", R.string.reason_for_icing_on_vessel_or_platform_5),
    @SerializedName("ICING_FROM_SEA_SPRAY_AND_RAIN")
    FREEZING_RAIN("FREEZING_RAIN", R.string.reason_for_icing_on_vessel_or_platform_6),
    @SerializedName("SUB_COOLED_RAIN_AND_SEA_SPRAY_IN_COMBINATION")
    SUB_COOLED_RAIN_AND_SEA_SPRAY_IN_COMBINATION("SUB_COOLED_RAIN_AND_SEA_SPRAY_IN_COMBINATION", R.string.reason_for_icing_on_vessel_or_platform_7);

    override fun getLocalizedName(context: Context): String {
        return context.resources.getString(stringResource)
    }

    override fun getFormValue(): String {
        val retval: String = when (this) {
            ICING_FROM_SEA_SPRAY -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_1
            ICING_FROM_FOG -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_2
            ICING_FROM_SEA_SPRAY_AND_FOG -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_3
            ICING_FROM_RAIN -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_4
            ONLY_NEW_ICE -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_5
            FREEZING_RAIN -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_6
            SUB_COOLED_RAIN_AND_SEA_SPRAY_IN_COMBINATION -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_7
            NOT_SELECTED -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_NOT_SELECTED
        }

        return retval
    }

    override fun getFormIndex(): String {
        val retval: String = when (this) {
            ICING_FROM_SEA_SPRAY -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_1_INDEX
            ICING_FROM_FOG -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_2_INDEX
            ICING_FROM_SEA_SPRAY_AND_FOG -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_3_INDEX
            ICING_FROM_RAIN -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_4_INDEX
            ONLY_NEW_ICE -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_5_INDEX
            FREEZING_RAIN -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_6_INDEX
            SUB_COOLED_RAIN_AND_SEA_SPRAY_IN_COMBINATION -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_7_INDEX
            NOT_SELECTED -> REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_NOT_SELECTED_INDEX
        }

        return retval
    }

    companion object {
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_NOT_SELECTED = "Ikke valgt"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_1 = "1 - bare sjøsprøyt"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_2 = "2 - bare snø/sludd"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_3 = "3 - sjøsprøyt og snø/sludd i kombinasjon"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_4 = "4 - bare tåke"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_5 = "5 - tåke og sjøsprøyt i kombinasjon"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_6 = "6- underkjølt regn"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_7 = "7 - underkjølt regn og sjøsprøyt i kombinasjon"

        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_NOT_SELECTED_INDEX = ""
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_1_INDEX = "1"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_2_INDEX = "2"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_3_INDEX = "3"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_4_INDEX = "4"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_5_INDEX = "5"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_6_INDEX = "6"
        const val REASON_FOR_ICING_ON_VESSEL_OR_PLATFORM_7_INDEX = "7"
    }
}