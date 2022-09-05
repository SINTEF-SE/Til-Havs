package no.sintef.fiskinfo.model.sprice

import android.content.Context
import com.google.gson.annotations.SerializedName
import no.sintef.fiskinfo.R

enum class MaxMiddleWindTimeEnum(val code : String, val stringResource : Int) {
    @SerializedName("DURING_OBSERVATION")
    DURING_OBSERVATION("DURING_OBSERVATION", R.string.max_middle_wind_time_0),

    @SerializedName("AFTER_1_HOUR")
    AFTER_1_HOUR("AFTER_1_HOUR", R.string.max_middle_wind_time_1),

    @SerializedName("AFTER_2_HOUR")
    AFTER_2_HOUR("AFTER_2_HOUR", R.string.max_middle_wind_time_2),

    @SerializedName("AFTER_3_HOUR")
    AFTER_3_HOUR("AFTER_3_HOUR", R.string.max_middle_wind_time_3),

    @SerializedName("AFTER_4_HOUR")
    AFTER_4_HOUR("AFTER_4_HOUR", R.string.max_middle_wind_time_4),

    @SerializedName("AFTER_5_HOUR")
    AFTER_5_HOUR("AFTER_5_HOUR", R.string.max_middle_wind_time_5),

    @SerializedName("AFTER_6_HOUR")
    AFTER_6_HOUR("AFTER_6_HOUR", R.string.max_middle_wind_time_6),

    @SerializedName("AFTER_7_HOUR")
    AFTER_7_HOUR("AFTER_7_HOUR", R.string.max_middle_wind_time_7),

    @SerializedName("AFTER_8_HOUR")
    AFTER_8_HOUR("AFTER_8_HOUR", R.string.max_middle_wind_time_8),

    @SerializedName("AFTER_9_HOUR")
    AFTER_9_HOUR("AFTER_9_HOUR", R.string.max_middle_wind_time_9),

    @SerializedName("IMPOSSIBLE_TO_SAY")
    IMPOSSIBLE_TO_SAY("IMPOSSIBLE_TO_SAY", R.string.max_middle_wind_time_x);

    fun getLocalizedName(context : Context):String {
        return context.resources.getString(stringResource)
    }

    fun getFormValue(): String {
        var retval = ""

        when (this) {
            DURING_OBSERVATION -> retval = MAX_MIDDLE_WIND_TIME_0_FORM_VALUE
            AFTER_1_HOUR -> retval = MAX_MIDDLE_WIND_TIME_1_FORM_VALUE
            AFTER_2_HOUR -> retval = MAX_MIDDLE_WIND_TIME_2_FORM_VALUE
            AFTER_3_HOUR -> retval = MAX_MIDDLE_WIND_TIME_3_FORM_VALUE
            AFTER_4_HOUR -> retval = MAX_MIDDLE_WIND_TIME_4_FORM_VALUE
            AFTER_5_HOUR -> retval = MAX_MIDDLE_WIND_TIME_5_FORM_VALUE
            AFTER_6_HOUR -> retval = MAX_MIDDLE_WIND_TIME_6_FORM_VALUE
            AFTER_7_HOUR -> retval = MAX_MIDDLE_WIND_TIME_7_FORM_VALUE
            AFTER_8_HOUR -> retval = MAX_MIDDLE_WIND_TIME_8_FORM_VALUE
            AFTER_9_HOUR -> retval = MAX_MIDDLE_WIND_TIME_9_FORM_VALUE
            IMPOSSIBLE_TO_SAY -> retval = MAX_MIDDLE_WIND_TIME_X_FORM_VALUE
        }

        return retval
    }

    companion object {
        const val MAX_MIDDLE_WIND_TIME_0_FORM_VALUE = " i observasjonstida"
        const val MAX_MIDDLE_WIND_TIME_1_FORM_VALUE = " 0-1 time fra obs "
        const val MAX_MIDDLE_WIND_TIME_2_FORM_VALUE = " 1-2 time fra obs  "
        const val MAX_MIDDLE_WIND_TIME_3_FORM_VALUE = " 2-3 time fra obs "
        const val MAX_MIDDLE_WIND_TIME_4_FORM_VALUE = " 3-6 time fra obs "
        const val MAX_MIDDLE_WIND_TIME_5_FORM_VALUE = " 6-9 time fra obs "
        const val MAX_MIDDLE_WIND_TIME_6_FORM_VALUE = " 9-12 time fra obs "
        const val MAX_MIDDLE_WIND_TIME_7_FORM_VALUE = " Vinden synes å avta "
        const val MAX_MIDDLE_WIND_TIME_8_FORM_VALUE = " Vinden synes uendret "
        const val MAX_MIDDLE_WIND_TIME_9_FORM_VALUE = " Vinden synes å øke "
        const val MAX_MIDDLE_WIND_TIME_X_FORM_VALUE = " Umulig å angi"
    }
}