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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

import no.sintef.fiskinfo.R
import android.net.http.SslError
import android.webkit.SslErrorHandler
import androidx.preference.PreferenceManager
import no.sintef.fiskinfo.databinding.EchogramViewerFragmentBinding

class EchogramViewerFragment : Fragment() {

    companion object {
        fun newInstance() = EchogramViewerFragment()
        const val ARG_SNAP_ID = "snap_id"
    }

    private var _binding: EchogramViewerFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var webView: WebView

    private var snapId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            snapId = it.getString(ARG_SNAP_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EchogramViewerFragmentBinding.inflate(inflater, container, false)

        configureWebView()
        loadContent()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureWebView() {
        if (getView() == null) return
        webView = requireView().findViewById(R.id.echogramviewer_fragment_web_view)
        with (webView.settings) {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            setGeolocationEnabled(true)
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        }
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.cancel() // Ignore SSL certificate errors
            }
        }

    }

    private val DEFAULT_SNAP_FISH_WEB_SERVER_ADDRESS = "https://129.242.16.123:37457/"

    private fun loadContent() {
        try {
            if (snapId == null)
                return

            val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val snapFishServerUrl = prefs.getString(getString(R.string.pref_snap_web_server_address), DEFAULT_SNAP_FISH_WEB_SERVER_ADDRESS)
            if (snapFishServerUrl != null) {
                //val snapFishWebServerUrl = snapFishServerUrl.replace("5002", "5006").replace("http:", "https:")
                //val url = snapFishWebServerUrl + "snap/" + snapId
                val url = snapFishServerUrl + "snap/" + snapId
                webView.loadUrl(url)
            }

        } catch (ex: Exception) {
        }

    }
}
