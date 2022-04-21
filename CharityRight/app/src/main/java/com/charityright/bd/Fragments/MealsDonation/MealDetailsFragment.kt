package com.charityright.bd.Fragments.MealsDonation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.bd.Adapters.MealDetailsAdapter.MealsImageAdapter
import com.charityright.bd.Models.MealsDetailsModel.Image
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.MealDetailsViewModel
import com.charityright.bd.databinding.FragmentMealDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class MealDetailsFragment : Fragment() {

    private var _binding: FragmentMealDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBarText: TextView

    private lateinit var mealDetailsViewModel: MealDetailsViewModel

    private lateinit var imageAdapter: MealsImageAdapter
    private lateinit var imageLayoutManager: LinearLayoutManager

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealDetailsBinding.inflate(inflater,container,false)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "Campaign Details"

        mealDetailsViewModel = ViewModelProvider(this).get(MealDetailsViewModel::class.java)
        CustomDialog.init(requireContext())
        CustomSharedPref.init(requireContext())

        val id = requireArguments().getString("id","")

        if (id != ""){
            mealDetailsViewModel.id = id
            lifecycleScope.launch {
                mealDetailsViewModel.launchApiCall()
            }
        }

        mealDetailsViewModel.mealDetailsResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {

                //image slider
                if (it.data?.images?.isNotEmpty() == true) {

                    imageLayoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    imageAdapter = MealsImageAdapter(
                        it.data?.images ?: emptyList(),
                        mealDetailsViewModel,
                        requireActivity()
                    )
                    binding.imageRecycler.layoutManager = imageLayoutManager
                    binding.imageRecycler.adapter = imageAdapter

                    startAutoScroll(it.data?.images ?: emptyList())
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    binding.titleTV.text = Html.fromHtml(it.data?.title, Html.FROM_HTML_MODE_LEGACY)
                    binding.objectiveTV.text =
                        Html.fromHtml(it.data?.objective, Html.FROM_HTML_MODE_LEGACY)
                    binding.descriptionTV.text =
                        Html.fromHtml(it.data?.details, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    binding.titleTV.text = Html.fromHtml(it.data?.title)
                    binding.objectiveTV.text = Html.fromHtml(it.data?.objective)
                    binding.descriptionTV.text = Html.fromHtml(it.data?.details)
                }

            }

        }

        binding.donationBtn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("id",id)
            findNavController().navigate(R.id.action_mealDetailsFragment_to_donationStepOneFragment,bundle)

        }

        binding.shareBtn.setOnClickListener {
            try {
                val url = "https://charityright.com.bd/"

                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Check Out This Event On Charity Right Bangladesh Official Website")
                i.putExtra(Intent.EXTRA_TEXT, url)
                startActivity(Intent.createChooser(i, "Share via"))
            }catch (e: Exception){
                Log.wtf("MealDetailsFragment", "onFailure: Share: ", e)
            }
        }

        return binding.root
    }

    private fun startAutoScroll(imageList: List<Image?>) {

        lifecycleScope.launch {
            if (position != (imageList.size - 1)){
                position++
                mealDetailsViewModel.setImagePath(imageList[position]?.url)
                delay(2000)
                startAutoScroll(imageList)
            }else{
                position = 0
                mealDetailsViewModel.setImagePath(imageList[position]?.url)
                delay(2000)
                startAutoScroll(imageList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealDetailsViewModel.getImagePath().observe(viewLifecycleOwner,{
            Picasso.get().load("${resources.getString(R.string.base_url)}$it").placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(binding.imageView)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        appBarText.text = ""
    }
}