package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

enum class ChangeInIcingOnVesselOrPlatformEnum(val code : String, val stringResource : Int) : IDropDownMenu {
    @SerializedName("NOT_SELECTED")
    NOT_SELECTED("NOT_SELECTED", R.string.enum_not_selected),
    @SerializedName("ICE_THAT_DOESNT_BUILD_UP")
    ICE_THAT_DOESNT_BUILD_UP("ICE_THAT_DOESNT_BUILD_UP", R.string.max_middle_wind_time_0),
    @SerializedName("ICE_THAT_DOESNT_BUILD_UP")
    ICE_THAT_BUILDS_UP_SLOWLY("ICE_THAT_BUILDS_UP_SLOWLY", R.string.max_middle_wind_time_1),
    @SerializedName("ICE_THAT_DOESNT_BUILD_UP")
    ICE_THAT_BUILDS_UP_FAST("ICE_THAT_BUILDS_UP_FAST", R.string.max_middle_wind_time_2),
    @SerializedName("ICE_THAT_DOESNT_BUILD_UP")
    ICE_THAT_MELTS_OR_BREAKS_UP_SLOWLY("ICE_THAT_MELTS_OR_BREAKS_UP_SLOWLY", R.string.max_middle_wind_time_3),
    @SerializedName("ICE_THAT_DOESNT_BUILD_UP")
    ICE_THAT_MELTS_OR_BREAKS_UP_FAST("ICE_THAT_MELTS_OR_BREAKS_UP_FAST", R.string.max_middle_wind_time_4),;

    override fun getLocalizedName(context: Context): String {
        return context.resources.getString(stringResource)
    }

    override fun getFormValue(): String {
        val retval: String = when (this) {
            ICE_THAT_DOESNT_BUILD_UP -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_0
            ICE_THAT_BUILDS_UP_SLOWLY -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_1
            ICE_THAT_BUILDS_UP_FAST -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_2
            ICE_THAT_MELTS_OR_BREAKS_UP_SLOWLY -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_3
            ICE_THAT_MELTS_OR_BREAKS_UP_FAST -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_4
            NOT_SELECTED -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_NOT_SELECTED
        }

        return retval
    }

    override fun getFormIndex(): String {
        val retval: String = when (this) {
            ICE_THAT_DOESNT_BUILD_UP -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_0_INDEX
            ICE_THAT_BUILDS_UP_SLOWLY -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_1_INDEX
            ICE_THAT_BUILDS_UP_FAST -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_2_INDEX
            ICE_THAT_MELTS_OR_BREAKS_UP_SLOWLY -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_3_INDEX
            ICE_THAT_MELTS_OR_BREAKS_UP_FAST -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_4_INDEX
            NOT_SELECTED -> CHANGE_IN_VESSEL_ICING_FORM_VALUE_NOT_SELECTED_INDEX
        }

        return retval
    }

    companion object {
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_NOT_SELECTED = "Ikke valgt"
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_0 = "0- Is som ikke bygger seg opp "
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_1 = "1- Is som bygger seg sakte opp "
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_2 = "2- Is som bygger seg raskt opp "
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_3 = "3- Is som smelter eller brekker opp sakte "
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_4 = "4- Is som smelter eller brekker opp raskt"

        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_NOT_SELECTED_INDEX = ""
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_0_INDEX = "0"
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_1_INDEX = "1"
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_2_INDEX = "2"
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_3_INDEX = "3"
        const val CHANGE_IN_VESSEL_ICING_FORM_VALUE_4_INDEX = "4"
    }
}