package com.charityright.bd.Fragments.SchoolsDonation

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.bd.Adapters.SchoolDetailsAdapter.ClassAdapter
import com.charityright.bd.Adapters.SchoolDetailsAdapter.ImageAdapter
import com.charityright.bd.Models.SchoolDetails.Image
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.SchoolDetailsViewModel
import com.charityright.bd.databinding.FragmentSchoolDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class SchoolDetailsFragment : Fragment() {

    private var _binding: FragmentSchoolDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBarText: TextView

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var classAdapter: ClassAdapter

    private var id = ""
    private var name = ""

    private lateinit var imageLayoutManager: LinearLayoutManager
    private lateinit var classLayoutManager: GridLayoutManager

    private lateinit var schoolDetailsViewModel: SchoolDetailsViewModel

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchoolDetailsBinding.inflate(inflater,container,false)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)

        schoolDetailsViewModel = ViewModelProviders.of(this).get(SchoolDetailsViewModel::class.java)
        CustomDialog.init(requireContext())
        CustomSharedPref.init(requireContext())

        try {
            id = requireArguments().getString("id","")
            name = requireArguments().getString("name","")

            appBarText.text = name

            if (id != ""){
                lifecycleScope.launch {
                    schoolDetailsViewModel.id = id
                    schoolDetailsViewModel.launchApiCall()
                }
            }
        }catch (e: Exception){
            Log.wtf("SchoolDetailsFragment",e.message)
        }


        schoolDetailsViewModel.schoolDetailsResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {

                if (Build.VERSION.SDK_INT >= 24) {
                    binding.schoolTitleTV.text =
                        Html.fromHtml(it.data?.school_name, Html.FROM_HTML_MODE_LEGACY)
                    binding.schoolDescriptionTV.text =
                        Html.fromHtml(it.data?.details, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    binding.schoolTitleTV.text = Html.fromHtml(it.data?.school_name)
                    binding.schoolDescriptionTV.text = Html.fromHtml(it.data?.details)
                }

                binding.schoolLocationTV.text = it.data?.zone

                if (it.data?.images?.isNotEmpty() == true){
                    imageLayoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    imageAdapter = ImageAdapter(
                        it.data?.images ?: emptyList(),
                        schoolDetailsViewModel,
                        requireActivity()
                    )
                    binding.imageRecycler.layoutManager = imageLayoutManager
                    binding.imageRecycler.adapter = imageAdapter

                    startAutoScroll(it.data?.images ?: emptyList())
                }


                if (it.data?.class_list?.isNotEmpty() == true) {
                    binding.classRecycler.visibility = View.VISIBLE
                    binding.noDataLayout.visibility = View.GONE

                    classLayoutManager = GridLayoutManager(requireContext(), 2)
                    classAdapter =
                        ClassAdapter(requireActivity(), it.data?.class_list ?: emptyList(), id)
                    binding.classRecycler.layoutManager = classLayoutManager
                    binding.classRecycler.adapter = classAdapter
                } else {
                    binding.classRecycler.visibility = View.GONE
                    binding.noDataLayout.visibility = View.VISIBLE
                }

            }

        }

        return binding.root
    }

    private fun startAutoScroll(imageList: List<Image?>) {


        lifecycleScope.launch {
            if (position != (imageList.size - 1)){
                position++
                schoolDetailsViewModel.setImagePath(imageList[position]?.url)
                delay(2000)
                startAutoScroll(imageList)
            }else{
                position = 0
                schoolDetailsViewModel.setImagePath(imageList[position]?.url)
                delay(2000)
                startAutoScroll(imageList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        schoolDetailsViewModel.getImagePath().observe(viewLifecycleOwner) {
            Picasso.get().load("${resources.getString(R.string.base_url)}$it")
                .placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)
                .into(binding.imageView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        appBarText.text = ""
    }
}