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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.SnapInboxFragmentBinding
import no.sintef.fiskinfo.model.SnapMessage

/**
 * A fragment for showing the inbox or outbox of snap messages.
 *
 *
 */

/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class SnapBoxFragment : Fragment(), SnapRecyclerViewAdapter.OnSnapInteractionListener {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private var mViewModel: SnapViewModel? = null
    private var mAdapter: SnapRecyclerViewAdapter? = null
    private var mSwipeLayout: SwipeRefreshLayout? = null
    private var mIsInbox : Boolean = true
    private val IS_INBOX = "IsInbox"

    private var _binding: SnapInboxFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIsInbox = arguments?.getBoolean(IS_INBOX, true) ?: true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        _binding = SnapInboxFragmentBinding.inflate(inflater, container, false)

        val listView = binding.inboxList
        val context = binding.root.context
        listView.layoutManager = LinearLayoutManager(context)
        mAdapter = SnapRecyclerViewAdapter(this, mIsInbox)
        listView.adapter = mAdapter

        mSwipeLayout = binding.inboxswipelayout

        if (mIsInbox) // Refresh only supported on inbox as outbox is currently only local on phone
            mSwipeLayout!!.setOnRefreshListener { mViewModel!!.refreshInboxContent() }


        mViewModel = ViewModelProvider(requireActivity()).get(SnapViewModel::class.java)
        val box = if (mIsInbox) mViewModel!!.getInboxSnaps() else mViewModel!!.getOutboxSnaps()

        box?.observe(viewLifecycleOwner
        ) { snaps ->
            mAdapter!!.setSnaps(snaps)
            if (mSwipeLayout != null)
                mSwipeLayout!!.isRefreshing = false
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewSnapClicked(v: View, snap: SnapMessage?) {
        mViewModel!!.selectSnap(snap, mIsInbox)
        Navigation.findNavController(v).navigate(R.id.action_fragment_snap_to_snapDetailFragment)
    }

    override fun onViewSnapInMapClicked(v: View, snap: SnapMessage?) {
        val toast = Toast.makeText(this.context, "Not yet implemented!", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Snap List")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "SnapBoxFragment")
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(isInbox: Boolean) = SnapBoxFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_INBOX, isInbox)
            }
        }
    }
}
