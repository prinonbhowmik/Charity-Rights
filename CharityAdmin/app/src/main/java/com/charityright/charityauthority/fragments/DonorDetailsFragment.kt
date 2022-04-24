package com.charityright.charityauthority.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.adapters.DonorDetailsAdapter
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentDonorDetailsBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.viewModels.adminViewModel.donorDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class DonorDetailsFragment : Fragment() {

    private var _binding: FragmentDonorDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBar: LinearLayout

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var donorDetailsAdapter: DonorDetailsAdapter

    private lateinit var viewModel: donorDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonorDetailsBinding.inflate(inflater,container,false)

        appBar = requireActivity().findViewById(R.id.linearLayout)

        viewModel = ViewModelProvider(this).get(donorDetailsViewModel::class.java)
        CustomDialog.init(requireContext())

        val id = requireArguments().getString("id","")
        if (id != ""){
            viewModel.donor_id = id
            lifecycleScope.launch {
                viewModel.launchDonorDetailsApiCall()
            }
        }

        viewModel.donorDetailsResponse.observe(viewLifecycleOwner,{
            if (it?.response_status == "200"){

                Picasso.get().load("${resources.getString(R.string.base_url)}${it.data?.image_url}").placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(binding.imageView)

                binding.nameTV.text = it.data?.name
                binding.areaTV.text = it.data?.address
                binding.amountTV.text = it.data?.amount

                binding.emailTV.text = it.data?.email
                binding.phoneTV.text = it.data?.mobile
                binding.addressTV.text = it.data?.address
                binding.postTV.text = it.data?.post
                binding.countryTV.text = it.data?.country

                if (it.data?.donation_list?.isNotEmpty() == true){

                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noDataLayout.visibility = View.GONE

                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    donorDetailsAdapter = DonorDetailsAdapter(it.data?.donation_list ?: emptyList())
                    binding.recyclerView.layoutManager = layoutManager
                    binding.recyclerView.adapter = donorDetailsAdapter

                }else{
                    binding.recyclerView.visibility = View.GONE
                    binding.noDataLayout.visibility = View.VISIBLE
                }

            }else{
                Toast.makeText(requireContext(),""+it?.message,Toast.LENGTH_SHORT).show()
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
        appBar.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        appBar.visibility = View.GONE
    }
}