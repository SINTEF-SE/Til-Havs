package no.sintef.fiskinfo.ui.consent

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.databinding.ConsentFragmentBinding
import no.sintef.fiskinfo.ui.login.LoginViewModel

class ConsentFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private var _binding: ConsentFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ConsentFragmentBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        val consent = prefs.getBoolean("user_consent_to_terms", false)

        val consentSwitch = binding.consentSwitch
        consentSwitch.isChecked = consent
        consentSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.withdraw_consent_title))
                    .setMessage(resources.getString(R.string.withdraw_consent_description))
                    .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                        // Revert the switch back
                        consentSwitch.isChecked = true
                    }
                    .setPositiveButton(resources.getString(R.string.withdraw_consent_confirm)) { _, _ ->
                        //prefs.edit().putBoolean("user_consent_to_terms", isChecked).apply()
                        prefs.edit().clear().apply()
                        loginViewModel.clearAuthentication()
                        // clear login

                        Navigation.findNavController(binding.root).popBackStack(R.id.fragment_overview, false)
                        // navigate back to root
                        //while (Navigation.findNavController(v).popBackStack());
                        //Navigation.findNavController(v).popBackStack()
                    }
                    .show()

                // Responds to switch being checked/unchecked

            } else {
                prefs.edit().putBoolean("user_consent_to_terms", isChecked).apply()
                Navigation.findNavController(binding.root).popBackStack()
            }
        }
        val textView = binding.consentText
//        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml( getString(R.string.user_consent_description), Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(getString(R.string.user_consent_description))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
/*
    private fun removeConsent() {
        loginViewModel.clearAuthentication()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().clear().apply()
    }
*/

}