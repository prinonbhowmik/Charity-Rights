package com.charityright.charityauthority.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.StudentAdapter
import com.charityright.charityauthority.databinding.FragmentClassDetailsBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel.ClassViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class ClassDetailsFragment : Fragment() {

    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var layoutManager: GridLayoutManager

    private lateinit var classViewModel: ClassViewModel

    private lateinit var appBar: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        classViewModel = ViewModelProvider(this).get(ClassViewModel::class.java)
        CustomDialog.init(requireContext())

        val id = requireArguments().getString("id","")
        if (id != ""){
            classViewModel.id = id
            lifecycleScope.launch {
                classViewModel.launchClassDetailsApiCall()
            }
        }

        classViewModel.classDetailsResponse.observe(viewLifecycleOwner,{

            Picasso.get().load("${resources.getString(R.string.base_url)}${it?.data?.image}").placeholder(R.drawable.image_placeholder).into(binding.coverImg)

            if (it?.data?.student_list?.isNotEmpty() == true){
                binding.classRecycler.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = GridLayoutManager(requireContext(),3)
                studentAdapter = StudentAdapter(findNavController(),"null", it.data?.student_list ?: emptyList(),requireActivity())
                binding.classRecycler.layoutManager = layoutManager
                binding.classRecycler.adapter = studentAdapter
            }else{
                binding.classRecycler.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }

        })

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

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