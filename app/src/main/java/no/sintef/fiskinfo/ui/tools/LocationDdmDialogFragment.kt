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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.LocationDdmEditorFragmentBinding
import no.sintef.fiskinfo.util.GpsLocationTracker
import no.sintef.fiskinfo.utilities.ui.IntRangeValidator


class LocationDdmDialogFragment : DialogFragment() {

    private lateinit var viewModel: LocationDdmViewModel
    private var _mBinding: LocationDdmEditorFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _mBinding!!

    interface LocationDdmDialogListener {
        fun onDdmEditConfirmed()
    }

    fun createView(
        inflater: LayoutInflater, container: ViewGroup?
    ): View {
        _mBinding = LocationDdmEditorFragmentBinding.inflate(inflater, container, false)

        val root = mBinding.root

        mBinding.latitudeDegreesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.latitudeDegreesEditText,
                0,
                true,
                90,
                true
            ) { viewModel.ddmLocation.value!!.latitudeDegrees = it.toInt() }
        )

        mBinding.latitudeMinutesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.latitudeMinutesEditText,
                0,
                true,
                59,
                true
            ) { viewModel.ddmLocation.value!!.latitudeDecimalMinutes = it }
        )

        mBinding.longitudeDegreesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.longitudeDegreesEditText,
                0,
                true,
                180,
                true
            ) { viewModel.ddmLocation.value!!.longitudeDegrees = it.toInt() }
        )

        mBinding.longitudeMinutesEditText.addTextChangedListener(
            IntRangeValidator(mBinding.longitudeMinutesEditText,
                0,
                true,
                59,
                true
            ) { viewModel.ddmLocation.value!!.longitudeDecimalMinutes = it }
        )

        mBinding.setToCurrentPositionIcon.setOnClickListener { setLocationToCurrentPosition() }
        return root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = createView(requireActivity().layoutInflater, null)

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.tool_edit_location))
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
            .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                val listener: LocationDdmDialogListener? = targetFragment as LocationDdmDialogListener?
                listener?.onDdmEditConfirmed()
                dismiss()

            }
        builder.setView(view)

        return builder.create()
    }

    fun setLocationToCurrentPosition() {
        viewModel.getLocation();

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


    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[LocationDdmViewModel::class.java]

        viewModel.ddmLocation.observe(this) { dmsLoc ->
            if (dmsLoc != null) {
                mBinding.viewmodel = dmsLoc
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        fun newInstance(title: String?): LocationDdmDialogFragment {
            val frag = LocationDdmDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }

}