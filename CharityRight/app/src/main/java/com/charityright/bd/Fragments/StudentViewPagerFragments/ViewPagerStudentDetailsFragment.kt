package com.charityright.bd.Fragments.StudentViewPagerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.charityright.bd.Fragments.SchoolsDonation.StudentDetailsFragment
import com.charityright.bd.ViewModel.StudentDetailsViewModel
import com.charityright.bd.databinding.FragmentViewPagerStudentDetailsBinding

class ViewPagerStudentDetailsFragment(private val viewModelProvider: StudentDetailsFragment) : Fragment() {

    private var _binding: FragmentViewPagerStudentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerStudentDetailsBinding.inflate(inflater,container,false)

        studentDetailsViewModel = ViewModelProvider(viewModelProvider).get(StudentDetailsViewModel::class.java)

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                binding.nameTV.text = it.data?.student_details?.std_name
                binding.ageTV.text = it.data?.student_details?.age
                binding.classTV.text = it.data?.student_details?.class_name
                binding.schoolTV.text = it.data?.student_details?.school
                binding.familyTV.text = it.data?.student_details?.local_gurdian
                binding.bioTV.text = it.data?.student_details?.speech
                binding.addressTV.text = it.data?.student_details?.address
                binding.hobbyTV.text = it.data?.student_details?.hobby
                binding.interestTV.text = it.data?.student_details?.interest
                binding.foodTV.text = it.data?.student_details?.food_like
            }

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}