package com.charityright.charityauthority.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentHomeBinding
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityViewModel: adminHomeActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        activityViewModel = ViewModelProvider(requireActivity()).get(adminHomeActivityViewModel::class.java)

        activityViewModel.dashBoardResponse.observe(viewLifecycleOwner,{
            if (it?.response_status == "200"){

                binding.userNameTV.text = "Hi, ${it.data.name}"
                binding.totalSchoolTV.text = it.data.total_school
                binding.totalDonorTV.text = it.data.total_donar
                binding.totalReportsTV.text = it.data.auditor_report
                binding.totalStudentTV.text = it.data.total_student
                binding.totalDonationTV.text = it.data.total_donar
                binding.totalCampaignTV.text = it.data.total_campaign

            }else{
                Toast.makeText(requireContext(),""+it?.message,Toast.LENGTH_SHORT).show()
            }
        })

        activityViewModel.zoneResponse.observe(viewLifecycleOwner,{
            if (it?.response_status == "200"){

            }else{
                Toast.makeText(requireContext(),""+it?.message,Toast.LENGTH_SHORT).show()
            }
        })

        binding.auditorCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_auditorReportFragment)
        }

        binding.donorCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_donorListFragment)
        }

        binding.totalSchoolCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_totalSchoolListFragment)
        }

        binding.totalDonationCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_totalDonationReportFragment)
        }

        binding.campaignCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_totalCampaignListFragment)
        }

        binding.studentCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_totalStudentFragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}