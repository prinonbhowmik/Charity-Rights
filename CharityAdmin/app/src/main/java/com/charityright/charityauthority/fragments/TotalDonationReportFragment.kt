package com.charityright.charityauthority.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.DonationListAdapter
import com.charityright.charityauthority.databinding.FragmentTotalDonationReportBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.donationListViewModel
import kotlinx.coroutines.launch
import java.util.*


class TotalDonationReportFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentTotalDonationReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var donationListAdapter: DonationListAdapter

    private lateinit var viewModel: donationListViewModel
    private var donationType = 0

    private var day = 0
    private var month = 0
    private var year = 0
    private var flag = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalDonationReportBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(this).get(donationListViewModel::class.java)
        CustomDialog.init(requireContext())

        binding.totalDonationTV.setBackgroundResource(R.color.colorPrimary)

        lifecycleScope.launch {
            viewModel.donationType = donationType.toString()
            viewModel.startDate = ""
            viewModel.endDate = ""
            viewModel.launchApiCall()
        }

        binding.startDateTV.setOnClickListener {
            pickDate()
            flag = 1
        }

        binding.endDateTV.setOnClickListener {
            pickDate()
            flag = 2
        }

        binding.totalDonationTV.setOnClickListener {
            binding.totalDonationTV.setBackgroundResource(R.color.colorPrimary)
            binding.schoolDonationTV.setBackgroundResource(R.color.button_black)
            binding.campaignDonationTV.setBackgroundResource(R.color.button_black)
            donationType = 0
            val start = binding.startDateTV.text.toString()
            val end = binding.endDateTV.text.toString()

            viewModel.donationType = donationType.toString()
            viewModel.startDate = start
            viewModel.endDate = end

            lifecycleScope.launch {
                viewModel.launchApiCall()
            }

        }

        binding.schoolDonationTV.setOnClickListener {
            binding.totalDonationTV.setBackgroundResource(R.color.button_black)
            binding.schoolDonationTV.setBackgroundResource(R.color.colorPrimary)
            binding.campaignDonationTV.setBackgroundResource(R.color.button_black)
            donationType = 1
            val start = binding.startDateTV.text.toString()
            val end = binding.endDateTV.text.toString()

            viewModel.donationType = donationType.toString()
            viewModel.startDate = start
            viewModel.endDate = end

            lifecycleScope.launch {
                viewModel.launchApiCall()
            }
        }

        binding.campaignDonationTV.setOnClickListener {
            binding.totalDonationTV.setBackgroundResource(R.color.button_black)
            binding.schoolDonationTV.setBackgroundResource(R.color.button_black)
            binding.campaignDonationTV.setBackgroundResource(R.color.colorPrimary)
            donationType = 2
            val start = binding.startDateTV.text.toString()
            val end = binding.endDateTV.text.toString()

            viewModel.donationType = donationType.toString()
            viewModel.startDate = start
            viewModel.endDate = end

            lifecycleScope.launch {
                viewModel.launchApiCall()
            }
        }

        binding.filterBtn.setOnClickListener {
            val start = binding.startDateTV.text.toString()
            val end = binding.endDateTV.text.toString()

            viewModel.donationType = donationType.toString()
            viewModel.startDate = start
            viewModel.endDate = end

            lifecycleScope.launch {
                viewModel.launchApiCall()
                binding.startDateTV.text.clear()
                binding.endDateTV.text.clear()
            }
        }

        viewModel.donationListResponse.observe(viewLifecycleOwner,{

            if (it?.data?.isNotEmpty() == true){

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                donationListAdapter = DonationListAdapter(findNavController(),it?.data ?: emptyList())
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = donationListAdapter

            }else{

                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }

        })

        return binding.root
    }

    private fun pickDate() {
        getDateTimeCalender()
        DatePickerDialog(requireContext(),this,year,month,day).show()
    }

    private fun getDateTimeCalender() {
        val cal: Calendar = Calendar.getInstance()

        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        if (flag == 1){
            binding.startDateTV.setText("$year-${month+1}-$day")
        }else if (flag == 2){
            binding.endDateTV.setText("$year-${month+1}-$day")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}