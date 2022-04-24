package com.charityright.charityauthority.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.viewpagerAdapters.StudentViewPagerAdapter
import com.charityright.charityauthority.databinding.FragmentStudentDetailsBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.StudentDetailsViewModel
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class StudentDetailsFragment : Fragment() {

    private var _binding: FragmentStudentDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var studentViewPagerAdapter: StudentViewPagerAdapter

    private lateinit var appBar: LinearLayout

    private lateinit var studentDetailsViewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailsBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        studentDetailsViewModel = ViewModelProvider(this).get(StudentDetailsViewModel::class.java)
        CustomDialog.init(requireContext())

        val id = requireArguments().getString("id","")
        if (id != ""){
            studentDetailsViewModel.student_id = id
            lifecycleScope.launch {
                studentDetailsViewModel.launchStudentDetailsApiCall()
            }
        }

        studentDetailsViewModel.studentDetailsResponse.observe(viewLifecycleOwner,{

            if (it?.response_status == "200"){
                Picasso.get().load("${resources.getString(R.string.base_url)}${it.data?.image_url}").placeholder(R.drawable.image_placeholder).into(binding.imageView)

                binding.nameTV.text = it.data?.student_details?.std_name
                binding.ageTV.text = it.data?.age
                binding.detailsTV.text = it.data?.speech

                if (Build.VERSION.SDK_INT >= 24) {
                    binding.nameTV.text = Html.fromHtml(it.data?.student_details?.std_name, Html.FROM_HTML_MODE_LEGACY)
                    binding.ageTV.text = Html.fromHtml(it.data?.age, Html.FROM_HTML_MODE_LEGACY)
                    binding.detailsTV.text = Html.fromHtml(it.data?.speech ?: "", Html.FROM_HTML_MODE_LEGACY)
                } else {
                    binding.nameTV.text = Html.fromHtml(it.data?.student_details?.std_name)
                    binding.ageTV.text = Html.fromHtml(it.data?.age)
                    binding.detailsTV.text = Html.fromHtml(it.data?.speech ?: "")
                }
            }
        })

        binding.editBtn.setOnClickListener {
            findNavController().navigate(R.id.action_studentDetailsFragment_to_studentDetailsEditFragment)
        }

        studentViewPagerAdapter = StudentViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle,this)
        binding.viewPager.adapter = studentViewPagerAdapter

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

    override fun onResume() {
        super.onResume()
        appBar.visibility = View.GONE
    }
}