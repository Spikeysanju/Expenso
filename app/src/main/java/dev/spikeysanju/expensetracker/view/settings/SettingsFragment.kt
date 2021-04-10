package dev.spikeysanju.expensetracker.view.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.databinding.FragmentSettingsBinding
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {
    override val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the item state
        lifecycleScope.launchWhenStarted {
            binding.biometric.isChecked = viewModel.bioMetricPreference.first()
        }

        initViews()
    }

    private fun initViews() = with(binding) {
        biometric.setOnCheckedChangeListener { _, biometricEnabled ->
            viewModel.setBioMetricLock(biometricEnabled)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)
}
