package com.charityright.bd.Fragments.HomeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.charityright.bd.Adapters.HomeFragmentAdapters.HelpAdapter
import com.charityright.bd.Adapters.HomeFragmentAdapters.SchoolMealAdapter
import com.charityright.bd.Adapters.HomeViewPagerAdapter
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.ActivityViewModel
import com.charityright.bd.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import kotlin.math.abs


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var schoolMealAdapter: SchoolMealAdapter
    private lateinit var helpAdapter: HelpAdapter

    private lateinit var mealLayoutManager: LinearLayoutManager
    private lateinit var helpLayoutManager: LinearLayoutManager

    private lateinit var activityViewModel: ActivityViewModel

    private var goalCardImageList = ArrayList<Int>()
    private var goalImageList = ArrayList<Int>()
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
        CustomSharedPref.init(requireContext())

        activityViewModel.userInfoResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                binding.donorNameTV.text = "Hi, ${it.data?.fullname}"
            }

        }

        activityViewModel.campaignListResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                helpLayoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                helpAdapter = HelpAdapter(requireActivity(), it.data)
                binding.helpRecycler.layoutManager = helpLayoutManager
                binding.helpRecycler.adapter = helpAdapter
            }

        }

        activityViewModel.schoolListResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                mealLayoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                schoolMealAdapter = SchoolMealAdapter(requireActivity(), it.data)
                binding.schoolMealRecycler.layoutManager = mealLayoutManager
                binding.schoolMealRecycler.adapter = schoolMealAdapter
            }

        }

        binding.viewEvents.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSchoolMealFragment)
        }

        binding.viewSchools.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSchoolListFragment)
        }

        goalCardImageList.add(R.drawable.home_slider_1)
        goalCardImageList.add(R.drawable.home_slider_2)
        goalCardImageList.add(R.drawable.home_slider_3)
        goalCardImageList.add(R.drawable.home_slider_4)

        goalImageList.add(R.drawable.goal_one)
        goalImageList.add(R.drawable.goal_two)
        goalImageList.add(R.drawable.goal_three)
        goalImageList.add(R.drawable.goal_four)

        homeViewPagerAdapter = HomeViewPagerAdapter(goalCardImageList,goalImageList)
        binding.homeViewPager.adapter = homeViewPagerAdapter

        binding.homeViewPager.clipToPadding = false
        binding.homeViewPager.clipChildren = false
        binding.homeViewPager.offscreenPageLimit = 3
        binding.homeViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r : Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.homeViewPager.setPageTransformer(compositePageTransformer)
        binding.homeViewPager.currentItem = 1

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (CustomSharedPref.read("Token","") != null && CustomSharedPref.read("Token","") != ""){
            lifecycleScope.launch {
                activityViewModel.launchProfileDetailsApiCall()
            }
        }
    }
}