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
package no.sintef.fiskinfo.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import net.openid.appauth.*
import no.sintef.fiskinfo.BuildConfig
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.LoginFragmentBinding
import no.sintef.fiskinfo.util.AuthStateManager


class LoginFragment : Fragment() {
    companion object {
        fun newInstance() = LoginFragment()

        const val ISSUER_URI = BuildConfig.SERVER_ID_URL
        const val CLIENT_ID = BuildConfig.FISKINFO_BW_CLIENT_ID
        const val REDIRECT_URI = BuildConfig.FISKINFO_BW_CLIENT_REDIRECT_URL
        const val SCOPE = BuildConfig.FISKINFO_BW_CLIENT_SCOPE
        const val REQCODE_AUTH = 100
        const val CLIENT_SECRET = BuildConfig.FISKINFO_BW_CLIENT_SECRET
    }
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mAuthService : AuthorizationService
    private lateinit var authStateManager : AuthStateManager
    private lateinit var loginButton: Button
    private lateinit var consentButton: Button
    private lateinit var consentStepText: TextView
    private lateinit var loginStepText: TextView

    private var _binding: LoginFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        authStateManager = AuthStateManager.getInstance(this.requireContext())

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val consent = prefs.getBoolean("user_consent_to_terms", false)
        binding.consentStepLayout.visibility = if (consent) View.GONE else View.VISIBLE
        binding.loginStepLayout.visibility = if (consent) View.VISIBLE else View.GONE

        loginButton = binding.signInButton
        loginButton.setOnClickListener {
            startAuthentication()
        }

        consentButton = binding.consentStepButton.findViewById(R.id.consent_step_button)
        consentButton.setOnClickListener {
            Navigation.findNavController(this.requireView()).navigate(R.id.consentFragment)
        }

        mAuthService = AuthorizationService(this.requireActivity())

        val navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
        }

        viewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    navController.popBackStack()
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION ->
                    Snackbar.make(
                        requireView(),
                        R.string.login_error_incorrect_password,
                        Snackbar.LENGTH_SHORT
                    ).show()
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> Snackbar.make(
                    requireView(),
                    R.string.login_error_incorrect_password,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun startAuthentication() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val consent = prefs.getBoolean("user_consent_to_terms", false)
        if (true) { //!consent)
            //Navigation.findNavController(this.requireView()).navigate(R.id.consentFragment)
        //else {
            AuthorizationServiceConfiguration.fetchFromIssuer(Uri.parse(ISSUER_URI),
                fun(config: AuthorizationServiceConfiguration?, ex: AuthorizationException?) {
                    if (config != null) {
                        val req = AuthorizationRequest
                            .Builder(
                                config,
                                CLIENT_ID,
                                ResponseTypeValues.CODE,
                                Uri.parse(REDIRECT_URI)
                            )
                            .setScope(SCOPE)
                            .build()

                        if (mAuthService == null)
                            mAuthService = AuthorizationService(this.requireActivity())
                        val intent =
                            mAuthService.getAuthorizationRequestIntent(req)

                        startActivityForResult(intent, REQCODE_AUTH)
                    } else {
                        if (ex != null) {
                            val m = Throwable().stackTrace[0]
                            //Log.e(LOG_TAG, "${m}: ${ex}")
                        }
                        showLoginFailedMessage("Login authorization failed", ex?.toString())
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQCODE_AUTH) {
            if (resultCode == Activity.RESULT_OK) {
                val resp = AuthorizationResponse.fromIntent(data!!)
                val ex = AuthorizationException.fromIntent(data)

                authStateManager.updateAfterAuthorization(resp, ex)

                // Proceed with token request if response is not null, otherwise just
                // stay on the login page
                if (resp != null) {
                    val clientAuth: ClientAuthentication = ClientSecretBasic(CLIENT_SECRET)
                    mAuthService
                        .performTokenRequest(
                            resp!!.createTokenExchangeRequest(),
                            clientAuth
                        ) { resp2, ex2 ->
                            if (resp2 != null) {

                                authStateManager.updateAfterTokenResponse(resp2, ex2)
                                viewModel.authenticate()

                            } else {
                                showLoginFailedMessage("Could not get login token", ex2.toString())
                            }
                        }
                } else  {
                    showLoginFailedMessage("Login cancelled", "")
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                showLoginFailedMessage("Login cancelled", "")
            }
        }
    }

    private fun showLoginFailedMessage(message : String, details : String?) {
        Snackbar.make(requireView(), message , Snackbar.LENGTH_LONG)
            .show()
    }

}
