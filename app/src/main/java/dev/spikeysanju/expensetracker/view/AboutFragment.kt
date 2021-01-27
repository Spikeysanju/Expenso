package dev.spikeysanju.expensetracker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.spikeysanju.expensetracker.databinding.FragmentAboutBinding
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.viewmodel.AboutViewModel


class AboutFragment : BaseFragment<FragmentAboutBinding, AboutViewModel>() {
    override val viewModel: AboutViewModel
        get() = TODO("Not yet implemented")

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAboutBinding.inflate(inflater, container, false)

}
