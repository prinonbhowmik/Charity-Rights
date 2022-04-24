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
import com.charityright.charityauthority.adapters.DonorListAdapter
import com.charityright.charityauthority.databinding.FragmentDonorListBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.donorListViewModel
import kotlinx.coroutines.launch


class DonorListFragment : Fragment() {

    private var _binding: FragmentDonorListBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var donorListAdapter: DonorListAdapter

    private lateinit var donorViewModel: donorListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonorListBinding.inflate(inflater,container,false)

        donorViewModel = ViewModelProvider(this).get(donorListViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        lifecycleScope.launch {
            donorViewModel.launchApiCall()
        }

        donorViewModel.donorListResponse.observe(viewLifecycleOwner,{
            if (it?.response_status == "200"){

                binding.totalAmountTV.text = "${it.total_donation} Tk"

                if (it.donar_list.isNotEmpty()){

                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noDataLayout.visibility = View.GONE

                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    donorListAdapter = DonorListAdapter(requireActivity(),findNavController(),it.donar_list)
                    binding.recyclerView.layoutManager = layoutManager
                    binding.recyclerView.adapter = donorListAdapter

                }else{
                    binding.recyclerView.visibility = View.GONE
                    binding.noDataLayout.visibility = View.VISIBLE
                }

            }else{
                Toast.makeText(requireContext(),""+it?.message,Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}