package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingBinding
import com.example.playlistmaker.settings.domain.entities.SettingsUri
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE){
        FragmentSettingBinding.inflate(layoutInflater)
    }
    private val viewModel: SettingsScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeLiveData.observe(viewLifecycleOwner) { theme ->
            binding.switchDarkTheme.isChecked = theme.isDark
        }


        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeTheme(isDark = isChecked)
        }

        binding.textShareApp.setOnClickListener {
            startActivity(Intent.createChooser(createShareAppIntent(), SettingsUri.SHARE_APP))
        }

        binding.textSupport.setOnClickListener {
            startActivity(createSupportIntent())
        }

        binding.textUserAgreement.setOnClickListener {
            startActivity(createUserAgreementIntent())
        }
    }
    private fun createShareAppIntent(): Intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_headerText))
        type = "text/plain"
    }

    private fun createSupportIntent(): Intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(SettingsUri.SUPPORT_EMAIL))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
        putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
    }

    private fun createUserAgreementIntent(): Intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(SettingsUri.USER_AGREEMENT)
    }
}