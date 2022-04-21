package com.charityright.bd.Fragments.StudentViewPagerFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.charityright.bd.Fragments.SchoolsDonation.StudentDetailsFragment
import com.charityright.bd.ViewModel.StudentDetailsViewModel
import com.charityright.bd.databinding.FragmentViewPagerBmiBinding
import java.lang.Exception

class ViewPagerBmiFragment(private val viewModelProvider: StudentDetailsFragment) : Fragment() {

    private var _binding: FragmentViewPagerBmiBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBmiBinding.inflate(inflater,container,false)

        studentDetailsViewModel = ViewModelProvider(viewModelProvider).get(StudentDetailsViewModel::class.java)

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                try {

                    val last = it.data?.health_status?.size

                    binding.weightKgTV.text =
                        "${it.data?.health_status?.get(last.toString().toInt() - 1)?.kg} kg"
                    binding.heightCmTV.text =
                        "${it.data?.health_status?.get(last.toString().toInt() - 1)?.cm} cm"
                    binding.weightPoundTV.text =
                        "${it.data?.health_status?.get(last.toString().toInt() - 1)?.pound} lbs"
                    binding.heightFtTV.text =
                        "${it.data?.health_status?.get(last.toString().toInt() - 1)?.feet} ft"
                    binding.bmiTV.text =
                        it.data?.health_status?.get(last.toString().toInt() - 1)?.bmi

                } catch (e: Exception) {
                    Log.wtf("BmiFragment", e.message)
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}