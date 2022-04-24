package com.charityright.charityauthority.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentForgotBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.ForgetViewModel
import kotlinx.coroutines.launch


class ForgotFragment : Fragment() {

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!

    private lateinit var forgetViewModel: ForgetViewModel
    private var tempEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotBinding.inflate(inflater,container,false)

        forgetViewModel = ViewModelProvider(this).get(ForgetViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        forgetViewModel.forgetResponse.observe(viewLifecycleOwner, {
            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                val bundle = Bundle()
                bundle.putString("email",tempEmail)

                findNavController().navigate(R.id.action_forgotFragment_to_confirmPasswordFragment,bundle)
            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.forgetSendBtn.setOnClickListener {
            tempEmail = binding.forgetEmailET.text.toString()

            if (tempEmail != ""){
                forgetViewModel.email = tempEmail

                lifecycleScope.launch {
                    forgetViewModel.launchApiCall()
                }

            }else{
                Toast.makeText(requireContext(), "Email Field Can't be Empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}