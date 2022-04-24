package com.charityright.charityauthority.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.charityauthority.adapters.NotificationAdapter
import com.charityright.charityauthority.databinding.FragmentNotificationBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.NotificationViewModel
import kotlinx.coroutines.launch


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater,container,false)

        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        notificationViewModel.notificationBaseResponse.observe(viewLifecycleOwner,{

            if (it?.data?.isNotEmpty() == true){

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                notificationAdapter = NotificationAdapter(it.data)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = notificationAdapter

            }else{

                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
            }

        })

        lifecycleScope.launch {
            notificationViewModel.launchApiCall()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}