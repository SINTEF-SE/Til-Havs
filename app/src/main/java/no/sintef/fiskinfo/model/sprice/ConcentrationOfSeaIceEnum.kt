package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

enum class ConcentrationOfSeaIceEnum(val code : String, val stringResource : Int) : IDropDownMenu {
    @SerializedName("NO_SEA_ICE_IN_SIGHT")
    NO_SEA_ICE_IN_SIGHT("NO_SEA_ICE_IN_SIGHT", R.string.concentration_of_sea_ice_0),
    @SerializedName("SHIP_IN_OPEN_LEAD_FURTHER_THAN_1_NM")
    SHIP_IN_OPEN_LEAD_FURTHER_THAN_1_NM("SHIP_IN_OPEN_LEAD_FURTHER_THAN_1_NM", R.string.concentration_of_sea_ice_1),
    @SerializedName("OPEN_PACK_ICE_WITH_WATER_IN_BETWEEN")
    OPEN_PACK_ICE_WITH_WATER_IN_BETWEEN("OPEN_PACK_ICE_WITH_WATER_IN_BETWEEN", R.string.concentration_of_sea_ice_2),
    @SerializedName("FOUR_TO_SIX_OF_TEN_PARTS_OPEN_PACK_ICE")
    FOUR_TO_SIX_OF_TEN_PARTS_OPEN_PACK_ICE("FOUR_TO_SIX_OF_TEN_PARTS_OPEN_PACK_ICE", R.string.concentration_of_sea_ice_3),
    @SerializedName("SEVEN_TO_EIGHT_OF_10_PARTS_CLOSED_PACK_ICE")
    SEVEN_TO_EIGHT_OF_10_PARTS_CLOSED_PACK_ICE("SEVEN_TO_EIGHT_OF_10_PARTS_CLOSED_PACK_ICE", R.string.concentration_of_sea_ice_4),
    @SerializedName("NINE_TO_TEN_PARTS_COVER_OF_CLOSE_PACK_ICE")
    NINE_TO_TEN_PARTS_COVER_OF_CLOSE_PACK_ICE("NINE_TO_TEN_PARTS_COVER_OF_CLOSE_PACK_ICE", R.string.concentration_of_sea_ice_5),
    @SerializedName("AREAS_OF_PACK_ICE_WITH_LIGHTER_PACK_ICE_IN_BETWEEN")
    AREAS_OF_PACK_ICE_WITH_LIGHTER_PACK_ICE_IN_BETWEEN("AREAS_OF_PACK_ICE_WITH_LIGHTER_PACK_ICE_IN_BETWEEN", R.string.concentration_of_sea_ice_6),
    @SerializedName("AREAS_OF_PACK_ICE_WITH_DENSE_PACK_ICE")
    AREAS_OF_PACK_ICE_WITH_DENSE_PACK_ICE("AREAS_OF_PACK_ICE_WITH_DENSE_PACK_ICE", R.string.concentration_of_sea_ice_7),
    @SerializedName("FAST_ICE_WITH_OPEN_WATER")
    FAST_ICE_WITH_OPEN_WATER("FAST_ICE_WITH_OPEN_WATER", R.string.concentration_of_sea_ice_8),
    @SerializedName("FAST_ICE_WITH_CLOSE_PACK_ICE")
    FAST_ICE_WITH_CLOSE_PACK_ICE("FAST_ICE_WITH_CLOSE_PACK_ICE", R.string.concentration_of_sea_ice_9),
    @SerializedName("NOT_POSSIBLE_TO_REPORT")
    NOT_POSSIBLE_TO_REPORT("NOT_POSSIBLE_TO_REPORT", R.string.concentration_of_sea_ice_10);

    override fun getLocalizedName(context: Context): String {
        return context.resources.getString(stringResource)
    }

