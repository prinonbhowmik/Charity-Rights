package com.charityright.charityauthority.adapters.viewpagerAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.charityright.charityauthority.fragments.StudentViewPagerActivity.*

class StudentViewPagerEditAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> {
                return DetailsEditFragment()
            }

            1-> {
                return HealthStatusEditFragment()
            }

            2->{
                return BmiEditFragment()
            }
        }
        return DetailsEditFragment()
    }
}