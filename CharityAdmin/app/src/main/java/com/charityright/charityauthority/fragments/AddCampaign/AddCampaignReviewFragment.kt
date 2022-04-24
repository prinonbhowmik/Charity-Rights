package com.charityright.charityauthority.fragments.AddCampaign

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentAddCampaignReviewBinding
import com.charityright.charityauthority.model.admin.campaignDetails.campaignDetailsBaseReponse
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.campaignDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.lang.Exception


class AddCampaignReviewFragment : Fragment() {

    private var _binding: FragmentAddCampaignReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBar: LinearLayout

    private lateinit var detailsViewModel: campaignDetailsViewModel
    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCampaignReviewBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        detailsViewModel = ViewModelProvider(this).get(campaignDetailsViewModel::class.java)
        CustomDialog.init(requireContext())

        try {
            id = requireArguments().getString("id","")
            if (id != ""){
                detailsViewModel.campaign_id = id
                lifecycleScope.launch {
                    detailsViewModel.launchCampaignDetailsApiCall()
                }
            }
        }catch (e: Exception){
            Log.wtf("AddCampaignDetailsFragment", "onArgumentsReceive: ",e)
        }

        detailsViewModel.campaignDetailsResponse.observe(viewLifecycleOwner,{
            if(it?.response_status == "200"){

                Picasso.get().load("${requireActivity().resources.getString(R.string.base_url)}${it.data?.cover_img}").fit().centerCrop().placeholder(R.drawable.image_placeholder).into(binding.imageView)

                binding.titleTV.text = it.data?.title
                binding.areaTV.text = it.data?.zone
                binding.dateTV.text = "Date: ${it.data?.date}"
                binding.descriptionTV.text = it.data?.details
                binding.objectiveTV.text = it.data?.objective
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
        appBar.visibility =View.GONE
    }
}