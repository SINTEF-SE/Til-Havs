package no.sintef.fiskinfo.ui.sprice

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.AutoCompleteTextView
import android.widget.GridView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajhuntsman.ksftp.FilePair
import com.ajhuntsman.ksftp.SftpClient
import com.ajhuntsman.ksftp.SftpConnectionParametersBuilder
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import no.sintef.fiskinfo.BuildConfig
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.SpriceReportIcingFragmentBinding
import no.sintef.fiskinfo.model.sprice.DegreeOfIcingEnum
import no.sintef.fiskinfo.model.sprice.IcingReportHourEnum
import no.sintef.fiskinfo.model.sprice.ReasonForIcingOnVesselOrPlatformEnum
import no.sintef.fiskinfo.model.sprice.SeaIceConditionsAndDevelopmentEnum
import no.sintef.fiskinfo.repository.OrapRepository
import no.sintef.fiskinfo.ui.layout.TextInputLayoutGridViewAdapter
import no.sintef.fiskinfo.ui.layout.TextInputLayoutGridViewModel
import no.sintef.fiskinfo.ui.tools.LocationDmsDialogFragment
import no.sintef.fiskinfo.ui.tools.LocationDmsDialogFragment.LocationDmsDialogListener
import no.sintef.fiskinfo.ui.tools.LocationDmsViewModel
import no.sintef.fiskinfo.ui.tools.LocationRecyclerViewAdapter
import no.sintef.fiskinfo.ui.tools.LocationViewModel
import no.sintef.fiskinfo.util.DMSLocation
import java.io.File
import java.io.InputStream
import java.util.*


class ReportIcingFragment : LocationRecyclerViewAdapter.OnLocationInteractionListener, FragmentResultListener,
    Fragment(),
    LocationDmsDialogListener {

    companion object {
        fun newInstance() = ReportIcingFragment()
    }

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    private lateinit var mViewModel: ReportIcingViewModel
    private lateinit var mLocationViewModel: LocationViewModel

    private lateinit var seaIcingGridView: GridView
    private lateinit var vesselIcingGridView: GridView
    private lateinit var vesselIcingGridViewSecondRow: GridView
    private lateinit var imagesGridView: GridView

    private lateinit var vesselIcingRecyclerView: RecyclerView
    private lateinit var windObservationsGridView: GridView

    private lateinit var mReportingHourAdapter: DropDownMenuArrayAdapter<IcingReportHourEnum>
    private lateinit var mSynopHourDropdown: AutoCompleteTextView

    private lateinit var locAdapter: LocationRecyclerViewAdapter
    private lateinit var selectImageIntent:  androidx.activity.result.ActivityResultLauncher<String>

    private var _mBinding: SpriceReportIcingFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val mBinding get() = _mBinding!!
    private lateinit var mVesselIcingRecyclerViewGridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        _mBinding = SpriceReportIcingFragmentBinding.inflate(inflater, container, false)

        initInputAndGridViews()

        return mBinding.root
    }

    private fun initInputAndGridViews() {
        initTimeInputs()
        initSeaIcingGridView()
        initVesselIcingGridView()
//        initWindObservationsGridView()
        initPositionInput()
        initImageSelector()
    }

    private fun initPositionInput() {
        locAdapter = LocationRecyclerViewAdapter(this)
        mBinding.icingObservationPositionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.icingObservationPositionRecyclerView.adapter = locAdapter
    }

    private fun initTimeInputs() {
        initReportingHourDropDown()

        mBinding.icingReportDateField.setOnClickListener {
            val builder: MaterialDatePicker.Builder<Long> = MaterialDatePicker.Builder.datePicker()
            builder.setSelection(mViewModel.synopDate.value!!.time)
            Log.e("setSelection", "Reporting time: ${mViewModel.synopDate.value!!.time}")

            val picker: MaterialDatePicker<*> = builder.build()
            picker.addOnPositiveButtonClickListener {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it as Long
                mViewModel.setObservationDate(cal.time)
            }
            picker.show(parentFragmentManager, picker.toString())
        }
    }

    private fun initVesselIcingGridView() {
        val vesselIcingInputsArrayList: ArrayList<TextInputLayoutGridViewModel<IDropDownMenu>> = ArrayList<TextInputLayoutGridViewModel<IDropDownMenu>>()
        val vesselIcingInputsArrayListSecond: ArrayList<TextInputLayoutGridViewModel<IDropDownMenu>> = ArrayList<TextInputLayoutGridViewModel<IDropDownMenu>>()
        vesselIcingGridView = mBinding.reportIcingVesselIcingGridView
        vesselIcingGridViewSecondRow = mBinding.reportIcingVesselIcingGridViewSecondRow
//        vesselIcingRecyclerView = mBinding.reportIcingVesselIcingRecyclerView

        vesselIcingInputsArrayList.add(
            TextInputLayoutGridViewModel(
                fieldName = getString(R.string.icing_report_vessel_degree_of_icing),
                hint = getString(R.string.icing_report_vessel_degree_of_icing),
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START,
                onClickListener = { parent, _, position, _ ->
                    mViewModel.currentVesselIcingIcingDegree.value = parent.getItemAtPosition(position) as DegreeOfIcingEnum
                },
                dropDownAdapter = DropDownMenuArrayAdapter(
                    requireContext(),
                    R.layout.exposed_dropdown_menu_item,
                    DegreeOfIcingEnum.values().drop(1).toTypedArray()
                )
            )
        )
        vesselIcingInputsArrayList.add(
            TextInputLayoutGridViewModel(
                fieldName = getString(R.string.icing_report_vessel_reason_for_icing_hint),
                hint = getString(R.string.icing_report_vessel_reason_for_icing_hint),
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START,
                onClickListener = { parent, _, position, _ ->
                    mViewModel.reasonForVesselIcing.value = parent.getItemAtPosition(position) as ReasonForIcingOnVesselOrPlatformEnum
                },
                dropDownAdapter = DropDownMenuArrayAdapter(
                    requireContext(),
                    R.layout.exposed_dropdown_menu_item,
                    ReasonForIcingOnVesselOrPlatformEnum.values().drop(1).toTypedArray()
                )
            )
        )
        vesselIcingInputsArrayListSecond.add(
            TextInputLayoutGridViewModel(
                fieldName = getString(R.string.icing_report_vessel_icing_thickness_hint),
                hint = getString(R.string.icing_report_vessel_icing_thickness_hint),
                suffixText = getString(R.string.icing_report_vessel_icing_thickness_suffix),
                textChangedListener = object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        mViewModel.vesselIcingThickness.value = s.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })
        )

