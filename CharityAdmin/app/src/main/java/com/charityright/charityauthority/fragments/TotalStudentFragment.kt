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
import androidx.recyclerview.widget.GridLayoutManager
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.FilterStudentAdapter
import com.charityright.charityauthority.databinding.FragmentTotalStudentBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.TotalStudentViewModel
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel
import kotlinx.coroutines.launch


class TotalStudentFragment : Fragment() {

    private var _binding: FragmentTotalStudentBinding? = null
    private val binding get() = _binding!!

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var studentAdapter: FilterStudentAdapter

    private lateinit var appBar: LinearLayout

    private var zoneIdList = ArrayList<String>()
    private var zoneNameList = ArrayList<String>()
    private var schoolIdList = ArrayList<String>()
    private var schoolNameList = ArrayList<String>()
    private var classIdList = ArrayList<String>()
    private var classNameList = ArrayList<String>()
    private var zoneFlag: Boolean = false
    private var schoolFlag: Boolean = false
    private var classFlag: Boolean = false

    private lateinit var activityViewModel: adminHomeActivityViewModel
    private lateinit var totalStudentViewModel: TotalStudentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalStudentBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        activityViewModel = ViewModelProvider(requireActivity()).get(adminHomeActivityViewModel::class.java)
        totalStudentViewModel = ViewModelProvider(this).get(TotalStudentViewModel::class.java)
        CustomDialog.init(requireContext())

        activityViewModel.zoneResponse.observe(viewLifecycleOwner,{
            if (it?.data?.isNotEmpty() == true){

                if (!zoneFlag){
                    for (i in it.data.indices) {
                        zoneIdList.add(it.data[i]?.id.toString())
                        zoneNameList.add(it.data[i]?.name.toString())
                    }
                    zoneFlag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.textItem, zoneNameList)
                binding.zoneSpinner.adapter = spinnerArrayAdapter
            }
        })

        totalStudentViewModel.schoolListResponse.observe(viewLifecycleOwner,{

            if (it?.data?.isNotEmpty() == true){
                binding.schoolSpinner.visibility = View.VISIBLE
                binding.classSpinner.visibility = View.GONE

                if (!schoolFlag){

                    schoolIdList.clear()
                    schoolNameList.clear()

                    schoolIdList.add("0")
                    schoolNameList.add("--Select Please--")

                    for (i in it.data.indices) {
                        schoolIdList.add(it.data[i]?.id.toString())
                        schoolNameList.add(it.data[i]?.school_name.toString())
                    }
                    schoolFlag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.textItem, schoolNameList)
                binding.schoolSpinner.adapter = spinnerArrayAdapter

            }else{
                schoolIdList.clear()
                schoolNameList.clear()

                totalStudentViewModel.school_id = ""
                totalStudentViewModel.class_id = ""

                binding.schoolSpinner.visibility = View.GONE
                binding.classSpinner.visibility = View.GONE

                val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.textItem, schoolNameList)
                binding.schoolSpinner.adapter = spinnerArrayAdapter

            }

        })

        totalStudentViewModel.classListResponse.observe(viewLifecycleOwner,{

            println("ClassListResponse ${it?.data}")

            if (it?.data?.isNotEmpty() == true){
                binding.classSpinner.visibility = View.VISIBLE
                if (!classFlag){
                    classIdList.clear()
                    classNameList.clear()

                    schoolIdList.add("0")
                    schoolNameList.add("--Select Please--")

                    for (i in it.data.indices) {
                        classIdList.add(it.data[i]?.id.toString())
                        classNameList.add(it.data[i]?.name.toString())
                    }
                    classFlag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.textItem, classNameList)
                binding.classSpinner.adapter = spinnerArrayAdapter

            }else{
                classIdList.clear()
                classNameList.clear()

                totalStudentViewModel.class_id = ""

                binding.classSpinner.visibility = View.GONE

                val spinnerArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.textItem, classNameList)
                binding.classSpinner.adapter = spinnerArrayAdapter

            }

        })

        totalStudentViewModel.studentListResponse.observe(viewLifecycleOwner,{
            if (it?.data?.isNotEmpty() == true){
                binding.noDataLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                gridLayoutManager = GridLayoutManager(requireContext(),3)
                studentAdapter = FilterStudentAdapter(findNavController(), it.data, requireActivity())
                binding.recyclerView.layoutManager = gridLayoutManager
                binding.recyclerView.adapter  = studentAdapter
            }else{
                binding.noDataLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        })

        binding.zoneSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                totalStudentViewModel.zone_id = zoneIdList[position]
                totalStudentViewModel.school_id = ""
                totalStudentViewModel.class_id = ""
                schoolFlag = false
                classFlag = false
                lifecycleScope.launch {
                    totalStudentViewModel.launchSchoolListApiCall()
                }
            }

        }

        binding.schoolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                totalStudentViewModel.zone_id = zoneIdList[binding.zoneSpinner.selectedItemPosition]
                totalStudentViewModel.school_id = schoolIdList[position]
                totalStudentViewModel.class_id = ""
                classFlag = false
                lifecycleScope.launch {
                    totalStudentViewModel.launchClassListApiCall()
                }
            }

        }

        binding.classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                totalStudentViewModel.class_id = classIdList[position]
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.filterBtn.setOnClickListener {

            lifecycleScope.launch {
                totalStudentViewModel.launchStudentListApiCall()
            }

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
    }
}