package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

enum class DegreeOfIcingEnum (val code : String, val stringResource : Int) : IDropDownMenu {
    @SerializedName("NOT_SELECTED")
    NOT_SELECTED("NOT_SELECTED", R.string.enum_not_selected),
    @SerializedName("NO_ICING")
    NO_ICING("NO_ICING", R.string.degree_of_icing_0),
    @SerializedName("LIGHT_ICING")
    LIGHT_ICING("LIGHT_ICING", R.string.degree_of_icing_1),
    @SerializedName("MODERATE_ICING")
    MODERATE_ICING("MODERATE_ICING", R.string.degree_of_icing_2),
    @SerializedName("HEAVY_ICING")
    HEAVY_ICING("HEAVY_ICING", R.string.degree_of_icing_3);

    override fun getLocalizedName(context: Context): String {
        return context.resources.getString(stringResource)
    }

    override fun getFormValue(): String {
        return when (this) {
            NO_ICING -> DEGREE_OF_ICING_0
            LIGHT_ICING -> DEGREE_OF_ICING_1
            MODERATE_ICING -> DEGREE_OF_ICING_2
            HEAVY_ICING -> DEGREE_OF_ICING_3
            NOT_SELECTED -> DEGREE_OF_ICING_NOT_SELECTED
        }
    }

    override fun getFormIndex(): String {
        return when (this) {
            NO_ICING -> DEGREE_OF_ICING_0_INDEX
            LIGHT_ICING -> DEGREE_OF_ICING_1_INDEX
            MODERATE_ICING -> DEGREE_OF_ICING_2_INDEX
            HEAVY_ICING -> DEGREE_OF_ICING_3_INDEX
            NOT_SELECTED -> DEGREE_OF_ICING_NOT_SELECTED_INDEX
        }
    }

    companion object {
        const val DEGREE_OF_ICING_NOT_SELECTED = "Ikke valgt"
        const val DEGREE_OF_ICING_0 = "No icing"
        const val DEGREE_OF_ICING_1 = "light icing"
        const val DEGREE_OF_ICING_2 = "Moderate icing"
        const val DEGREE_OF_ICING_3 = "Heavy icing"

        const val DEGREE_OF_ICING_NOT_SELECTED_INDEX = ""
        const val DEGREE_OF_ICING_0_INDEX = "0"
        const val DEGREE_OF_ICING_1_INDEX = "1"
        const val DEGREE_OF_ICING_2_INDEX = "2"
        const val DEGREE_OF_ICING_3_INDEX = "3"
    }
}