    override fun getFormValue(): String {
        val retval: String = when (this) {
            NO_SEA_ICE_IN_SIGHT -> CONCENTRATION_OF_SEA_ICE_0
            SHIP_IN_OPEN_LEAD_FURTHER_THAN_1_NM -> CONCENTRATION_OF_SEA_ICE_1
            OPEN_PACK_ICE_WITH_WATER_IN_BETWEEN -> CONCENTRATION_OF_SEA_ICE_2
            FOUR_TO_SIX_OF_TEN_PARTS_OPEN_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_3
            SEVEN_TO_EIGHT_OF_10_PARTS_CLOSED_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_4
            NINE_TO_TEN_PARTS_COVER_OF_CLOSE_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_5
            AREAS_OF_PACK_ICE_WITH_LIGHTER_PACK_ICE_IN_BETWEEN -> CONCENTRATION_OF_SEA_ICE_6
            AREAS_OF_PACK_ICE_WITH_DENSE_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_7
            FAST_ICE_WITH_OPEN_WATER -> CONCENTRATION_OF_SEA_ICE_8
            FAST_ICE_WITH_CLOSE_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_9
            NOT_POSSIBLE_TO_REPORT -> CONCENTRATION_OF_SEA_ICE_10
        }

        return retval
    }

    override fun getFormIndex(): String {
        var retval = ""

        when (this) {
            NO_SEA_ICE_IN_SIGHT -> CONCENTRATION_OF_SEA_ICE_0_INDEX
            SHIP_IN_OPEN_LEAD_FURTHER_THAN_1_NM -> CONCENTRATION_OF_SEA_ICE_1_INDEX
            OPEN_PACK_ICE_WITH_WATER_IN_BETWEEN -> CONCENTRATION_OF_SEA_ICE_2_INDEX
            FOUR_TO_SIX_OF_TEN_PARTS_OPEN_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_3_INDEX
            SEVEN_TO_EIGHT_OF_10_PARTS_CLOSED_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_4_INDEX
            NINE_TO_TEN_PARTS_COVER_OF_CLOSE_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_5_INDEX
            AREAS_OF_PACK_ICE_WITH_LIGHTER_PACK_ICE_IN_BETWEEN -> CONCENTRATION_OF_SEA_ICE_6_INDEX
            AREAS_OF_PACK_ICE_WITH_DENSE_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_7_INDEX
            FAST_ICE_WITH_OPEN_WATER -> CONCENTRATION_OF_SEA_ICE_8_INDEX
            FAST_ICE_WITH_CLOSE_PACK_ICE -> CONCENTRATION_OF_SEA_ICE_9_INDEX
            NOT_POSSIBLE_TO_REPORT -> CONCENTRATION_OF_SEA_ICE_10_INDEX
        }

        return retval
    }

    companion object {
        const val CONCENTRATION_OF_SEA_ICE_0 = "No sea ice in sight"
        const val CONCENTRATION_OF_SEA_ICE_1 = "Ship in open lead further than 1 NM"
        const val CONCENTRATION_OF_SEA_ICE_2 = "Open pack ice, with water in between"
        const val CONCENTRATION_OF_SEA_ICE_3 = "4 to 6 10 parts open pack of ice"
        const val CONCENTRATION_OF_SEA_ICE_4 = "7 and 8 10 parts closed pack ice"
        const val CONCENTRATION_OF_SEA_ICE_5 = "9/10 cover of dense pack ice"
        const val CONCENTRATION_OF_SEA_ICE_6 = "Areas of pack ice with lighter pack ice in between"
        const val CONCENTRATION_OF_SEA_ICE_7 = "Areas of pack ice with dense pack ice"
        const val CONCENTRATION_OF_SEA_ICE_8 = "Fast ice with open water"
        const val CONCENTRATION_OF_SEA_ICE_9 = "Fast ice with dense pack ice"
        const val CONCENTRATION_OF_SEA_ICE_10 = "Not possible to report"

        const val CONCENTRATION_OF_SEA_ICE_0_INDEX = "0"
        const val CONCENTRATION_OF_SEA_ICE_1_INDEX = "1"
        const val CONCENTRATION_OF_SEA_ICE_2_INDEX = "2"
        const val CONCENTRATION_OF_SEA_ICE_3_INDEX = "3"
        const val CONCENTRATION_OF_SEA_ICE_4_INDEX = "4"
        const val CONCENTRATION_OF_SEA_ICE_5_INDEX = "5"
        const val CONCENTRATION_OF_SEA_ICE_6_INDEX = "6"
        const val CONCENTRATION_OF_SEA_ICE_7_INDEX = "7"
        const val CONCENTRATION_OF_SEA_ICE_8_INDEX = "8"
        const val CONCENTRATION_OF_SEA_ICE_9_INDEX = "9"
        const val CONCENTRATION_OF_SEA_ICE_10_INDEX = "/"
    }
}