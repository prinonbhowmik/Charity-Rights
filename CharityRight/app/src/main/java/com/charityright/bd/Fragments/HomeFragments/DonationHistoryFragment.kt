package com.charityright.bd.Fragments.HomeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.bd.Adapters.DonationHistoryAdapter
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.DonationHistoryViewModel
import com.charityright.bd.databinding.FragmentDonationHistoryBinding
import kotlinx.coroutines.launch

class DonationHistoryFragment : Fragment() {


    private var _binding: FragmentDonationHistoryBinding? = null
    private val binding get() = _binding!!

    lateinit var appBarText: TextView

    private lateinit var historyAdapter: DonationHistoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var donationHistoryViewModel: DonationHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonationHistoryBinding.inflate(inflater,container,false)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "Donation History"

        donationHistoryViewModel = ViewModelProvider(this).get(DonationHistoryViewModel::class.java)
        CustomDialog.init(requireContext())

        donationHistoryViewModel.donationHistoryResponse.observe(viewLifecycleOwner) {

            if (it?.donar_list?.isNotEmpty() == true) {

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                historyAdapter = DonationHistoryAdapter(it.donar_list)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = historyAdapter

            } else {

                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE

            }

        }

        if (CustomSharedPref.read("Token","") == ""){
            binding.recyclerView.visibility = View.GONE
            binding.noDataLayout.visibility = View.VISIBLE
        }else{
            lifecycleScope.launch {
                donationHistoryViewModel.launchApiCall()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        appBarText.text = ""
    }
}