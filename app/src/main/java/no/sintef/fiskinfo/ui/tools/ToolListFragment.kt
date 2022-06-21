/**
 * Copyright (C) 2020 SINTEF
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
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.model.fishingfacility.FishingFacility
import no.sintef.fiskinfo.model.fishingfacility.ToolViewModel


/**
 * A fragment for showing the active tools.
 */
class ToolListFragment : Fragment(), ToolsRecyclerViewAdapter.OnToolInteractionListener {  //, SnapRecyclerViewAdapter.OnSnapInteractionListener {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    private val mViewModel: ToolsViewModel by activityViewModels()

    private var mAdapter: ToolsRecyclerViewAdapter? = null
    private var mSwipeLayout: SwipeRefreshLayout? = null
    private var mIsConfirmed : Boolean = false
    private val IS_CONFIRMED_TOOLS = "IsConfirmedTools"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIsConfirmed = arguments?.getBoolean(IS_CONFIRMED_TOOLS, false) ?: false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val tools = if (mIsConfirmed) mViewModel!!.getConfirmedTools() else mViewModel!!.getUnconfirmedTools()

        tools?.observe(viewLifecycleOwner,
            Observer<List<ToolViewModel>> { _tools ->
                mAdapter!!.setTools(_tools)
                if (mSwipeLayout != null)
                    mSwipeLayout!!.isRefreshing = false
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());

        val view = inflater.inflate(R.layout.tool_list_fragment, container, false)

        val listView = view.findViewById<RecyclerView>(R.id.tool_list)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        if (mIsConfirmed)
            fab.visibility = View.GONE // No fab for confirmed tools

        else {
            fab.setOnClickListener { view ->

                if (!isUserProfileValid()) {
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.CONTENT_TYPE, "Create new tool, invalid profile")
                        param(FirebaseAnalytics.Param.SCREEN_NAME, "Tool List")
                        param(FirebaseAnalytics.Param.SCREEN_CLASS, "ToolListFragment")
                    }

                    Snackbar.make(
                        view,
                        getString(R.string.tool_list_profile_warning),
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Action", null)
                        .show()
                } else {
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.CONTENT_TYPE, "Create new tool")
                        param(FirebaseAnalytics.Param.SCREEN_NAME, "Tool List")
                        param(FirebaseAnalytics.Param.SCREEN_CLASS, "ToolListFragment")
                    }

                    var depViewModel = ViewModelProviders.of(requireActivity()).get(
                        DeploymentViewModel::class.java
                    )
                    depViewModel.clear()

                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment_tools_to_deployment_editor_fragment)
                }
            }
        }

        val context = view.context
        listView.layoutManager = LinearLayoutManager(context)
        mAdapter = ToolsRecyclerViewAdapter(this, mIsConfirmed)
        listView.adapter = mAdapter

        mSwipeLayout = view.findViewById(R.id.toollistswipelayout) as SwipeRefreshLayout
        //swipeLayout.setProgressBackgroundColorSchemeResource(R.color.colorBrn);
        mSwipeLayout!!.setOnRefreshListener { mViewModel!!.refreshTools() }

        return view
    }

    private fun isUserProfileValid():Boolean {
        return true;
    }


/*
    override fun onViewSnapClicked(v: View, snap: SnapMessage?) {
        mViewModel!!.selectSnap(snap, mIsInbox)
        Navigation.findNavController(v).navigate(R.id.action_fragment_snap_to_snapDetailFragment)
    }

    override fun onViewSnapInMapClicked(v: View, snap: SnapMessage?) {
        val toast = Toast.makeText(this.context, "Not yet implemented!", Toast.LENGTH_SHORT)
        toast.show()
    }

*/
    companion object {
        @JvmStatic
        fun newInstance(isConfirmedTools: Boolean) = ToolListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_CONFIRMED_TOOLS, isConfirmedTools)
            }
        }
    }

    override fun onViewToolClicked(v: View, tool: ToolViewModel?) {
        mViewModel!!.selectTool(tool, mIsConfirmed)
        Navigation.findNavController(v).navigate(R.id.action_tools_fragment_to_tool_details_fragment)
    }

    override fun onRemoveToolClicked(v: View, tool: ToolViewModel?) {
        if (mIsConfirmed && (tool != null)){
            var toolTypeStr = if (tool.toolTypeCode != null) tool.toolTypeCode?.getLocalizedName(requireContext())!!.toLowerCase() else "tool"

            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.tool_report_retrieval_heading) + toolTypeStr)
                .setMessage(getString(R.string.tool_report_retrieval_message_confirm))
                //            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                //                dismiss();
                //            }
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    val sendRetrievedReport =
                        mViewModel.sendRetrievedReport(tool)
                    //sendRetrievedReport.observe(viewLifecycleOwner, Observer {
                    //    sendRetrievedReport.removeObservers(viewLifecycleOwner)
                    //})
                    dialog.dismiss()
                }
            //builder.setView(view)
            builder.create().show()
        }
    }

    override fun onToolStatusClicked(v: View, tool: ToolViewModel?) {
        // TODO: Show info about status of tool
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Tool List")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ToolListFragment")
        }
    }

}
