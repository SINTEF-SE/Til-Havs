package no.sintef.fiskinfo.ui.sprice

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.model.sprice.IcingReport
import no.sintef.fiskinfo.model.sprice.IcingTypeCode
import no.sintef.fiskinfo.util.locationsToGeoJsonGeometry
import no.sintef.fiskinfo.utilities.ui.ObservableAndroidViewModel
import java.util.*

@Suppress("unused")
class GetReportsViewModel(application: Application) : ObservableAndroidViewModel(application) {
    val reportingTime = MutableLiveData<Date>()
    val locations = MutableLiveData<MutableList<Location>>()
    val icingTypeCode = MutableLiveData<IcingTypeCode>()
    private var reportChecked = MutableLiveData<Boolean>()
    var reportValid = MutableLiveData<Boolean>()

    var orapUsername: String? = null
    private var orapPassword: String? = null
    var initialized = false

    private fun createIcingReport(): IcingReport {
        getOrapInfoFromPreferences()

        return IcingReport(
            geometry = locationsToGeoJsonGeometry(locations.value!!),
            reportingTime = reportingTime.value!!

        )
    }

    fun initContent() {
        if (!initialized) {
            val context: Context = getApplication()
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)


            reportingTime.value = Date()
            val defaultLoc = Location("")
            // TODO: Default locations
            defaultLoc.latitude = 0.0  //your coords of course
            defaultLoc.longitude = 0.0
            locations.value = mutableListOf(defaultLoc)
            reportChecked.value = false
            reportValid.value = false
        }
    }

    private fun getOrapInfoFromPreferences() {
        val context: Context = getApplication()
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)

        orapUsername = prefs.getString(context.getString(R.string.pref_sprice_username_key), "")
        orapPassword = prefs.getString(context.getString(R.string.pref_sprice_password_key), "")
    }

    fun setReportingDate(date: Date) {
        // TODO: pick out only date part (not time)
        val c = Calendar.getInstance()
        c.time = reportingTime.value!!

        val newDateC = Calendar.getInstance()
        newDateC.time = date
        c.set(Calendar.YEAR, newDateC.get(Calendar.YEAR))
        c.set(Calendar.DAY_OF_YEAR, newDateC.get(Calendar.DAY_OF_YEAR))

        reportingTime.value = c.time
    }

    fun clear() {
        initialized = false
    }
}