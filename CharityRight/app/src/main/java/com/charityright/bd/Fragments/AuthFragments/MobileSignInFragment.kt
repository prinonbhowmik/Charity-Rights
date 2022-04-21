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
import com.charityright.bd.ViewModel.Authentication.MobileSignInViewModel
import com.charityright.bd.databinding.FragmentMobileSignInBinding
import kotlinx.coroutines.launch

class MobileSignInFragment : Fragment() {

    private var _binding: FragmentMobileSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var mobileSignInViewModel: MobileSignInViewModel
    private var tempMobile = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMobileSignInBinding.inflate(inflater,container,false)

        mobileSignInViewModel = ViewModelProvider(this).get(MobileSignInViewModel::class.java)
        CustomDialog.init(requireContext())

        mobileSignInViewModel.registrationResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putString("mobile", tempMobile)
                findNavController().navigate(
                    R.id.action_mobileSignInFragment_to_mobileOtpFragment,
                    bundle
                )
            } else {
                Toast.makeText(requireContext(), "" + it?.message, Toast.LENGTH_SHORT).show()
            }

        }

        binding.signInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mobileSignInFragment_to_authenticationFragment)
        }

        binding.signUpBtn.setOnClickListener {
            tempMobile = binding.signUpPhoneET.text.toString()

            if (tempMobile != "" && tempMobile.startsWith("01")){
                mobileSignInViewModel.mobile = tempMobile

                lifecycleScope.launch {
                    mobileSignInViewModel.launchApiCall()
                }

            }else{
                binding.signUpPhoneET.requestFocus()
                binding.signUpPhoneET.error = "Write In Correct Format (01*********)"
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}