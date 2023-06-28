
package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R

enum class IcingTypeCode (val code : String, val stringResource : Int) {
    @SerializedName("LIGHT_ICING")
    LIGHT_ICING("LIGHT_ICING", R.string.icing_type_code_light_icing),

    @SerializedName("MODERATE_ICING")
    MODERATE_ICING("MODERATE_ICING", R.string.icing_type_code_moderate_icing),

    @SerializedName("HEAVY_ICING")
    HEAVY_ICING("HEAVY_ICING", R.string.icing_type_code_heavy_icing);

    fun getLocalizedName(context : Context):String {
        return context.resources.getString(stringResource)
    }
}