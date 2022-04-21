package com.charityright.bd.Adapters.StudentViewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.charityright.bd.Fragments.SchoolsDonation.StudentDetailsFragment
import com.charityright.bd.Fragments.StudentViewPagerFragments.ViewPagerBmiFragment
import com.charityright.bd.Fragments.StudentViewPagerFragments.ViewPagerHealthStatusFragment
import com.charityright.bd.Fragments.StudentViewPagerFragments.ViewPagerStudentDetailsFragment

class StudentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val studentDetailsFragment: StudentDetailsFragment
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> {
                return ViewPagerStudentDetailsFragment(studentDetailsFragment)
            }

            1-> {
                return ViewPagerHealthStatusFragment(studentDetailsFragment)
            }

            2->{
                return ViewPagerBmiFragment(studentDetailsFragment)
            }
        }
        return ViewPagerStudentDetailsFragment(studentDetailsFragment)
    }
}