//        vesselIcingInputsArrayList.forEach { vesselIcingGridLayout.addView(TextInputLayoutGridViewAdapter.getViewFromModel(requireContext(), it, vesselIcingGridView)) }

        vesselIcingGridView.adapter = TextInputLayoutGridViewAdapter(requireContext(), vesselIcingInputsArrayList)
        vesselIcingGridViewSecondRow.adapter = TextInputLayoutGridViewAdapter(requireContext(), vesselIcingInputsArrayListSecond)
    }

    private fun initSeaIcingGridView() {
        seaIcingGridView = mBinding.reportIcingSeaIcingGridView
        val seaIcingInputsArrayList: ArrayList<TextInputLayoutGridViewModel<SeaIceConditionsAndDevelopmentEnum>> = ArrayList()

        seaIcingInputsArrayList.add(
            TextInputLayoutGridViewModel(
                fieldName = getString(R.string.icing_report_sea_ice_conditions_and_development_hint),
                hint = getString(R.string.icing_report_sea_ice_conditions_and_development_hint),
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START,
                onClickListener = { parent, _, position, _ ->
                    mViewModel.seaIcingConditionsAndDevelopment.value = parent.getItemAtPosition(position) as SeaIceConditionsAndDevelopmentEnum
                },
                maxLines = 2,
                dropDownAdapter = DropDownMenuArrayAdapter(
                    requireContext(),
                    R.layout.exposed_dropdown_menu_item,
                    SeaIceConditionsAndDevelopmentEnum.values().drop(1).toTypedArray()
                )
            )
        )
        seaIcingGridView.adapter = TextInputLayoutGridViewAdapter(requireContext(), seaIcingInputsArrayList)
    }

    private fun initReportingHourDropDown() {
        mReportingHourAdapter = DropDownMenuArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_item,
            IcingReportHourEnum.values().drop(1).toTypedArray()
        )
        mSynopHourDropdown = mBinding.icingReportTimeField

        mSynopHourDropdown.setOnItemClickListener { parent, _, time, _ ->
            mViewModel.synopHourSelect.value = (parent.getItemAtPosition(time) as IcingReportHourEnum).code
            val calendar = Calendar.getInstance()
            calendar.time = mViewModel.synopDate.value!!
            calendar.set(Calendar.HOUR_OF_DAY, mViewModel.getSynopHourAsInt())

            mViewModel.synopDate.value = calendar.time
        }

        mSynopHourDropdown.setAdapter(mReportingHourAdapter)
    }

    private fun initImageSelector() {
        imagesGridView = mBinding.imageGridView

        selectImageIntent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            val arrayListImage = ArrayList<Int>()
            val imageNamesList = ArrayList<String>()
            val filesList = ArrayList<File>()

            for(imageUri in uriList) {
                arrayListImage.add(R.drawable.ic_image)
                imageNamesList.add(imageUri.lastPathSegment!!)

                val mime: MimeTypeMap = MimeTypeMap.getSingleton()
                val extension = mime.getExtensionFromMimeType(requireContext().contentResolver.getType(imageUri));
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(imageUri)
                val tempFile = File.createTempFile(imageUri.lastPathSegment!!, ".${extension}")

                inputStream.use { input ->
                    tempFile.outputStream().use { output ->
                        input!!.copyTo(output)
                    }
                }

                filesList.add(tempFile)
                inputStream!!.close()
            }

            mViewModel.attachedImages.value = filesList
            imagesGridView.adapter = GridViewImageAdapter(requireContext(), ArrayList(uriList), imageNamesList)
        }

        mBinding.icingObservationAddImagesField.setOnClickListener {
            selectImagesToUploadIntentCallback()
        }
    }

    private fun selectImagesToUploadIntentCallback() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestReadStoragePermission()
        } else {
            selectImageIntent.launch("image/*")
        }
    }

    private fun uploadImagesOverSftp(files: List<File>, webKitFormBoundaryId: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val orapUsername = BuildConfig.SPRICE_ORAP_SFTP_USER_NAME
        val orapPassword = BuildConfig.SPRICE_ORAP_SFTP_PASSWORD
        val filePairs = ArrayList<FilePair>()

        val connectionParameters = SftpConnectionParametersBuilder.newInstance().createConnectionParameters()
            .withHost(BuildConfig.SPRICE_ORAP_SFTP_URL)
            .withPort(BuildConfig.SPRICE_ORAP_SFTP_PORT_NUMBER)
            .withUsername(orapUsername)
            .withPassword(orapPassword.toByteArray())
            .create()

        for(file in files) {
            if (file.exists()) {
                val filePath: String = file.absolutePath
                var fileName = file.name
                val filenameLength = fileName.lastIndexOf('.', fileName.length)
                fileName = "${fileName.substring(0, filenameLength)}_${webKitFormBoundaryId}${fileName.substring(fileName.lastIndexOf('.', fileName.length))}".replace("[/<>:\"|?*]".toRegex(), "")

                val remoteFileName = "/dev/${fileName}"

                filePairs.add(FilePair(filePath, remoteFileName))
            } else {
                Log.d("SPRICE SFTP", "Path does not exist: ${file.path}")
            }
        }

        SftpClient
            .create(connectionParameters)
            .upload(filePairs, 60)
    }

    /**
     * function to prompt user to enable storage access
     */
    private fun requestReadStoragePermission() {
        val mAlertDialog = AlertDialog.Builder(
            ContextThemeWrapper(
                requireContext(),
                R.style.AppTheme
            )
        )
        mAlertDialog.setTitle(getString(R.string.util_storage_settings_disabled))
        mAlertDialog.setMessage(getString(R.string.util_storage_enable_question))
        mAlertDialog.setPositiveButton(
            getString(R.string.yes)
        ) { _, _ ->
            ActivityCompat.requestPermissions(requireActivity() as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        mAlertDialog.setNegativeButton(
            getString(R.string.no)
        ) { dialog, _ -> dialog.cancel() }

        val dialog = mAlertDialog.create()
        dialog.show()
    }

    override fun onDmsEditConfirmed() {
        val location = mLocationViewModel.getLocation()
        if (location != null) {
            mViewModel.location.value = location
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()

        mViewModel = ViewModelProvider(requireActivity())[ReportIcingViewModel::class.java]
        mLocationViewModel = ViewModelProvider(requireActivity())[LocationDmsViewModel::class.java]
        mViewModel.init()

        mViewModel.synopDate.observe(viewLifecycleOwner) {
            mBinding.viewmodel = mViewModel
        }

        mViewModel.location.observe(viewLifecycleOwner) {
            locAdapter.locations = listOf<Location>(it)
        }
    }

    private fun initMenu() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.sprice_report_icing_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Log.d("Request", mViewModel.getIcingReportBody().getRequestBodyForSpriceEndpointReportSubmissionAsString())
                Log.d(
                    "onOptionsItemSelected",
                    "\nSynop date: ${mViewModel.synopDate.value}, synop time: ${mViewModel.synopHourSelect.value}, reporting time: ${mViewModel.reportingTime.value}, synop unix: ${mViewModel.synopDate.value!!.time}\n" +
                            "air temp: ${mViewModel.airTemperature.value}, sea temp: ${mViewModel.seaTemperature.value}, icing thickness: ${mViewModel.vesselIcingThickness.value},\n" +
                            "${mViewModel.maxMiddleWindTime.value.getFormValue()},\n" +
                            "location: (lat: ${mViewModel.location.value?.latitude}, lon: ${mViewModel.location.value?.longitude})"
                )

                if (menuItem.itemId == R.id.send_icing_report_action) {
                    if (!reportedIcingValuesAreValid()) {
                        return false
                    }

                    val requestBody = mViewModel.getIcingReportBody()
                    val repository = OrapRepository.getInstance(requireContext(), requestBody.Username, requestBody.Password, requestBody.WebKitFormBoundaryId)
                    val result = repository.sendIcingReport(requestBody)

                    result.observe(viewLifecycleOwner) {
                        Log.e("ORAP", "Icing reported")
                        if (it.success) {
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                                param(FirebaseAnalytics.Param.CONTENT_TYPE, "Send icing report, success")
                                param(FirebaseAnalytics.Param.SCREEN_NAME, "Icing report")
                                param(FirebaseAnalytics.Param.SCREEN_CLASS, "ReportIcingFragment")
                            }

                            val text = getString(R.string.icing_report_sent)
                            val toast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
                            toast.show()
                            Navigation.findNavController(requireView()).navigateUp()
                        } else {
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                                param(FirebaseAnalytics.Param.CONTENT_TYPE, "Send icing report, error")
                                param(FirebaseAnalytics.Param.SCREEN_NAME, "Icing report")
                                param(FirebaseAnalytics.Param.SCREEN_CLASS, "ReportIcingFragment")
                            }

                            Snackbar.make(
                                requireView(),
                                getString(R.string.icing_report_submit_error) + it.errorMsg,
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }
                    }

                    uploadImagesOverSftp(mViewModel.attachedImages.value, requestBody.WebKitFormBoundaryId)

                    return true
                }

                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun reportedIcingValuesAreValid(): Boolean {
        var valid = true

//        if (mViewModel.synopHourSelect.value == "") {
//            (mSynopHourDropdown.parent as TextInputLayout).error = getString(R.string.drop_down_menu_error_not_selected)
//            valid = false
//        } else {
//            (mSynopHourDropdown.parent as TextInputLayout).error = null
//        }


        if (mViewModel.seaIcingConditionsAndDevelopment.value == SeaIceConditionsAndDevelopmentEnum.NOT_SELECTED) {
//            ((seaIcingGridView.getChildAt(0) as ViewGroup).getChildAt(0)
//                    as TextInputLayout).error = getString(R.string.drop_down_menu_error_not_selected)
            Toast.makeText(
                requireContext(),
                String.format(
                    getString(R.string.drop_down_menu_error_not_selected_for_template_string),
                    getString(R.string.icing_report_sea_ice_conditions_and_development_hint)
                ),
                Toast.LENGTH_LONG
            ).show()
            valid = false
            Log.e("checkReportedValues", "seaIcingConditionsAndDevelopment is invalid: ${mViewModel.seaIcingConditionsAndDevelopment.value}")
        } else {
//            ((seaIcingGridView.getChildAt(0) as ViewGroup).getChildAt(0)
//                    as TextInputLayout).error = null
        }

        if (mViewModel.vesselIcingThickness.value.isEmpty()) {
            ((((vesselIcingGridView.getChildAt(0) as ViewGroup).getChildAt(0) as ViewGroup)
                .getChildAt(0)) as TextInputEditText).error = getString(R.string.icing_missing_value)
            valid = false
            Log.e("checkReportedValues", "vesselIcingThickness is invalid: '${mViewModel.vesselIcingThickness.value}'")
        } else {
            ((((vesselIcingGridView.getChildAt(0) as ViewGroup).getChildAt(0) as ViewGroup)
                .getChildAt(0)) as MaterialAutoCompleteTextView).error = null
        }

        if (mViewModel.reasonForVesselIcing.value == ReasonForIcingOnVesselOrPlatformEnum.NOT_SELECTED) {
//            ((vesselIcingGridView.getChildAt(1) as ViewGroup).getChildAt(0)
//                    as TextInputLayout).error = getString(R.string.drop_down_menu_error_not_selected)
            Toast.makeText(
                requireContext(),
                String.format(
                    getString(R.string.drop_down_menu_error_not_selected_for_template_string),
                    getString(R.string.icing_report_vessel_reason_for_icing_hint)
                ),
                Toast.LENGTH_LONG
            ).show()
            valid = false
            Log.e("checkReportedValues", "reasonForVesselIcing is invalid: ${mViewModel.reasonForVesselIcing.value}")
        } else {
//            ((vesselIcingGridView.getChildAt(1) as ViewGroup).getChildAt(0)
//                    as TextInputLayout).error = null
        }

//        if(mViewModel.vesselIcingChangeInIcing.value == ChangeInIcingOnVesselOrPlatformEnum.NOT_SELECTED) {
////            ((vesselIcingGridViewSecondRow.findViewWithTag<TextInputLayout>(getString(R.string.icing_report_vessel_change_in_icing)) as ViewGroup).getChildAt(0)
////                    as TextInputLayout).error = getString(R.string.drop_down_menu_error_not_selected)
//            Toast.makeText(requireContext(), String.format(getString(R.string.drop_down_menu_error_not_selected_for_template_string), getString(R.string.icing_report_vessel_change_in_icing)), Toast.LENGTH_LONG).show()
//            valid = false
//            Log.e("checkReportedValues", "vesselIcingChangeInIcing is invalid: ${mViewModel.vesselIcingChangeInIcing.value}")
//        } else {
////            ((vesselIcingGridViewSecondRow.findViewWithTag<TextInputLayout>(getString(R.string.icing_report_vessel_change_in_icing)) as ViewGroup).getChildAt(0)
////                    as TextInputLayout).error = null
//        }

        if (mViewModel.currentVesselIcingIcingDegree.value == DegreeOfIcingEnum.NOT_SELECTED) {
            Toast.makeText(
                requireContext(),
                String.format(
                    getString(R.string.drop_down_menu_error_not_selected_for_template_string),
                    getString(R.string.icing_report_vessel_change_in_icing)
                ),
                Toast.LENGTH_LONG
            ).show()
            valid = false
            Log.e("checkReportedValues", "currentVesselIcingIcingDegree is invalid: ${mViewModel.currentVesselIcingIcingDegree.value}")
        } else {
//            ((vesselIcingGridViewSecondRow.findViewWithTag<TextInputLayout>(getString(R.string.icing_report_vessel_change_in_icing)) as ViewGroup).getChildAt(0)
//                    as TextInputLayout).error = null
        }

        if (mViewModel.location.value!!.latitude == 0.0 || mViewModel.location.value!!.longitude == 0.0) {
            Toast.makeText(
                requireContext(),
                String.format(getString(R.string.drop_down_menu_error_not_selected_for_template_string), getString(R.string.position)),
                Toast.LENGTH_LONG
            ).show()
        }

        // TODO: Check other values

        Log.e("checkReportedValues", "Values are ${if (!valid) "not" else ""} valid!")
        return valid
    }

    override fun onEditLocationClicked(v: View, itemClicked: Int) {
        mLocationViewModel.initWithLocation(mViewModel.location.value!!, itemClicked)
        val fm: FragmentManager = parentFragmentManager
        val locDialogFragment: LocationDmsDialogFragment =
            LocationDmsDialogFragment.newInstance(getString(R.string.tool_edit_location))

        fm.setFragmentResultListener(DMSLocation.EDIT_DMS_POSITION_FRAGMENT_RESULT_REQUEST_KEY, viewLifecycleOwner, this)
        locDialogFragment.show(fm, "fragment_edit_location")
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        if (DMSLocation.EDIT_DMS_POSITION_FRAGMENT_RESULT_REQUEST_KEY == requestKey) {
            val location = mLocationViewModel.getLocation()
            if (location != null) {
                mViewModel.location.value = location
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}