
package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R

enum class WindTypeCode (val code : String, val stringResource : Int) {
    @SerializedName("LIGHT_WIND")
    LIGHT_ICING("LIGHT_WIND", R.string.wind_type_code_light_wind),

    @SerializedName("MODERATE_WIND")
    MODERATE_ICING("MODERATE_WIND", R.string.wind_type_code_moderate_wind),

    @SerializedName("HEAVY_WIND")
    HEAVY_ICING("HEAVY_WIND", R.string.wind_type_code_heavy_wind);

    fun getLocalizedName(context : Context):String {
        return context.resources.getString(stringResource)
    }
}