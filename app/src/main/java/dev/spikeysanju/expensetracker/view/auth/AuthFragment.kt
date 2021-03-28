package dev.spikeysanju.expensetracker.view.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentAuthBinding
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlin.system.exitProcess

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding, AuthViewModel>() {
    override val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {
            if(viewModel.bioMetricPreference.first())
                authenticate()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAuthBinding.inflate(inflater, container, false)


    private fun authenticate(){
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    findNavController().navigate(R.id.action_authFragment_to_dashboardFragment)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    //todo: show message wait for a second; static err code
                    exitProcess(-1)
                }

                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    //todo: show message wait for a second; static err code
                    exitProcess(-2)
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Please complete bio-metric login to use Expenso")
            .setNegativeButtonText("Exit")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

}
