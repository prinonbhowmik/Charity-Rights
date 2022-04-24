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
import com.charityright.charityauthority.adapters.AuditorReportAdapter
import com.charityright.charityauthority.databinding.FragmentAuditorReportListBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.AuditorReportListViewModel
import kotlinx.coroutines.launch


class AuditorReportListFragment : Fragment() {


    private var _binding: FragmentAuditorReportListBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var auditorReportAdapter: AuditorReportAdapter

    private lateinit var viewModel: AuditorReportListViewModel

    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuditorReportListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(AuditorReportListViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        id = requireArguments().getString("id", id)

        lifecycleScope.launch {
            viewModel.launchApiCall(id)
        }

        viewModel.reportListResponse.observe(viewLifecycleOwner, {

            if (it?.data?.isNotEmpty() == true){
                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                auditorReportAdapter =
                    AuditorReportAdapter(findNavController(), requireActivity(), it.data, "flag")
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = auditorReportAdapter
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