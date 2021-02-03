package dev.spikeysanju.expensetracker.view.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.BuildConfig
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentAboutBinding
import dev.spikeysanju.expensetracker.view.base.BaseFragment

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding, AboutViewModel>() {
    override val viewModel: AboutViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        appVersion.text = getString(
            R.string.text_app_version,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )

        license.setOnClickListener {
            launchBrowser(REPO_LICENSE)
        }

        visitURL.setOnClickListener {
            launchBrowser(REPO_URL)
        }
    }

    private fun launchBrowser(url: String) =
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
            startActivity(it)
        }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAboutBinding.inflate(inflater, container, false)

    companion object {
        const val REPO_URL = "https://github.com/Spikeysanju/ExpenseTracker"
        const val REPO_LICENSE = "https://github.com/TheCodeMonks/NYTimes-App/blob/master/LICENSE"
    }
}
