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
package no.sintef.fiskinfo.ui.snap

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.EchogramListFragmentBinding
import no.sintef.fiskinfo.model.SnapMetadata

/**
 * A fragment showing a list of Echograms.
 *
 *
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EchogramListFragment : Fragment(), EchogramRecyclerViewAdapter.OnEchogramInteractionListener {

    private var mSnapViewModel: SnapViewModel? = null
    private var mEchogramViewModel: EchogramViewModel? = null
    private var mAdapter: EchogramRecyclerViewAdapter? = null
    private var mSwipeLayout: SwipeRefreshLayout? = null

    private var _binding: EchogramListFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EchogramListFragmentBinding.inflate(inflater, container, false)

        val recyclerView = binding.echogramList
        val context = binding.root.context
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = EchogramRecyclerViewAdapter(this)
        recyclerView.adapter = mAdapter

        mSwipeLayout = binding.root.findViewById<View>(R.id.echogramlistswipelayout) as SwipeRefreshLayout

        mSwipeLayout!!.setOnRefreshListener { mEchogramViewModel!!.refreshEchogramListContent() }

        mSnapViewModel = ViewModelProvider(requireActivity()).get(SnapViewModel::class.java)
        mEchogramViewModel = ViewModelProvider(requireActivity()).get(EchogramViewModel::class.java)
        mEchogramViewModel!!.getEchogramInfos()!!.observe(viewLifecycleOwner
        ) { echogramInfos ->
            mAdapter!!.setEchograms(echogramInfos)
            if (mSwipeLayout != null)
                mSwipeLayout!!.isRefreshing = false
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewEchogramClicked(v: View, echogram: SnapMetadata?) {
        if (echogram?.snapId != null) {
            val snapId = echogram.snapId.toString()
            val bundle = bundleOf(EchogramViewerFragment.ARG_SNAP_ID to snapId)
            v.findNavController().navigate(R.id.action_fragment_snap_to_echogram_viewer_fragment, bundle)
        }
    }

    override fun onShareEchogramClicked(v: View, echogram: SnapMetadata?) {
        mSnapViewModel!!.createDraftFrom(echogram!!)
        Navigation.findNavController(v).navigate(R.id.action_snap_fragment_to_newSnapFragment)
    }

    companion object {
        fun newInstance() = EchogramListFragment()
    }
}

