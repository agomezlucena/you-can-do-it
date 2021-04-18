package com.agomezlucena.youcandoit.task_managament.ui.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.contact_managament.Contact
import com.agomezlucena.youcandoit.databinding.FragmentUpdateEmergencyContactBinding
import com.agomezlucena.youcandoit.writeContact
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UpdateEmergencyContactFragment : Fragment() {
    private lateinit var binding: FragmentUpdateEmergencyContactBinding
    private val args by navArgs<UpdateEmergencyContactFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateEmergencyContactBinding.inflate(inflater, container, false)
        args.contact?.let {
            var contact = it;
            binding.etContactName.setText(contact.name)
            binding.etPhoneNumber.setText(contact.phoneNumber)
            binding.btnStablishEmergencyContact.setText(R.string.update_emergency_contact)
            binding.btnStablishEmergencyContact.setOnClickListener {
                val newName = binding.etContactName.text.toString()
                val newPhone = binding.etPhoneNumber.text.toString()
                if (validateNewContact(contact, newName, newPhone)) {
                    writeContact(requireContext(), Contact(newName, newPhone))
                    findNavController().navigate(UpdateEmergencyContactFragmentDirections.actionUpdateEmergencyContactFragmentToConfigurationFragment())
                } else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.delete_dialog_title)
                        .setMessage(R.string.emergency_contact_error)
                        .show()
                }
            }
            return binding.root
        }

        binding.btnStablishEmergencyContact.setOnClickListener {
            var contact = Contact("","")
            val newName = binding.etContactName.text.toString()
            val newPhone = binding.etPhoneNumber.text.toString()

            if(validateNewContact(contact,newName,newPhone)){
                writeContact(requireContext(), Contact(newName,newPhone))
                findNavController().navigate(UpdateEmergencyContactFragmentDirections.actionUpdateEmergencyContactFragmentToConfigurationFragment())
            } else{
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete_dialog_title)
                    .setMessage(R.string.emergency_contact_error)
                    .show()
            }
        }

        return binding.root
    }

    private fun validateNewContact(contact: Contact, newName: String, newPhone: String): Boolean {
        return newName != contact.name && newName.isNotBlank() ||
                newPhone != contact.phoneNumber &&
                newPhone.isNotBlank() &&
                newPhone.matches(Regex.fromLiteral("\\+?\\(?([0-9]{3})\\)?[-.]?\\(?([0-9]{3})\\)?[-.]?\\(?([0-9]{4})\\)?"))
    }
}