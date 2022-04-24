package com.charityright.charityauthority.adapters.viewpagerAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.charityright.charityauthority.fragments.StudentDetailsFragment
import com.charityright.charityauthority.fragments.StudentViewPagerActivity.BmiFragment
import com.charityright.charityauthority.fragments.StudentViewPagerActivity.DetailsFragment
import com.charityright.charityauthority.fragments.StudentViewPagerActivity.HealthStatusFragment

class StudentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val studentDetailsViewModel: StudentDetailsFragment
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> {
                return DetailsFragment(studentDetailsViewModel)
            }

            1-> {
                return HealthStatusFragment(studentDetailsViewModel)
            }

            2->{
                return BmiFragment(studentDetailsViewModel)
            }
        }
        return DetailsFragment(studentDetailsViewModel)
    }
}