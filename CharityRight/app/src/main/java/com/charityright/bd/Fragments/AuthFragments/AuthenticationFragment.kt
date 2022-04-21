package com.charityright.bd.Fragments.AuthFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.bd.HomeActivity
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.Authentication.AuthenticationViewModel
import com.charityright.bd.databinding.FragmentAuthenticationBinding
import kotlinx.coroutines.launch

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!

    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater,container,false)
        CustomDialog.init(requireContext())
        CustomSharedPref.init(requireContext())

        println("Device Token 1: ${CustomSharedPref.read("DeviceToken","")}")

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        CustomSharedPref.init(requireContext())

        authenticationViewModel.authenticationResponse.observe(viewLifecycleOwner) {
            if (it?.response_status == "200") {
                CustomSharedPref.write("Token", it.data)
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()

                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()

            } else {
                Toast.makeText(
                    requireContext(),
                    "Email & Password Doesn't Match",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.signInForgetBtn.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_forgetPasswordFragment)
        }

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_signUpFragment)
        }

        binding.signInBtn.setOnClickListener {
            val email = binding.signInEmailET.text.toString()
            val pass = binding.signInPassET.text.toString()

            if (email != "" && pass != ""){
                lifecycleScope.launch {
                    authenticationViewModel.email = email
                    authenticationViewModel.password = pass
                    authenticationViewModel.token = CustomSharedPref.read("DeviceToken","").toString()

                    authenticationViewModel.launchApiCall()
                }
            }else{
                Toast.makeText(requireContext(),"Email & Password Files Can't Be Empty",Toast.LENGTH_SHORT).show()
            }
        }

        binding.signInMobileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_mobileSignInFragment)
        }

        binding.skipBtn.setOnClickListener {
            startActivity(Intent(requireContext(),HomeActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}