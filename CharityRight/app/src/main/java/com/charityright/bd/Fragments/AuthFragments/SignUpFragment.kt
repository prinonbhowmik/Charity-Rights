package com.charityright.bd.Fragments.AuthFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.ViewModel.Authentication.RegistrationViewModel
import com.charityright.bd.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        CustomDialog.init(requireContext())

        registrationViewModel.registationResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signUpFragment_to_authenticationFragment)
            } else {
                Toast.makeText(requireContext(), "Registration Failed!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.signInBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.signUpBtn.setOnClickListener {
            val name = binding.signUpNameET.text.toString()
            val email = binding.signUpEmailET.text.toString()
            val phone = binding.signUpPhoneET.text.toString()
            val pass = binding.signUpPassET.text.toString()

            if (binding.signUpNameET.text.toString() == ""){
                binding.signUpNameET.requestFocus()
                binding.signUpNameET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.signUpEmailET.text.toString() == ""){
                binding.signUpEmailET.requestFocus()
                binding.signUpEmailET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.signUpPhoneET.text.toString() == ""){
                binding.signUpPhoneET.requestFocus()
                binding.signUpPhoneET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (!binding.signUpPhoneET.text.toString().startsWith("01")){
                binding.signUpPhoneET.requestFocus()
                binding.signUpPhoneET.error = "Write In Correct Format (01*********)"
                return@setOnClickListener
            }

            if (binding.signUpPassET.text.toString() == ""){
                binding.signUpPassET.requestFocus()
                binding.signUpPassET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.signUpConfPassET.text.toString() == ""){
                binding.signUpConfPassET.requestFocus()
                binding.signUpConfPassET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.signUpPassET.text.toString() != binding.signUpConfPassET.text.toString()){
                binding.signUpConfPassET.requestFocus()
                binding.signUpConfPassET.error = "Didn't Match"
                return@setOnClickListener
            }

            registrationViewModel.name = name
            registrationViewModel.email = email
            registrationViewModel.phone = phone
            registrationViewModel.password = pass

            lifecycleScope.launch {
                registrationViewModel.launchApiCall()
            }

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}