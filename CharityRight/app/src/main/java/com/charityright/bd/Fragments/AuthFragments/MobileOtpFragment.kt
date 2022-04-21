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
import com.charityright.bd.HomeActivity
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.Authentication.MobileOtpViewModel
import com.charityright.bd.databinding.FragmentMobileOtpBinding
import kotlinx.coroutines.launch


class MobileOtpFragment : Fragment() {

    private var _binding: FragmentMobileOtpBinding? = null
    private val binding get() = _binding!!

    private lateinit var mobileOtpViewModel: MobileOtpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMobileOtpBinding.inflate(inflater,container,false)

        mobileOtpViewModel = ViewModelProvider(this).get(MobileOtpViewModel::class.java)
        CustomDialog.init(requireContext())
        CustomSharedPref.init(requireContext())

        val mobile = requireArguments().getString("mobile","")

        mobileOtpViewModel.OtpResponse.observe(viewLifecycleOwner) {
            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                CustomSharedPref.write("Token", it.data)
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "" + it?.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.submitBtn.setOnClickListener {
            val otp = binding.otpET.text.toString()
            if (otp != ""){
                mobileOtpViewModel.otp = otp
                mobileOtpViewModel.mobile = mobile

                lifecycleScope.launch {
                    mobileOtpViewModel.launchApiCall()
                }

            }else{
                Toast.makeText(requireContext(),"OTP Field Can't be Empty",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}