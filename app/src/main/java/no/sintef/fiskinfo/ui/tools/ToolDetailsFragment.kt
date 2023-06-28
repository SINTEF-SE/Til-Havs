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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.ToolDetailsFragmentBinding
import no.sintef.fiskinfo.model.fishingfacility.ResponseStatus
import no.sintef.fiskinfo.model.fishingfacility.ToolTypeCode
import no.sintef.fiskinfo.util.formatDate


class ToolDetailsFragment : Fragment() {
    // PRI: Get basic working editor
    // MAYBE: use material design to improve l&f

    companion object {
        fun newInstance() = ToolDetailsFragment()
    }

    private lateinit var mViewModel: ToolsViewModel
    private var _mBinding : ToolDetailsFragmentBinding? = null
    private val binding get() = _mBinding!!

    private lateinit var mToolCodeAdapter : ToolTypeCodeArrayAdapter
    private lateinit var mLocationAdapter : CompactLocationRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = ToolDetailsFragmentBinding.inflate(inflater, container, false)

        mToolCodeAdapter = ToolTypeCodeArrayAdapter(requireContext(), R.layout.exposed_dropdown_menu_item, ToolTypeCode.values())

        binding.toolPositionRecyclerView.layoutManager = LinearLayoutManager(context)

//        mViewModel = ViewModelProviders.of(requireActivity()).get(ToolsViewModel::class.java)
//        val locations = mViewModel?.getSelectedTool().value?.getLocations()
//        if (locations != null)
//            locAdapter.locations = locations

        mLocationAdapter = CompactLocationRecyclerViewAdapter()
//        mViewModel = ViewModelProviders.of(requireActivity()).get(ToolsViewModel::class.java)
//        val locations = mViewModel?.getSelectedTool().value?.getLocations()
//        if (locations != null)
//            locAdapter.locations = locations

        binding.toolPositionRecyclerView.adapter = mLocationAdapter

        mViewModel = ViewModelProvider(requireActivity()).get(ToolsViewModel::class.java)

        mViewModel.getSelectedTool().observe(viewLifecycleOwner) { tool ->
            if (tool != null) {

                binding.tool = tool
                binding.toolviewmodel = mViewModel
                mLocationAdapter.locations = tool.getLocations()

                // Testing tool.responseReason = "This is a test on a response reason string that is quite long, just to test"
                // Testing tool.responseDateTime = Date()


                var responseStr = when (tool.responseStatus) {
                    ResponseStatus.RESPONSE_APPROVED -> getString(R.string.tool_response_approved)
                    ResponseStatus.RESPONSE_REJECTED -> getString(R.string.tool_response_rejected)
                    ResponseStatus.NO_RESPONSE -> getString(R.string.tool_response_no_response)
                    ResponseStatus.RESPONSE_UNKNOWN -> getString(R.string.tool_response_unknown)
                    else -> getString(R.string.tool_response_unknown)
                }

                if (tool.responseDateTime != null) {
                    responseStr = formatDate(tool.responseDateTime) + ": " + responseStr
                }

                if (tool.responseReason != null) {
                    responseStr += "\n" + tool.responseReason
                }

                if (tool.errorReportedFromApi == true) {
                    responseStr += "\n" + getString(R.string.tool_report_api_error)
                }

                binding.toolResponseField.setText(responseStr)
            }
        }

        mViewModel.selectedToolCodeName.observe(viewLifecycleOwner) { toolCodeName ->
            if (toolCodeName != null) {
                binding.toolcodename = toolCodeName
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

}
