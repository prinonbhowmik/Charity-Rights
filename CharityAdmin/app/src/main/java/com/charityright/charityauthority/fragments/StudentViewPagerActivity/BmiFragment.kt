package com.charityright.charityauthority.fragments.StudentViewPagerActivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.charityright.charityauthority.databinding.FragmentBmiBinding
import com.charityright.charityauthority.fragments.StudentDetailsFragment
import com.charityright.charityauthority.viewModels.adminViewModel.StudentDetailsViewModel
import java.lang.Exception

class BmiFragment(private val viewModelProvider: StudentDetailsFragment) : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmiBinding.inflate(inflater,container,false)

        studentDetailsViewModel = ViewModelProvider(viewModelProvider).get(StudentDetailsViewModel::class.java)

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner,{

            if (it?.response_status == "200"){
                try {

                    val last = it.data?.health_status?.size

                    binding.weightKgTV.text = "${it.data?.health_status?.get(last.toString().toInt() - 1)?.kg} kg"
                    binding.heightCmTV.text = "${it.data?.health_status?.get(last.toString().toInt() - 1)?.cm} cm"
                    binding.weightPoundTV.text = "${it.data?.health_status?.get(last.toString().toInt() - 1)?.pound} lbs"
                    binding.heightFtTV.text = "${it.data?.health_status?.get(last.toString().toInt() - 1)?.feet} ft"
                    binding.bmiTV.text = it.data?.health_status?.get(last.toString().toInt() - 1)?.bmi

                }catch (e: Exception){
                    Log.wtf("BmiFragment",e.message)
                }
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}