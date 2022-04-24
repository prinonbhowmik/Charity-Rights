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
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.adapters.AuditorListAdapter
import com.charityright.charityauthority.databinding.FragmentAuditorListBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.AuditorReportViewModel
import kotlinx.coroutines.launch


class AuditorListFragment : Fragment() {

    private var _binding: FragmentAuditorListBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var auditorListAdapter: AuditorListAdapter

    private lateinit var auditReportViewModel: AuditorReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuditorListBinding.inflate(inflater, container, false)

        auditReportViewModel = ViewModelProvider(this).get(AuditorReportViewModel::class.java)
        CustomDialog.init(requireContext())

        lifecycleScope.launch {
            auditReportViewModel.launchApiCall()
        }

        auditReportViewModel.reportListResponse.observe(viewLifecycleOwner, {

            if (it?.data?.isNotEmpty() == true){

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                auditorListAdapter = AuditorListAdapter(findNavController(), requireActivity(), it.data)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = auditorListAdapter

            }else{

                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }

        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}