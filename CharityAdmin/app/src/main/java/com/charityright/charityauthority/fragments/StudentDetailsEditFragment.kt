package com.charityright.charityauthority.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.viewpagerAdapters.StudentViewPagerEditAdapter
import com.charityright.charityauthority.databinding.FragmentStudentDetailsEditBinding
import com.google.android.material.tabs.TabLayout

class StudentDetailsEditFragment : Fragment() {

    private var _binding: FragmentStudentDetailsEditBinding? = null
    private val binding get() = _binding!!

    lateinit var studentViewPagerEditAdapter: StudentViewPagerEditAdapter
    private lateinit var appBar: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailsEditBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)



        studentViewPagerEditAdapter = StudentViewPagerEditAdapter(requireActivity().supportFragmentManager,lifecycle)
        binding.viewPager.adapter = studentViewPagerEditAdapter

        binding.tablayout.addTab(binding.tablayout.newTab().setText("Student Details"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Health Status"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("BMI"))

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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}