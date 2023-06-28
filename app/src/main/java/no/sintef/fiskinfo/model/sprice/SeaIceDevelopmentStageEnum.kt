package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

enum class SeaIceDevelopmentStageEnum(val code : String, val stringResource : Int) : IDropDownMenu {
    @SerializedName("ONLY_NEW_ICE")
    ONLY_NEW_ICE("ONLY_NEW_ICE", R.string.direction_to_nearest_sea_ice_edge_0),
    @SerializedName("THIN_ICE_LESS_THAN_10_CM")
    THIN_ICE_LESS_THAN_10_CM("THIN_ICE_LESS_THAN_10_CM", R.string.direction_to_nearest_sea_ice_edge_1),
    @SerializedName("YOUNG_ICE")
    YOUNG_ICE("YOUNG_ICE", R.string.direction_to_nearest_sea_ice_edge_2),
    @SerializedName("NEW_OR_YOUNG_ICE_WITH_SOME_FIRST_YEARS_ICE")
    NEW_OR_YOUNG_ICE_WITH_SOME_FIRST_YEARS_ICE("NEW_OR_YOUNG_ICE_WITH_SOME_FIRST_YEARS_ICE", R.string.direction_to_nearest_sea_ice_edge_3),
    @SerializedName("THIN_FIRST_YEARS_ICE_WITH_SOME_NEW_ICE")
    THIN_FIRST_YEARS_ICE_WITH_SOME_NEW_ICE("THIN_FIRST_YEARS_ICE_WITH_SOME_NEW_ICE", R.string.direction_to_nearest_sea_ice_edge_4),
    @SerializedName("FIRST_YEARS_ICE_30_TO_70_CM")
    FIRST_YEARS_ICE_30_TO_70_CM("FIRST_YEARS_ICE_30_TO_70_CM", R.string.direction_to_nearest_sea_ice_edge_5),
    @SerializedName("FIRST_YEARS_ICE_70_120_CM")
    FIRST_YEARS_ICE_70_120_CM("FIRST_YEARS_ICE_70_120_CM", R.string.direction_to_nearest_sea_ice_edge_6),
    @SerializedName("THICK_FIRST_YEARS_ICE_GREATER_THAN_120_CM")
    THICK_FIRST_YEARS_ICE_GREATER_THAN_120_CM("THICK_FIRST_YEARS_ICE_GREATER_THAN_120_CM", R.string.direction_to_nearest_sea_ice_edge_7),
    @SerializedName("MEDIUM_AND_THICK_FIRST_YEARS_ICE_WITH_SOME_OLD_ICE_GREATER_THAN_200_CM")
    MEDIUM_AND_THICK_FIRST_YEARS_ICE_WITH_SOME_OLD_ICE_GREATER_THAN_200_CM("MEDIUM_AND_THICK_FIRST_YEARS_ICE_WITH_SOME_OLD_ICE_GREATER_THAN_200_CM", R.string.direction_to_nearest_sea_ice_edge_8),
    @SerializedName("MOSTLY_OLD_ICE")
    MOSTLY_OLD_ICE("MOSTLY_OLD_ICE", R.string.direction_to_nearest_sea_ice_edge_9),
    @SerializedName("NOT_POSSIBLE_TO_REPORT")
    NOT_POSSIBLE_TO_REPORT("NOT_POSSIBLE_TO_REPORT", R.string.direction_to_nearest_sea_ice_edge_x)
    ;

    override fun getLocalizedName(context: Context): String {
        return context.resources.getString(stringResource)
    }

    override fun getFormValue(): String {
        val retval: String = when (this) {
            ONLY_NEW_ICE -> SEA_ICE_DEVELOPMENT_STAGE_0
            THIN_ICE_LESS_THAN_10_CM -> SEA_ICE_DEVELOPMENT_STAGE_1
            YOUNG_ICE -> SEA_ICE_DEVELOPMENT_STAGE_2
            NEW_OR_YOUNG_ICE_WITH_SOME_FIRST_YEARS_ICE -> SEA_ICE_DEVELOPMENT_STAGE_3
            THIN_FIRST_YEARS_ICE_WITH_SOME_NEW_ICE -> SEA_ICE_DEVELOPMENT_STAGE_4
            FIRST_YEARS_ICE_30_TO_70_CM -> SEA_ICE_DEVELOPMENT_STAGE_5
            FIRST_YEARS_ICE_70_120_CM -> SEA_ICE_DEVELOPMENT_STAGE_6
            THICK_FIRST_YEARS_ICE_GREATER_THAN_120_CM -> SEA_ICE_DEVELOPMENT_STAGE_7
            MEDIUM_AND_THICK_FIRST_YEARS_ICE_WITH_SOME_OLD_ICE_GREATER_THAN_200_CM -> SEA_ICE_DEVELOPMENT_STAGE_8
            MOSTLY_OLD_ICE -> SEA_ICE_DEVELOPMENT_STAGE_9
            NOT_POSSIBLE_TO_REPORT -> SEA_ICE_DEVELOPMENT_STAGE_x
        }

        return retval
    }

