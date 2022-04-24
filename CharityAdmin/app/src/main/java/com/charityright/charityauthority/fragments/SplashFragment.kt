package com.charityright.charityauthority.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.AuditorActivity
import com.charityright.charityauthority.MainActivity
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentSplashBinding
import com.charityright.charityauthority.util.CustomSharedPref
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)

        CustomSharedPref.init(requireContext())
        val type = CustomSharedPref.read("TYPE","")

        //val temp = requireArguments().getString("emon")

        this.lifecycleScope.launch {
            delay(3000)

            when (type) {
                "1" -> {
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                    requireActivity().finish()
                }
                "2" -> {
                    startActivity(Intent(requireContext(),AuditorActivity::class.java))
                    requireActivity().finish()
                }
                else -> {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}