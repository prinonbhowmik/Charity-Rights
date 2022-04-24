package com.charityright.charityauthority.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentConfirmPasswordBinding
import com.charityright.charityauthority.databinding.FragmentForgotBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.ConfirmPassViewModel
import com.charityright.charityauthority.viewModels.adminViewModel.ForgetViewModel
import kotlinx.coroutines.launch
import java.lang.Exception


class ConfirmPasswordFragment : Fragment() {

    private var _binding: FragmentConfirmPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmPassViewModel: ConfirmPassViewModel
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmPasswordBinding.inflate(inflater,container,false)

        try {
            email = requireArguments().getString("email","")
        }catch (e : Exception){
            Log.wtf("ConfirmPasswordFragment", "onArgumentReceive: ",e)
        }

        confirmPassViewModel = ViewModelProvider(this).get(ConfirmPassViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        confirmPassViewModel.confPassResponse.observe(viewLifecycleOwner, {
            if (it?.response_status == "200") {

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_confirmPasswordFragment_to_loginFragment)

            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.submitBtn.setOnClickListener {
            val tempOtp = binding.otpET.text.toString()
            val tempPass = binding.passET.text.toString()
            val tempConfPass = binding.confPassET.text.toString()

            if (tempOtp != ""){
                if (tempPass != ""){
                    if (tempConfPass != "" && tempConfPass == tempPass){

                        confirmPassViewModel.email = email
                        confirmPassViewModel.pass = tempPass
                        confirmPassViewModel.otp = tempOtp

                        lifecycleScope.launch {
                            confirmPassViewModel.launchApiCall()
                        }

                    }else{
                        Toast.makeText(requireContext(), "Check Confirm Password Again", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Email Field Can't be Empty", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "OTP Field Can't be Empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}