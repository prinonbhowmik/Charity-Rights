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
import com.charityright.bd.ViewModel.Authentication.ForgetViewModel
import com.charityright.bd.databinding.FragmentForgetPasswordBinding
import kotlinx.coroutines.launch


class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var forgetViewModel: ForgetViewModel
    private var tempEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater,container,false)

        forgetViewModel = ViewModelProvider(this).get(ForgetViewModel::class.java)
        CustomDialog.init(requireContext())

        forgetViewModel.forgetResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()

                val bundle = Bundle()
                bundle.putString("email", tempEmail)

                findNavController().navigate(
                    R.id.action_forgetPasswordFragment_to_confirmPasswordFragment,
                    bundle
                )
            } else {
                Toast.makeText(requireContext(), "" + it?.message, Toast.LENGTH_SHORT).show()
            }

        }

        binding.forgetSendBtn.setOnClickListener {

            tempEmail = binding.forgetMobileET.text.toString()

            if (tempEmail != ""){
                forgetViewModel.email = tempEmail

                lifecycleScope.launch {
                    forgetViewModel.launchApiCall()
                }

            }else{
                Toast.makeText(requireContext(),"Email / Phone Number Field Can't Be Empty",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}