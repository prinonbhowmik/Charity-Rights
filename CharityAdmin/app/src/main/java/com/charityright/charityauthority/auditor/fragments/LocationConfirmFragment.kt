package com.charityright.charityauthority.auditor.fragments


import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.R
import com.charityright.charityauthority.auditor.viewModel.AuditorActivityViewModel
import com.charityright.charityauthority.databinding.FragmentLocationConfirmBinding
import java.lang.Exception

class LocationConfirmFragment : Fragment() {

    private var _binding: FragmentLocationConfirmBinding? = null
    private val binding get() = _binding!!
    private var id = ""
    private var lat = ""
    private var lon = ""
    private var task = ""

    private lateinit var auditorActivityViewModel: AuditorActivityViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationConfirmBinding.inflate(inflater,container,false)

        try {
            id = requireArguments().getString("id","")
            lat = requireArguments().getString("lat","")
            lon = requireArguments().getString("lon","")
            task = requireArguments().getString("task","")
        }catch (e: Exception){
            Log.wtf("LocationConfirmFragment",e.message)
        }

        auditorActivityViewModel = ViewModelProvider(requireActivity()).get(AuditorActivityViewModel::class.java)

        val resultArray = FloatArray(2)

        try {
            val distanceInMeters = Location.distanceBetween(lat.toDouble(),lon.toDouble(),auditorActivityViewModel.lat.toDouble(),auditorActivityViewModel.lon.toDouble(),resultArray)
            if (resultArray[0] <= 300){
                binding.layout.visibility = View.VISIBLE
                binding.confirmBtn.visibility = View.VISIBLE
                binding.confirmText.visibility = View.VISIBLE
                binding.dismissBtn.visibility = View.GONE
                binding.dismissText.visibility = View.GONE
            }else{
                binding.layout.visibility = View.VISIBLE
                binding.confirmBtn.visibility = View.GONE
                binding.confirmText.visibility = View.GONE
                binding.dismissBtn.visibility = View.VISIBLE
                binding.dismissText.visibility = View.VISIBLE
            }
        }catch (e: Exception){
            Log.wtf("Location Confirm Fragment",e.message)
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.confirmBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",id)
            bundle.putString("task",task)
            findNavController().navigate(R.id.action_locationConfirmFragment_to_visitFromFragment,bundle)
        }

        binding.dismissBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}