    override fun getFormIndex(): String {
        val retval: String = when (this) {
            ONLY_NEW_ICE -> SEA_ICE_DEVELOPMENT_STAGE_0_INDEX
            THIN_ICE_LESS_THAN_10_CM -> SEA_ICE_DEVELOPMENT_STAGE_1_INDEX
            YOUNG_ICE -> SEA_ICE_DEVELOPMENT_STAGE_2_INDEX
            NEW_OR_YOUNG_ICE_WITH_SOME_FIRST_YEARS_ICE -> SEA_ICE_DEVELOPMENT_STAGE_3_INDEX
            THIN_FIRST_YEARS_ICE_WITH_SOME_NEW_ICE -> SEA_ICE_DEVELOPMENT_STAGE_4_INDEX
            FIRST_YEARS_ICE_30_TO_70_CM -> SEA_ICE_DEVELOPMENT_STAGE_5_INDEX
            FIRST_YEARS_ICE_70_120_CM -> SEA_ICE_DEVELOPMENT_STAGE_6_INDEX
            THICK_FIRST_YEARS_ICE_GREATER_THAN_120_CM -> SEA_ICE_DEVELOPMENT_STAGE_7_INDEX
            MEDIUM_AND_THICK_FIRST_YEARS_ICE_WITH_SOME_OLD_ICE_GREATER_THAN_200_CM -> SEA_ICE_DEVELOPMENT_STAGE_8_INDEX
            MOSTLY_OLD_ICE -> SEA_ICE_DEVELOPMENT_STAGE_9_INDEX
            NOT_POSSIBLE_TO_REPORT -> SEA_ICE_DEVELOPMENT_STAGE_x_INDEX
        }

        return retval
    }

    companion object {
        const val SEA_ICE_DEVELOPMENT_STAGE_0 = "0-Bare ny is "
        const val SEA_ICE_DEVELOPMENT_STAGE_1 = "1-Tynn is under 10 cm  "
        const val SEA_ICE_DEVELOPMENT_STAGE_2 = "2-Ung is  "
        const val SEA_ICE_DEVELOPMENT_STAGE_3 = "3-Ny/ung is med noe første års is "
        const val SEA_ICE_DEVELOPMENT_STAGE_4 = "4-Tynn første års is med noe ny is   "
        const val SEA_ICE_DEVELOPMENT_STAGE_5 = "5-Første års is 30-70 cm  "
        const val SEA_ICE_DEVELOPMENT_STAGE_6 = "6-Første års is 70-120 cm "
        const val SEA_ICE_DEVELOPMENT_STAGE_7 = "7-Fykk første års is > 120cm "
        const val SEA_ICE_DEVELOPMENT_STAGE_8 = "8-Medium og tykk første års is med noe gammel is > 200cm "
        const val SEA_ICE_DEVELOPMENT_STAGE_9 = "9-Mest gammel is "
        const val SEA_ICE_DEVELOPMENT_STAGE_x = "/-Ikke mulig å raportere"

        const val SEA_ICE_DEVELOPMENT_STAGE_0_INDEX = "0"
        const val SEA_ICE_DEVELOPMENT_STAGE_1_INDEX = "1"
        const val SEA_ICE_DEVELOPMENT_STAGE_2_INDEX = "2"
        const val SEA_ICE_DEVELOPMENT_STAGE_3_INDEX = "3"
        const val SEA_ICE_DEVELOPMENT_STAGE_4_INDEX = "4"
        const val SEA_ICE_DEVELOPMENT_STAGE_5_INDEX = "5"
        const val SEA_ICE_DEVELOPMENT_STAGE_6_INDEX = "6"
        const val SEA_ICE_DEVELOPMENT_STAGE_7_INDEX = "7"
        const val SEA_ICE_DEVELOPMENT_STAGE_8_INDEX = "8"
        const val SEA_ICE_DEVELOPMENT_STAGE_9_INDEX = "9"
        const val SEA_ICE_DEVELOPMENT_STAGE_x_INDEX = "/"
    }
}