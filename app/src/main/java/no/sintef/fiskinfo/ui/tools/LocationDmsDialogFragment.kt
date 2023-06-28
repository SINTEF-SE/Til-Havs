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

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.LocationDmsEditorFragmentBinding
import no.sintef.fiskinfo.util.DMSLocation.Companion.EDIT_DMS_POSITION_FRAGMENT_RESULT_REQUEST_KEY
import no.sintef.fiskinfo.util.GpsLocationTracker
import no.sintef.fiskinfo.utilities.ui.IntRangeValidator

class LocationDmsDialogFragment : DialogFragment() {

    private lateinit var viewModel: LocationDmsViewModel
    private var _mBinding: LocationDmsEditorFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _mBinding!!

    interface LocationDmsDialogListener {
        fun onDmsEditConfirmed()
    }

    fun createView(
        inflater: LayoutInflater, container: ViewGroup?
    ): View {
        _mBinding = LocationDmsEditorFragmentBinding.inflate(inflater, container, false)

        mBinding.latitudeDegreesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.latitudeDegreesEditText,
                0,
                true,
                90,
                true
            ) { viewModel.dmsLocation.value!!.latitudeDegrees = it.toInt() }
        )

        mBinding.latitudeMinutesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.latitudeMinutesEditText,
                0,
                true,
                59,
                true
            ) { viewModel.dmsLocation.value!!.latitudeMinutes = it.toInt() }
        )

        mBinding.latitudeSecondsEditText.addTextChangedListener(
            IntRangeValidator(mBinding.latitudeSecondsEditText,
                0,
                true,
                60,
                false
            ) { viewModel.dmsLocation.value!!.latitudeSeconds = it }
        )


        mBinding.longitudeDegreesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.longitudeDegreesEditText,
                0,
                true,
                180,
                true
            ) { viewModel.dmsLocation.value!!.longitudeDegrees = it.toInt() }
        )

        mBinding.longitudeMinutesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.longitudeMinutesEditText,
                0,
                true,
                59,
                true
            ) { viewModel.dmsLocation.value!!.longitudeMinutes = it.toInt() }
        )

        mBinding.longitudeSecondsEditText.addTextChangedListener(
            IntRangeValidator(mBinding.longitudeSecondsEditText,
                0,
                true,
                60,
                false
            ) { viewModel.dmsLocation.value!!.longitudeSeconds = it }
        )

        mBinding.setToCurrentPositionIcon.setOnClickListener { setLocationToCurrentPosition() }

        viewModel = ViewModelProvider(requireActivity())[LocationDmsViewModel::class.java]

        viewModel.dmsLocation.observe(this) { dmsLoc ->
            if (dmsLoc != null) {
                mBinding.viewModel = dmsLoc
            }
        }

        return mBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = createView(requireActivity().layoutInflater, null)

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.tool_edit_location))
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
            .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                val fm: FragmentManager = parentFragmentManager
                val args = Bundle()
                fm.setFragmentResult(EDIT_DMS_POSITION_FRAGMENT_RESULT_REQUEST_KEY, args)

                dismiss()
            }
        builder.setView(view)

        return builder.create()
    }

    fun setLocationToCurrentPosition() {
        viewModel.getLocation()

        val tracker = GpsLocationTracker(requireContext())
        if (tracker.canGetLocation()) {

            val loc = tracker.location
            if (loc != null) {
                viewModel.setNewLocation(loc)
            }

        } else {
            tracker.showSettingsAlert()
        }
    }

    companion object {
        fun newInstance(title: String?): LocationDmsDialogFragment {
            val frag = LocationDmsDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }
}