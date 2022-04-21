package com.charityright.bd.Fragments.HomeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charityright.bd.Adapters.NotificationFragmentAdapter
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.NotificationViewModel
import com.charityright.bd.databinding.FragmentNoticeBinding
import kotlinx.coroutines.launch


class NoticeFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    lateinit var appBarText: TextView

    private lateinit var notificationFragmentAdapter: NotificationFragmentAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeBinding.inflate(inflater,container,false)

        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "Notifications"

        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        CustomSharedPref.init(requireContext())
        CustomDialog.init(requireContext())

        if (CustomSharedPref.read("Token","") == ""){
            binding.recyclerView.visibility = View.GONE
            binding.noDataLayout.visibility = View.VISIBLE
        }

        notificationViewModel.notificationBaseResponse.observe(viewLifecycleOwner) {

            if (it?.data?.isNotEmpty() == true) {

                binding.recyclerView.visibility = View.VISIBLE
                binding.noDataLayout.visibility = View.GONE

                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                notificationFragmentAdapter =
                    NotificationFragmentAdapter(requireContext(), it.data, findNavController())
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = notificationFragmentAdapter

            } else {

                binding.recyclerView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE

            }

        }

        lifecycleScope.launch {
            notificationViewModel.launchApiCall()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        appBarText.text = ""
    }
}