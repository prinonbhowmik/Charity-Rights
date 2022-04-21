package com.charityright.bd.Fragments.HomeFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.bd.Adapters.SchoolFragmentAdapter
import com.charityright.bd.Models.SchoolListBaseModel.Data
import com.charityright.bd.R
import com.charityright.bd.ViewModel.ActivityViewModel
import com.charityright.bd.databinding.FragmentAllSchoolListBinding
import kotlinx.coroutines.launch


class AllSchoolListFragment : Fragment() {

    private var _binding: FragmentAllSchoolListBinding? = null
    private val binding get() = _binding!!

    lateinit var appBarText: TextView

    private lateinit var schoolFragmentAdapter: SchoolFragmentAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var activityViewModel: ActivityViewModel

    private lateinit var data: List<Data?>

    private  var zoneIdList = ArrayList<String>()
    private  var zoneNameList = ArrayList<String>()
    private var flag: Boolean = false
    private var spinnerPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSchoolListBinding.inflate(inflater, container, false)

        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "All Project List"

        activityViewModel.zoneListResponse.observe(viewLifecycleOwner) {
            if (it?.data?.isNotEmpty() == true) {

                if (!flag) {
                    for (i in it.data.indices) {
                        zoneIdList.add(it.data[i]?.id.toString())
                        zoneNameList.add(it.data[i]?.name.toString())
                    }
                    flag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    R.id.textItem,
                    zoneNameList
                )
                binding.spinner.adapter = spinnerArrayAdapter
            }
        }

        activityViewModel.schoolListResponse.observe(viewLifecycleOwner) {

            if (it?.data?.isNotEmpty() == true) {

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                data = it.data

                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                schoolFragmentAdapter = SchoolFragmentAdapter(requireActivity(), it.data)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = schoolFragmentAdapter
            } else {

                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE

            }

        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                lifecycleScope.launch {
                    spinnerPosition = position
                    activityViewModel.launchSchoolListApiCall(zoneIdList[position])
                }
            }
        }

        binding.searchET.addTextChangedListener(textWatcher)

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s == ""){
                schoolFragmentAdapter = SchoolFragmentAdapter(requireActivity(),data)
                binding.recyclerView.adapter = schoolFragmentAdapter
            }else{
                val tempData = ArrayList<Data?>()

                for (i in data.indices){
                    if (data[i]?.school_name?.lowercase()?.contains(s.toString()) == true){
                        tempData.add(data[i])
                    }
                }

                schoolFragmentAdapter = SchoolFragmentAdapter(requireActivity(),tempData)
                binding.recyclerView.adapter = schoolFragmentAdapter

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        appBarText.text = ""
    }

    override fun onResume() {
        super.onResume()
        if (spinnerPosition != 0){
            binding.spinner.setSelection(spinnerPosition)
        }
    }

}