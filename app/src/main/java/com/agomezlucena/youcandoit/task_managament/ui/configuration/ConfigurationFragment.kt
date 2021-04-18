package com.agomezlucena.youcandoit.task_managament.ui.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.FragmentConfigurationBinding
import com.agomezlucena.youcandoit.getEmergencyContact
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment : Fragment() {
    private lateinit var binding : FragmentConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationBinding.inflate(inflater,container,false)

        binding.contactConfigurationRoot.setOnClickListener(this::navigateToUpdateEmergencyContact)
        binding.tvConfigureContact.setOnClickListener(this::navigateToUpdateEmergencyContact)
        binding.contactIcon.setOnClickListener(this::navigateToUpdateEmergencyContact)

        binding.aboutUsRoot.setOnClickListener(this::showAboutUsDialog)
        binding.tvAboutUs.setOnClickListener(this::showAboutUsDialog)
        binding.aboutUsIcon.setOnClickListener(this::showAboutUsDialog)

        return binding.root
    }

    private fun navigateToUpdateEmergencyContact(v:View){
        val contact = getEmergencyContact(requireContext())
        if(contact.isValid()){
            findNavController().navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToUpdateEmergencyContactFragment(contact))
        } else{
            findNavController().navigate(ConfigurationFragmentDirections.actionConfigurationFragmentToUpdateEmergencyContactFragment(null))
        }
    }

    private fun showAboutUsDialog(v:View){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.about_us)
            .setMessage(R.string.about_us_message)
            .show()
    }
}