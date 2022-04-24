package com.charityright.charityauthority.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.AuditorActivity
import com.charityright.charityauthority.MainActivity
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentLoginBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.LoginViewModel
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    private var role = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        println("Device Token: ${CustomSharedPref.read("DeviceToken","")}")

        loginViewModel.loginResponse.observe(viewLifecycleOwner, {
            if (it?.response_status == "200") {
                CustomSharedPref.write("TOKEN", it.data)
                CustomSharedPref.write("TYPE",it.user_type)
                if (it.user_type == "1") {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                } else if (it.user_type == "2") {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(), AuditorActivity::class.java))
                    requireActivity().finish()
                }
            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.adminTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.adminTV.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))

        binding.auditorTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        binding.auditorTV.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        role = "1"

        binding.adminTV.setOnClickListener {
            binding.adminTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.adminTV.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )

            binding.auditorTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.auditorTV.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )

            role = "1"
        }

        binding.auditorTV.setOnClickListener {
            binding.auditorTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.auditorTV.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )

            binding.adminTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.adminTV.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )

            role = "2"
        }

        binding.signInBtn.setOnClickListener {
            val tempEmail = binding.signInEmailET.text.toString()
            val tempPass = binding.signInPassET.text.toString()

            if (tempEmail != "") {
                if (tempPass != "") {
                    loginViewModel.email = tempEmail
                    loginViewModel.password = tempPass
                    loginViewModel.userType = role
                    loginViewModel.deviceToken = CustomSharedPref.read("DeviceToken","").toString()

                    lifecycleScope.launch {
                        loginViewModel.launchApiCall()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password Field Can't be Empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Email Field Can't be Empty", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.signInForgetBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}