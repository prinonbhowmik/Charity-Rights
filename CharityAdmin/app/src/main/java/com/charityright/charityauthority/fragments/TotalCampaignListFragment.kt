package com.charityright.charityauthority.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.CampaignAdapter
import com.charityright.charityauthority.databinding.FragmentTotalCampaignListBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.CampaignListViewModel
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel
import kotlinx.coroutines.launch


class TotalCampaignListFragment : Fragment() {

    private var _binding: FragmentTotalCampaignListBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBar: LinearLayout

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var campaignAdapter: CampaignAdapter

    private lateinit var activityViewModel: adminHomeActivityViewModel
    private lateinit var campaignViewModel: CampaignListViewModel

    private  var zoneIdList = ArrayList<String>()
    private  var zoneNameList = ArrayList<String>()
    private var flag: Boolean = false
    private var spinnerPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalCampaignListBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        activityViewModel = ViewModelProvider(requireActivity()).get(adminHomeActivityViewModel::class.java)
        campaignViewModel = ViewModelProvider(this).get(CampaignListViewModel::class.java)

        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        activityViewModel.zoneResponse.observe(viewLifecycleOwner,{
            if (it?.data?.isNotEmpty() == true){

                if (!flag){
                    for (i in it.data.indices) {
                        zoneIdList.add(it.data[i]?.id.toString())
                        zoneNameList.add(it.data[i]?.name.toString())
                    }
                    flag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.textItem, zoneNameList)
                binding.spinner.adapter = spinnerArrayAdapter
            }
        })

        campaignViewModel.campaignListResponse.observe(viewLifecycleOwner,{
            if (it?.data?.isNotEmpty() == true){

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                layoutManager.isSmoothScrollbarEnabled = true
                campaignAdapter = CampaignAdapter(requireActivity(),findNavController(), "totalCampaign",it.data)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = campaignAdapter
            }else{
                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }
        })

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                lifecycleScope.launch {
                    spinnerPosition = position
                    campaignViewModel.zone_id = zoneIdList[position]
                    campaignViewModel.launchCampaignApiCall()
                }
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        appBar.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        appBar.visibility = View.GONE
        if (spinnerPosition != 0){
            binding.spinner.setSelection(spinnerPosition)
        }
    }
}