package com.charityright.charityauthority.fragments.StudentViewPagerActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.adapters.HealthStatusAdapter
import com.charityright.charityauthority.databinding.FragmentHealthStatusBinding
import com.charityright.charityauthority.fragments.StudentDetailsFragment
import com.charityright.charityauthority.viewModels.adminViewModel.StudentDetailsViewModel


class HealthStatusFragment(private val viewModelProvider: StudentDetailsFragment) : Fragment() {

    private var _binding: FragmentHealthStatusBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var healthStatusAdapter: HealthStatusAdapter

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthStatusBinding.inflate(inflater,container,false)

        studentDetailsViewModel = ViewModelProvider(viewModelProvider).get(StudentDetailsViewModel::class.java)

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner,{

            if (it?.response_status == "200"){
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                healthStatusAdapter = HealthStatusAdapter(it.data?.health_status ?: emptyList())
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = healthStatusAdapter
            }

            binding.nameTV.text = "${it?.data?.student_details?.std_name}'s Health Status : "

            val tempBmi = it?.data?.health_status?.get(0)?.bmi.toString().toDouble()

            when {
                tempBmi < 18.5 -> {
                    binding.statusTV.text = "Underweight"
                }
                tempBmi in 18.5..25.0 -> {
                    binding.statusTV.text = "Normal"
                }
                tempBmi in 25.0..30.0 -> {
                    binding.statusTV.text = "Overweight"
                }
                else -> {
                    binding.statusTV.text = "Obese"
                }
            }

            binding.descriptionTV.text = it?.data?.health_status?.get(0)?.details ?: "Not Provided"

        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}