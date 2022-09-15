package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

enum class SeaIceConditionsAndDevelopmentEnum(val code : String, val stringResource : Int) : IDropDownMenu {
    @SerializedName("NOT_SELECTED")
    NOT_SELECTED("NOT_SELECTED", R.string.enum_not_selected),
    @SerializedName("SHIP_IN_OPEN_WATERS")
    SHIP_IN_OPEN_WATERS("SHIP_IN_OPEN_WATERS", R.string.sea_ice_conditions_and_development_0),
    @SerializedName("SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_IMPROVING")
    SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_IMPROVING("SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_IMPROVING", R.string.sea_ice_conditions_and_development_1),
    @SerializedName("SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_STABLE")
    SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_STABLE("SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_STABLE", R.string.sea_ice_conditions_and_development_2),
    @SerializedName("SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_WORSENING")
    SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_WORSENING("SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_WORSENING", R.string.sea_ice_conditions_and_development_3),
    @SerializedName("SHIP_IN_DIFFICULT_TO_PENETRATE_ICE_CONDITIONS_ARE_IMPROVING")
    SHIP_IN_DIFFICULT_TO_PENETRATE_ICE_CONDITIONS_ARE_IMPROVING("SHIP_IN_DIFFICULT_TO_PENETRATE_ICE_CONDITIONS_ARE_IMPROVING", R.string.sea_ice_conditions_and_development_4),
    @SerializedName("ICE_FORMING_AND_CONDENSES_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING")
    ICE_FORMING_AND_CONDENSES_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING("ICE_FORMING_AND_CONDENSES_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING", R.string.sea_ice_conditions_and_development_6),
    @SerializedName("ICE_UNDER_LIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING")
    ICE_UNDER_LIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING("ICE_UNDER_LIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING", R.string.sea_ice_conditions_and_development_7),
    @SerializedName("ICE_UNDER_TIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING")
    ICE_UNDER_TIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING("ICE_UNDER_TIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING", R.string.sea_ice_conditions_and_development_8),
    @SerializedName("SHIP_STUCK_IN_ICE")
    SHIP_STUCK_IN_ICE("SHIP_STUCK_IN_ICE", R.string.sea_ice_conditions_and_development_9),
    @SerializedName("NOT_POSSIBLE_TO_REPORT")
    NOT_POSSIBLE_TO_REPORT("NOT_POSSIBLE_TO_REPORT", R.string.sea_ice_conditions_and_development_x),
    ;

    override fun getLocalizedName(context: Context): String {
        return context.resources.getString(stringResource)
    }

    override fun getFormValue(): String {
        val retval: String = when (this) {
            SHIP_IN_OPEN_WATERS -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_0
            SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_IMPROVING -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_1
            SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_STABLE -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_2
            SHIP_IN_EASILY_PENETRATED_ICE_CONDITIONS_ARE_WORSENING -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_3
            SHIP_IN_DIFFICULT_TO_PENETRATE_ICE_CONDITIONS_ARE_IMPROVING -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_4
            ICE_FORMING_AND_CONDENSES_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_6
            ICE_UNDER_LIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_7
            ICE_UNDER_TIGHT_PRESSURE_DIFFICULT_CONDITIONS_THAT_ARE_WORSENING -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_8
            SHIP_STUCK_IN_ICE -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_9
            NOT_POSSIBLE_TO_REPORT -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_x
            NOT_SELECTED -> SEA_ICE_CONDITIONS_AND_DEVELOPMENT_NOT_SELECTED
        }

        return retval
    }

    companion object {
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_NOT_SELECTED = "Ikke valgt"
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_0 = "0-Skip i åpent vann "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_1 = "1-Skip i lett gjennomtrengbar is; Forholdene er under bedring "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_2 = "2-Skip i lett gjennomtrengbar is; Forholdene er stabile "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_3 = "3-Skip i lett gjennomtrengbar is; Forholdene blir verre  "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_4 = "4-Skip i vanskleig gjennomtrengbar is; Forholdene er under bedring "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_6 = "6-Is dannes og fortettes. Vanskelige forhold som forverres "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_7 = "7-Is under lett trykk. Vanskelige forhold som forverres "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_8 = "8-Is under tett trykk. Vanskelige forhold som forverres "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_9 = "9-Skip fast i is "
        const val SEA_ICE_CONDITIONS_AND_DEVELOPMENT_x = "/-Ikke mulig å raportere"
    }
}