package com.charityright.charityauthority.fragments.StudentViewPagerActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charityright.charityauthority.databinding.FragmentDetailsEditBinding


class DetailsEditFragment : Fragment() {

    private var _binding: FragmentDetailsEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsEditBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}