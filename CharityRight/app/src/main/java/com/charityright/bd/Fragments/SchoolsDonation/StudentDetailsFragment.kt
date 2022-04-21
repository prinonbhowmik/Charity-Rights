package com.charityright.bd.Fragments.SchoolsDonation

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.charityright.bd.Adapters.StudentViewPagerAdapter.StudentViewPagerAdapter
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.StudentDetailsViewModel
import com.charityright.bd.databinding.FragmentStudentDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class StudentDetailsFragment : Fragment() {

    private var _binding: FragmentStudentDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var studentViewPagerAdapter: StudentViewPagerAdapter

    private lateinit var appBarText: TextView

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailsBinding.inflate(inflater,container,false)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "Student Details"

        studentDetailsViewModel = ViewModelProvider(this).get(StudentDetailsViewModel::class.java)
        CustomDialog.init(requireContext())
        CustomSharedPref.init(requireContext())

        val id = requireArguments().getString("id","")
        val school_id = requireArguments().getString("school_id","")
        if (id != ""){
            studentDetailsViewModel.id = id
            lifecycleScope.launch {
                studentDetailsViewModel.launchApiCall()
            }
        }

        studentViewPagerAdapter = StudentViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle,this)
        binding.viewPager.adapter = studentViewPagerAdapter

        binding.tablayout.addTab(binding.tablayout.newTab().setText("Student Details"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Health Status"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("BMI"))

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {

                Picasso.get()
                    .load("${requireActivity().resources.getString(R.string.base_url)}${it.data?.image_url}")
                    .fit().centerCrop()
                    .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)
                    .into(binding.imageView)

                binding.nameTV.text = it.data?.student_details?.std_name
                binding.ageTV.text = it.data?.student_details?.age

                if (Build.VERSION.SDK_INT >= 24) {
                    binding.descriptionTV.text = Html.fromHtml(
                        it.data?.student_details?.speech ?: "",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    binding.descriptionTV.text =
                        Html.fromHtml(it.data?.student_details?.speech ?: "")
                }

            }

        }

        binding.tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tablayout.selectTab(binding.tablayout.getTabAt(position))
            }
        })

        binding.donationBtn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("id",school_id)
            findNavController().navigate(R.id.action_studentDetailsFragment_to_donationStepOneFragment,bundle)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}