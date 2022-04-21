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
import com.charityright.bd.ViewModel.Authentication.ConfirmPassViewModel
import com.charityright.bd.databinding.FragmentConfirmPasswordBinding
import kotlinx.coroutines.launch

class ConfirmPasswordFragment : Fragment() {


    private var _binding: FragmentConfirmPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmPassViewModel: ConfirmPassViewModel
    private var tempEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmPasswordBinding.inflate(inflater,container,false)

        confirmPassViewModel = ViewModelProvider(this).get(ConfirmPassViewModel::class.java)
        CustomDialog.init(requireContext())

        tempEmail = requireArguments().getString("email","")


        confirmPassViewModel.resetResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_confirmPasswordFragment_to_authenticationFragment)
            } else {
                Toast.makeText(requireContext(), "" + it?.message, Toast.LENGTH_SHORT).show()
            }

        }


        binding.submitBtn.setOnClickListener {
            val otp = binding.otpET.text.toString()
            val pass = binding.passET.text.toString()
            val confPass = binding.confPassET.text.toString()

            if (otp != ""){
                if (pass != ""){
                    if (confPass != "" && confPass == pass){

                        confirmPassViewModel.email = tempEmail
                        confirmPassViewModel.otp = otp
                        confirmPassViewModel.pass = pass

                        lifecycleScope.launch {
                            confirmPassViewModel.launchApiCall()
                        }

                    }else{
                        Toast.makeText(requireContext(),"Check Confirm Password",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),"Password Can't Be Empty",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),"OTP Can't Be Empty",Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}