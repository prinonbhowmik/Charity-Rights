package com.charityright.charityauthority.fragments.StudentViewPagerActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.charityright.charityauthority.databinding.FragmentDetailsBinding
import com.charityright.charityauthority.fragments.StudentDetailsFragment
import com.charityright.charityauthority.viewModels.adminViewModel.StudentDetailsViewModel


class DetailsFragment(private val viewModelProvider: StudentDetailsFragment) : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater,container,false)

        studentDetailsViewModel = ViewModelProvider(viewModelProvider).get(StudentDetailsViewModel::class.java)

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner,{

            if (it?.response_status == "200"){
                binding.nameTV.text = it.data?.student_details?.std_name
                binding.ageTV.text = it.data?.student_details?.age
                binding.classTV.text = it.data?.student_details?.`class`
                binding.schoolTV.text = it.data?.student_details?.school
                binding.familyTV.text = it.data?.student_details?.local_gurdian
                binding.addressTV.text = it.data?.student_details?.address
                binding.hobbyTV.text = it.data?.student_details?.hobby
                binding.foodTV.text = it.data?.student_details?.food_like
            }

        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}