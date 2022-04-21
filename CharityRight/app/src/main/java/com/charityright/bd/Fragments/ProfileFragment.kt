package com.charityright.bd.Fragments

import android.app.Service
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.charityright.bd.R
import com.charityright.bd.ViewModel.ActivityViewModel
import com.charityright.bd.ViewModel.Authentication.ProfileViewModel
import com.charityright.bd.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch


class ProfileFragment : DialogFragment() {


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.color.transparent_black)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.nameET.isFocusable = false
        binding.phoneET.isFocusable = false
        binding.emailET.isFocusable = false
        binding.addressET.isFocusable = false
        binding.postET.isFocusable = false
        binding.countryET.isFocusable = false
        binding.changePassBtn.isEnabled = false

        activityViewModel.userInfoResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                binding.nameET.setText(it.data?.fullname)
                binding.phoneET.setText(it.data?.mobile)
                binding.emailET.setText(it.data?.email)
                binding.addressET.setText(it.data?.address)
                binding.postET.setText(it.data?.post)
                binding.countryET.setText(it.data?.country)
            }

        }

        profileViewModel.profileUpdateResponse.observe(viewLifecycleOwner) {
            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                requireActivity().lifecycleScope.launch {
                    activityViewModel.launchProfileDetailsApiCall()
                }
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Something Went Wrong!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.changePassBtn.setOnCheckedChangeListener { checkBtn, checkStatus ->
            if (checkStatus){
                binding.changePassLayout.visibility = View.VISIBLE
            }else{
                binding.changePassLayout.visibility = View.GONE
            }
        }

        binding.updateBtn.setOnClickListener {
            val tempName = binding.nameET.text.toString()
            val tempEmail = binding.emailET.text.toString()
            val tempAddress = binding.addressET.text.toString()
            val tempPost = binding.postET.text.toString()
            val tempCountry = binding.countryET.text.toString()
            val tempPass = binding.passET.text.toString()

            if (binding.nameET.text.isEmpty()){
                binding.nameET.requestFocus()
                binding.nameET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.emailET.text.isEmpty()){
                binding.emailET.requestFocus()
                binding.emailET.error = "Can't Be Empty"
                return@setOnClickListener
            }


            if (binding.addressET.text.isEmpty()){
                binding.addressET.requestFocus()
                binding.addressET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.postET.text.isEmpty()){
                binding.postET.requestFocus()
                binding.postET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.countryET.text.isEmpty()){
                binding.countryET.requestFocus()
                binding.countryET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            profileViewModel.name = tempName
            profileViewModel.email = tempEmail
            profileViewModel.address = tempAddress
            profileViewModel.post = tempPost
            profileViewModel.country = tempCountry
            profileViewModel.password = tempPass

            lifecycleScope.launch {
                profileViewModel.launchApiCall()
            }

        }

        binding.editBtn.setOnClickListener {

            binding.nameET.isFocusable = true
            binding.nameET.requestFocus()
            binding.nameET.isFocusableInTouchMode = true
            binding.nameET.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.spinner_background
                )
            )
            val imm: InputMethodManager =
                requireContext().getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInputFromWindow(
                binding.nameET.applicationWindowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )


            binding.emailET.isFocusable = true
            binding.emailET.requestFocus()
            binding.emailET.isFocusableInTouchMode = true
            binding.emailET.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.spinner_background
                )
            )


            binding.addressET.isFocusable = true
            binding.addressET.requestFocus()
            binding.addressET.isFocusableInTouchMode = true
            binding.addressET.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.spinner_background
                )
            )


            binding.postET.isFocusable = true
            binding.postET.requestFocus()
            binding.postET.isFocusableInTouchMode = true
            binding.postET.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.spinner_background
                )
            )


            binding.countryET.isFocusable = true
            binding.countryET.requestFocus()
            binding.countryET.isFocusableInTouchMode = true
            binding.countryET.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.spinner_background
                )
            )

            binding.changePassBtn.isEnabled = true

        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}