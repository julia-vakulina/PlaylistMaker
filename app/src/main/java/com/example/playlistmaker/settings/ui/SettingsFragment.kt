package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var themeSwitcher: SwitchCompat
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        themeSwitcher = binding.themeSwitcher

        viewModel.isNightModeOnLiveData.observe(viewLifecycleOwner) {
            switchThemeSwitcher(it)
        }
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

        val buttonShare = binding.buttonShare
        val buttonLegal = binding.buttonLegal
        val buttonSupport = binding.buttonSupport

        buttonShare.setOnClickListener {
            viewModel.shareApp()
        }
        buttonSupport.setOnClickListener {
            viewModel.openEmail()
        }
        buttonLegal.setOnClickListener {
            viewModel.openTerms()
        }

    }
    private fun switchThemeSwitcher(isNightModeOn: Boolean) {
        themeSwitcher.isChecked = isNightModeOn
    }
}