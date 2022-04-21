package com.charityright.bd.Fragments.StudentViewPagerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.bd.Adapters.HealthStatusAdapter
import com.charityright.bd.Fragments.SchoolsDonation.StudentDetailsFragment
import com.charityright.bd.ViewModel.StudentDetailsViewModel
import com.charityright.bd.databinding.FragmentViewPagerHealthStatusBinding

class ViewPagerHealthStatusFragment(private val viewModelProvider: StudentDetailsFragment) : Fragment() {

    private var _binding: FragmentViewPagerHealthStatusBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var healthStatusAdapter: HealthStatusAdapter

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerHealthStatusBinding.inflate(inflater,container,false)

        studentDetailsViewModel = ViewModelProvider(viewModelProvider).get(StudentDetailsViewModel::class.java)

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
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

            binding.descriptionTV.text = it?.data?.health_status?.get(0)?.details ?: ""

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}