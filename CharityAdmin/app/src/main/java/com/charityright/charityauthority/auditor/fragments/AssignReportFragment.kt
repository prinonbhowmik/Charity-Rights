package com.charityright.charityauthority.auditor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauditor.Adapters.AssignReportAdapter
import com.charityright.charityauthority.auditor.viewModel.AuditorActivityViewModel
import com.charityright.charityauthority.databinding.FragmentAssignReportBinding
import kotlinx.coroutines.launch


class AssignReportFragment : Fragment() {

    private var _binding: FragmentAssignReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var assignReportAdapter: AssignReportAdapter

    private lateinit var auditorActivityViewModel: AuditorActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignReportBinding.inflate(inflater,container,false)

        auditorActivityViewModel = ViewModelProvider(requireActivity()).get(AuditorActivityViewModel::class.java)

        auditorActivityViewModel.assignedReportResponse.observe(viewLifecycleOwner,{
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            assignReportAdapter = AssignReportAdapter(findNavController(),it?.data ?: emptyList())
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = assignReportAdapter
        })

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            auditorActivityViewModel.launchApiCall()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}