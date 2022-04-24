package com.charityright.charityauthority.auditor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.auditor.adapter.NotificationAdapter
import com.charityright.charityauthority.auditor.viewModel.AuditorNotificationViewModel
import com.charityright.charityauthority.databinding.FragmentNotification2Binding
import com.charityright.charityauthority.util.CustomDialog
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotification2Binding? = null
    private val binding get() = _binding!!

    private lateinit var auditorNotificationViewModel: AuditorNotificationViewModel

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotification2Binding.inflate(inflater,container,false)

        auditorNotificationViewModel = ViewModelProvider(this).get(AuditorNotificationViewModel::class.java)
        CustomDialog.init(requireContext())

        auditorNotificationViewModel.notificationResponse.observe(viewLifecycleOwner,{

            if (it?.data?.isNotEmpty() == true){
                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                notificationAdapter = NotificationAdapter(findNavController(), it.data)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = notificationAdapter
            }else{
                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }

        })


        lifecycleScope.launch {
            auditorNotificationViewModel.launchApiCall()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}