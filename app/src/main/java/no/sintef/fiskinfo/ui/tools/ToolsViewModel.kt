/**
 * Copyright (C) 2020 SINTEF
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.sintef.fiskinfo.ui.tools

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import no.sintef.fiskinfo.model.fishingfacility.*
import no.sintef.fiskinfo.repository.FishingFacilityRepository
import java.util.*

class ToolsViewModel(application: Application) : AndroidViewModel(application)  {

    private val selectedTool = MutableLiveData<ToolViewModel?>()
    private var confirmedTools: LiveData<List<ToolViewModel>>? = null
    private var unconfirmedTools: LiveData<List<ToolViewModel>>? = null

    private var profile: LiveData<FiskInfoProfileDTO>? = null

    private val draftReport = MutableLiveData<Report>()
    private val selectedToolTypeCodeName = MutableLiveData<String>()

    fun selectTool(tool: ToolViewModel?) {
        selectedTool.value = tool
        selectedToolTypeCodeName.value = tool?.toolTypeCode?.getLocalizedName(getApplication())
    }

    fun getSelectedTool(): LiveData<ToolViewModel?> {
        return selectedTool
    }


    fun getConfirmedTools(): LiveData<List<ToolViewModel>>? {
        if (confirmedTools == null) {
            confirmedTools = FishingFacilityRepository.getInstance(getApplication()).getConfirmedTools()
        }
        return confirmedTools
    }

    fun getUnconfirmedTools(): LiveData<List<ToolViewModel>>? {
        if (unconfirmedTools == null) {
            unconfirmedTools = FishingFacilityRepository.getInstance(getApplication()).getUnconfirmedTools()
        }
        return unconfirmedTools
    }

    fun refreshTools() {
        FishingFacilityRepository.getInstance(getApplication()).refreshFishingFacilityChanges();
    }

    fun getProfile(): LiveData<FiskInfoProfileDTO>? {
        if (profile == null) {
            profile = FishingFacilityRepository.getInstance(getApplication()).getFiskInfoProfileDTO()
        }
        return profile;
    }

    val draft: LiveData<Report>
        get() = draftReport

    fun createReportDraft() {
        val report = Report()

        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
        report.toolTypeCode = ToolTypeCode.valueOf(prefs.getString("default_tool_type", ToolTypeCode.NETS.code)!!)
        draftReport.value = report
    }

    fun setSelectedToolCode(code: ToolTypeCode) {
        if (selectedTool.value?.toolTypeCode != code) {
            selectedTool.value?.toolTypeCode != code
            selectedToolTypeCodeName.value = code.getLocalizedName(getApplication())
        }
    }


    val selectedToolCodeName: LiveData<String>
        get() = this.selectedToolTypeCodeName


    fun setSelectedToolDate(date : Date) {
        if (selectedTool.value != null) {
            // TODO: pick out only date part (not time)
            selectedTool.value!!.lastChangedDateTime = date
        }
    }


    fun sendRetrievedReport(tool: ToolViewModel):LiveData<FishingFacilityRepository.SendResult> {
        val retrievalDate = Date()
        val info = RetrievalInfoDto(toolId = tool.toolId!!, removedTime = retrievalDate)

        return FishingFacilityRepository.getInstance(getApplication()).sendRetrieved(info)
    }


/*
    fun canSendReport():Boolean {
        if (draftReport.value == null)
            return false

        // TODO: Check that all required fields are present
        return true
    }

    fun sendReport() {
        if (canSendReport()) {

        }
    }
*/

    /*
    fun getSelectedToolCodeName():String { this.selectedToolCodeName
        var retVal : String? = null
        selectedTool?.value?.let {
            retVal = it.toolTypeCode?.getLocalizedName(getApplication())
        }
        return if (retVal != null) retVal!! else ""
    }
*/

}
