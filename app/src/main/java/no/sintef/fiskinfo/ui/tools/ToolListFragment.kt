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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.ToolListFragmentBinding
import no.sintef.fiskinfo.model.fishingfacility.ToolViewModel


/**
 * A fragment for showing the active tools.
 */
class ToolListFragment : Fragment(), ToolsRecyclerViewAdapter.OnToolInteractionListener {  //, SnapRecyclerViewAdapter.OnSnapInteractionListener {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    private val mViewModel: ToolsViewModel by activityViewModels()

    private var _binding: ToolListFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ToolListFragmentBinding.inflate(inflater, container, false)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())


        val listView = binding.toolList
        val fab = binding.fab
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

                    val depViewModel = ViewModelProvider(requireActivity()).get(
                        DeploymentViewModel::class.java
                    )
                    depViewModel.clear()

                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment_tools_to_deployment_editor_fragment)
                }
            }
        }

        val context = binding.root.context
        listView.layoutManager = LinearLayoutManager(context)
        mAdapter = ToolsRecyclerViewAdapter(this, mIsConfirmed)
        listView.adapter = mAdapter

        mSwipeLayout = binding.toollistswipelayout
        mSwipeLayout!!.setOnRefreshListener { mViewModel.refreshTools() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
        val tools = if (mIsConfirmed) mViewModel.getConfirmedTools() else mViewModel.getUnconfirmedTools()

        tools?.observe(viewLifecycleOwner
        ) { _tools ->
            mAdapter!!.setTools(_tools)
            if (mSwipeLayout != null)
                mSwipeLayout!!.isRefreshing = false
        }
    }

    private fun isUserProfileValid():Boolean {
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance(isConfirmedTools: Boolean) = ToolListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_CONFIRMED_TOOLS, isConfirmedTools)
            }
        }
    }

    override fun onViewToolClicked(v: View, tool: ToolViewModel?) {
        mViewModel.selectTool(tool)
        Navigation.findNavController(v).navigate(R.id.action_tools_fragment_to_tool_details_fragment)
    }

    override fun onRemoveToolClicked(v: View, tool: ToolViewModel?) {
        if (mIsConfirmed && (tool != null)){
            val toolTypeStr = if (tool.toolTypeCode != null) tool.toolTypeCode?.getLocalizedName(requireContext())!!.lowercase() else "tool"

            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.tool_report_retrieval_heading) + toolTypeStr)
                .setMessage(getString(R.string.tool_report_retrieval_message_confirm))
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                    mViewModel.sendRetrievedReport(tool)
                    dialog.dismiss()
                }
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
