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
package no.sintef.fiskinfo.ui.overview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.OverviewFragmentBinding
import no.sintef.fiskinfo.ui.login.LoginViewModel


class OverviewFragment : Fragment(), OverviewRecyclerViewAdapter.OnOverviewCardInteractionListener {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private var _binding: OverviewFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAction2Clicked(v: View, item: OverviewCardItem?) {
        item?.action2Listener?.onClick(v);
    }

    override fun onAction1Clicked(v: View, item: OverviewCardItem?) {
        item?.action1Listener?.onClick(v);
    }

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel
    private var mSwipeLayout: SwipeRefreshLayout? = null

    private val loginViewModel: LoginViewModel by activityViewModels()

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
        viewModel.refreshFromPreferences(requireContext())

        viewModel.overviewList.observe(viewLifecycleOwner, Observer {
            mAdapter?.setOverviewItems(it)
            mSwipeLayout?.isRefreshing = false
        })
        viewModel.overviewInfo.observe(viewLifecycleOwner, Observer {
            viewModel.updateOverviewCardItems(requireContext())
            mSwipeLayout?.isRefreshing = false
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());

        _binding = OverviewFragmentBinding.inflate(inflater, container, false)
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.overview_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = OverviewRecyclerViewAdapter(this)
        recyclerView.adapter = mAdapter

        mSwipeLayout = binding.root.findViewById(R.id.overview_fragement_swipe_layout) as SwipeRefreshLayout
        mSwipeLayout!!.setOnRefreshListener {
            viewModel?.refreshOverviewItems(requireContext())
        }

        val navController = findNavController()

        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> initViewModel()
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> navController.navigate(R.id.login_fragment)
                else -> {}
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mAdapter: OverviewRecyclerViewAdapter? = null

    override fun onResume() {
        super.onResume()
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Overview")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "OverviewFragment")
        }
    }

}
