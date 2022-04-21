package com.charityright.bd.Fragments.SplashFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.charityright.bd.R
import com.charityright.bd.databinding.FragmentGreetingBinding
import com.charityright.bd.Utils.CustomSharedPref


class GreetingFragment : Fragment() {

    private var _binding: FragmentGreetingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGreetingBinding.inflate(inflater,container,false)

        CustomSharedPref.init(requireContext())

        binding.getStartedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_greetingFragment_to_authenticationFragment)
            CustomSharedPref.write("first_time","yes")
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}