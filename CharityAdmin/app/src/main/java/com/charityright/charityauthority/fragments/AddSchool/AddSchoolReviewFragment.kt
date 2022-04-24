package com.charityright.charityauthority.fragments.AddSchool

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.adapters.ClassAdapter
import com.charityright.charityauthority.adapters.ClassImageAdapter
import com.charityright.charityauthority.R
import com.charityright.charityauthority.adapters.FieldVisitReportAdapter
import com.charityright.charityauthority.viewModels.adminViewModel.addSchoolViewModel.SchoolViewModel
import com.charityright.charityauthority.databinding.FragmentAddSchoolReviewBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.lang.Exception


class AddSchoolReviewFragment : Fragment() {

    private var _binding: FragmentAddSchoolReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBar: LinearLayout

    private lateinit var classImageAdapter: ClassImageAdapter
    private lateinit var classAdapter: ClassAdapter

    private lateinit var imageLayoutManager: LinearLayoutManager
    private lateinit var classLayoutManager: GridLayoutManager

    private lateinit var reportLayoutManager: LinearLayoutManager
    private lateinit var fieldVisitReportAdapter: FieldVisitReportAdapter

    private lateinit var viewModel: SchoolViewModel
    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSchoolReviewBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        viewModel = ViewModelProviders.of(this).get(SchoolViewModel::class.java)

        try {
            id = requireArguments().getString("id","")
        }catch (e: Exception){
            Log.wtf("AddSchoolReviewFragment", "onCreateView: ArgumentException",e)
        }


        if (id != ""){
            viewModel.id = id
            lifecycleScope.launch {
                viewModel.launchSchoolDetailsApiCall()
                viewModel.launchSchoolVisitReport()
            }
        }

        binding.fieldVisitLayout.setOnClickListener {
            if (binding.fieldVisitDetailsLayout.isVisible){
                binding.fieldVisitDetailsLayout.visibility = View.GONE
                binding.fieldVisitImageView.setImageResource(R.drawable.ic_arrow_down)
            }else{
                binding.fieldVisitDetailsLayout.visibility = View.VISIBLE
                binding.fieldVisitImageView.setImageResource(R.drawable.ic_arrow_up)
                binding.nestedScrollView.post(Runnable { binding.nestedScrollView.fullScroll(View.FOCUS_DOWN) })
            }
        }

        viewModel.schoolDetailsResponse.observe(viewLifecycleOwner,{

            imageLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            classImageAdapter = ClassImageAdapter(it?.data?.images ?: emptyList(),viewModel,requireActivity())
            binding.imageRecycler.layoutManager = imageLayoutManager
            binding.imageRecycler.adapter = classImageAdapter

            binding.avgBmiTV.text = "Average BMI : ${it?.data?.avg_bmi}"

            if (it?.data?.class_list?.isNotEmpty() == true){

                binding.classListTV.visibility = View.VISIBLE
                binding.classRecycler.visibility = View.VISIBLE
                binding.avgLayout.visibility = View.VISIBLE
                binding.avgBmiTV.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                classLayoutManager = GridLayoutManager(requireContext(),2)
                classAdapter = ClassAdapter(findNavController(),it.data?.class_list ?: emptyList())
                binding.classRecycler.layoutManager = classLayoutManager
                binding.classRecycler.adapter = classAdapter

            }else{
                binding.classListTV.visibility = View.GONE
                binding.classRecycler.visibility = View.GONE
                binding.avgLayout.visibility = View.GONE
                binding.avgBmiTV.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }

            binding.titleTV.text = it?.data?.school_name
            binding.descriptionTV.text = it?.data?.details

        })

        viewModel.schoolVisitReportResponse.observe(viewLifecycleOwner,{

            if (it?.data?.isNotEmpty() == true){

                binding.fieldVisitLayout.visibility = View.VISIBLE
                binding.totalVisitedTV.visibility = View.VISIBLE

                reportLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                fieldVisitReportAdapter = FieldVisitReportAdapter(it.data)
                binding.fieldVisitRecycler.layoutManager = reportLayoutManager
                binding.fieldVisitRecycler.adapter = fieldVisitReportAdapter

                var avgPre = 0.0
                var avgAbs = 0.0
                var quantity = it.data.size.toDouble()

                for (i in it.data.indices){
                    avgPre += it.data[i]?.present_std.toString().toDouble()
                    avgAbs += it.data[i]?.absent_std.toString().toDouble()
                }

                binding.AvgPreTV.text = (avgPre/quantity).toString()
                binding.AvgAbsTV.text = (avgAbs/quantity).toString()

                binding.totalVisitedTV.text = "Total Visited : ${it.data.size}"

            }else{
                binding.fieldVisitLayout.visibility = View.GONE
                binding.totalVisitedTV.visibility = View.GONE
            }

        })

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getImagePath().observe(viewLifecycleOwner,{
            Picasso.get().load("${resources.getString(R.string.base_url)}$it").fit().centerCrop().placeholder(R.drawable.image_placeholder).into(binding.imageView)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
//        appBar.visibility = View.VISIBLE
    }


    override fun onResume() {
        super.onResume()
        appBar.visibility =View.GONE
    }
}