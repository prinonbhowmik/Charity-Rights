package com.charityright.charityauthority.auditor.fragments

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.charityauthority.R
import com.charityright.charityauthority.auditor.viewModel.CircumstanceViewModel
import com.charityright.charityauthority.databinding.FragmentSpecialCircumstanceBinding
import com.charityright.charityauthority.util.CustomDialog
import id.zelory.compressor.Compressor.compress
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


class SpecialCircumstanceFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentSpecialCircumstanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var cacheFile: File
    private lateinit var cacheFileUri: Uri

    lateinit var circumstanceViewModel: CircumstanceViewModel

    private var day = 0
    private var month = 0
    private var year = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecialCircumstanceBinding.inflate(inflater,container,false)

        circumstanceViewModel = ViewModelProvider(this).get(CircumstanceViewModel::class.java)
        CustomDialog.init(requireContext())

        circumstanceViewModel.addCircumstanceResponse.observe(viewLifecycleOwner,{
            if (it?.response_status == "200"){
                Toast.makeText(requireContext(),""+it.message,Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_specialCircumstanceFragment_to_auditorHomeFragment)
            }
        })

        binding.dateET.setOnClickListener {
            pickDate()
        }

        binding.submitBtn.setOnClickListener {

            if (binding.locationET.text.toString() == ""){
                binding.locationET.requestFocus()
                binding.locationET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.dateET.text.toString() == ""){
                binding.dateET.requestFocus()
                binding.dateET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.incidentET.text.toString() == ""){
                binding.incidentET.requestFocus()
                binding.incidentET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.affectedET.text.toString() == ""){
                binding.affectedET.requestFocus()
                binding.affectedET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.situationET.text.toString() == ""){
                binding.situationET.requestFocus()
                binding.situationET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.supportET.text.toString() == ""){
                binding.supportET.requestFocus()
                binding.supportET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            if (binding.remarksET.text.toString() == ""){
                binding.remarksET.requestFocus()
                binding.remarksET.error = "Can't Be Empty"
                return@setOnClickListener
            }

            lifecycleScope.launch {

                circumstanceViewModel.launchImageUploadApiCall(binding.locationET.text.toString(),binding.dateET.text.toString(),binding.incidentET.text.toString(),binding.affectedET.text.toString(),binding.situationET.text.toString(),binding.supportET.text.toString(),binding.remarksET.text.toString())
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun pickDate() {
        getDateTimeCalender()
        DatePickerDialog(requireContext(),this,year,month,day).show()
    }

    private fun getDateTimeCalender() {
        val cal: Calendar = Calendar.getInstance()

        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

        binding.dateET.setText("$year-${month + 1}-$day")

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}