package com.charityright.bd.Fragments.SplashFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.bd.HomeActivity
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        CustomSharedPref.init(requireContext())

        println("Device Token: ${CustomSharedPref.read("DeviceToken","")}")

        lifecycleScope.launch {
         splashViewModel.launchApiCall()
        }

        splashViewModel.configResponse.observe(viewLifecycleOwner) {

            if (it != null) {
                CustomSharedPref.write("SSL", it.data)
            }

        }

        lifecycleScope.launch {
            delay(3000)

            //val temp = requireArguments().getString("emon")

            if (CustomSharedPref.read("Token", "") != "") {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()
            } else {
                if (CustomSharedPref.read("first_time", "") == "yes") {
                    findNavController().navigate(R.id.action_splashFragment_to_authenticationFragment)
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_greetingFragment)
                }
            }

        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}