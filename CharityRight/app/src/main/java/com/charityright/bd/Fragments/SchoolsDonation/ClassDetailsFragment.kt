package com.charityright.bd.Fragments.SchoolsDonation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.charityright.bd.Adapters.SchoolDonationAdapters.StudentAdapter
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.ClassDetailsViewModel
import com.charityright.bd.databinding.FragmentClassDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class ClassDetailsFragment : Fragment() {

    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var layoutManager: GridLayoutManager

    private lateinit var appBarText: TextView

    private lateinit var classDetailsViewModel: ClassDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater,container,false)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "Class Details"

        classDetailsViewModel = ViewModelProvider(this).get(ClassDetailsViewModel::class.java)
        CustomSharedPref.init(requireContext())

        val id = requireArguments().getString("id","")
        val school_id = requireArguments().getString("school_id","")

        if (id != ""){
            classDetailsViewModel.id = id
            lifecycleScope.launch {
                classDetailsViewModel.launchApiCall()
            }
        }

        classDetailsViewModel.classDetailsResponse.observe(viewLifecycleOwner) {

            Picasso.get()
                .load("${requireActivity().resources.getString(R.string.base_url)}${it?.data?.image}")
                .fit().centerCrop().placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder).into(binding.imageView)

            if (it?.data?.student_list?.isNotEmpty() == true) {

                binding.classRecycler.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = GridLayoutManager(requireContext(), 3)
                studentAdapter = StudentAdapter(
                    requireActivity(),
                    it.data?.student_list ?: emptyList(),
                    school_id
                )
                binding.classRecycler.layoutManager = layoutManager
                binding.classRecycler.adapter = studentAdapter
            } else {
                binding.classRecycler.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }


        }

        binding.donationBtn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("id",school_id)
            findNavController().navigate(R.id.action_classDetailsFragment_to_donationStepOneFragment,bundle)

        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}