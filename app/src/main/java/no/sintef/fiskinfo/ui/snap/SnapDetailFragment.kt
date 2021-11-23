package no.sintef.fiskinfo.ui.snap

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

import android.net.http.SslError
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController

import no.sintef.fiskinfo.R

class SnapDetailFragment : Fragment() {

    private lateinit var mViewModel: SnapViewModel
    private var _mBinding: no.sintef.fiskinfo.databinding.SnapDetailFragmentBinding? = null

    private val mBinding get() = _mBinding!! // Only valid between onCreateView and onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = DataBindingUtil.inflate(inflater, R.layout.snap_detail_fragment, container, false)
        return mBinding.getRoot()
    }

    // Changed from ViewDataBinding to SnapDetailFragmentBinding above

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(requireActivity()).get(SnapViewModel::class.java)
        configureWebView()
        mViewModel.getSelectedSnap().observe(viewLifecycleOwner, Observer { snap ->
            mBinding.setSnap(snap)
            mBinding.setSnapMetadata(snap?.snapMetadata)
            mBinding.setIncomming(mViewModel!!.isIncomming().value)
            mBinding.setHandlers(this@SnapDetailFragment)
            loadContent(snap?.snapMetadata?.snapId.toString())
        })
    }

    private lateinit var webView: WebView

    fun configureWebView() {
        if (getView() == null) return
        webView = requireView().findViewById(R.id.snapdetail_web_view)
        with (webView.settings) {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            setGeolocationEnabled(true)
            // layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.NORMAL
            //setUseWideViewPort(true)
            //setLoadWithOverviewMode(true)
        }
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.cancel() // Ignore SSL certificate errors
            }
        }

    }

    val DEFAULT_SNAP_FISH_WEB_SERVER_ADDRESS = "https://129.242.16.123:37457/"

    fun loadContent(snapId : String) {
        try {
            if (snapId == null)
                return

            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val snapFishServerUrl = prefs.getString(getString(R.string.pref_snap_web_server_address), DEFAULT_SNAP_FISH_WEB_SERVER_ADDRESS)
            if (snapFishServerUrl != null) {
                val url = snapFishServerUrl + "snap/" + snapId
                webView.loadUrl(url)
            }

        } catch (ex: Exception) {
        }

    }




    fun onViewEchogramHereClicked(v: View) {
        try {
            if (! (mViewModel.getSelectedSnap()?.value?.snapMetadata?.snapId != null))
                return
            var snapId = mViewModel.getSelectedSnap()?.value?.snapMetadata?.snapId.toString()
            var bundle = bundleOf(EchogramViewerFragment.ARG_SNAP_ID to snapId)
            v.findNavController().navigate(R.id.action_snapDetailFragment_to_echogramViewerFragment, bundle)
        } catch (ex: Exception) {
        }

    }

    fun onViewInMapClicked(v: View) {
        val toast = Toast.makeText(this.context, "Not yet implemented!", Toast.LENGTH_SHORT)
        toast.show()
    }

    fun onViewInEchosounderClicked(v: View) {
        val toast = Toast.makeText(this.context, "Not yet implemented!", Toast.LENGTH_SHORT)
        toast.show()
    }

    fun onDeleteSnapClicked(v: View) {
        mViewModel.deleteSelectedSnap()
        v.findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }



    companion object {

        fun newInstance(): SnapDetailFragment {
            return SnapDetailFragment()
        }
    }

}
