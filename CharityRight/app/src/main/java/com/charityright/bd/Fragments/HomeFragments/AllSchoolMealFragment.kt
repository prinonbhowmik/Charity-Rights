package com.charityright.bd.Fragments.HomeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.bd.Adapters.HomeFragmentAdapters.MealFragmentAdapter
import com.charityright.bd.R
import com.charityright.bd.ViewModel.ActivityViewModel
import com.charityright.bd.databinding.FragmentAllSchoolMealBinding

class AllSchoolMealFragment : Fragment() {

    private var _binding: FragmentAllSchoolMealBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealFragmentAdapter: MealFragmentAdapter
    private lateinit var layoutManager: LinearLayoutManager

    lateinit var appBarText: TextView

    private lateinit var activityViewModel: ActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSchoolMealBinding.inflate(inflater,container,false)

        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "All Campaign List"

        activityViewModel.campaignListResponse.observe(viewLifecycleOwner) {

            if (it?.data?.isNotEmpty() == true) {

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                mealFragmentAdapter = MealFragmentAdapter(requireActivity(), it.data)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = mealFragmentAdapter

            } else {

                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE

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