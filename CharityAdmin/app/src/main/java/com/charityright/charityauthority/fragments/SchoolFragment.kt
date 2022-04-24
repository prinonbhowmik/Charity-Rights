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
import com.charityright.charityauthority.adapters.SchoolAdapter
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentSchoolBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel.schoolFragmentViewModel
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel
import kotlinx.coroutines.launch


class SchoolFragment : Fragment() {

    private var _binding: FragmentSchoolBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var schoolAdapter: SchoolAdapter

    private lateinit var appBar: LinearLayout

    private lateinit var activityViewModel: adminHomeActivityViewModel
    private lateinit var schoolViewModel: schoolFragmentViewModel

    private  var zoneIdList = ArrayList<String>()
    private  var zoneNameList = ArrayList<String>()
    private var flag: Boolean = false
    private var spinnerPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchoolBinding.inflate(inflater,container,false)

        activityViewModel = ViewModelProvider(requireActivity()).get(adminHomeActivityViewModel::class.java)
        schoolViewModel = ViewModelProvider(this).get(schoolFragmentViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        appBar = requireActivity().findViewById(R.id.linearLayout)


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

        schoolViewModel.schoolListResponse.observe(viewLifecycleOwner,{

            if (it?.data?.isNotEmpty() == true){
                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                schoolAdapter = SchoolAdapter(requireActivity(),findNavController(),"null",it?.data ?: emptyList())
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = schoolAdapter
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
                    schoolViewModel.zone_id = zoneIdList[position]
                    schoolViewModel.launchSchoolApiCall()
                }
            }
        }


        binding.addSchoolBtn.setOnClickListener {
            findNavController().navigate(R.id.action_schoolFragment_to_addSchoolDetailsFragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (spinnerPosition != 0){
            binding.spinner.setSelection(spinnerPosition)
        }
    }
}