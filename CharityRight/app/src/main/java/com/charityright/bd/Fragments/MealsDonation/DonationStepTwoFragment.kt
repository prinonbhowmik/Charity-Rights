package com.charityright.bd.Fragments.MealsDonation

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.ActivityViewModel
import com.charityright.bd.ViewModel.DonationViewModel
import com.charityright.bd.databinding.FragmentDonationStepTwoBinding
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import kotlinx.coroutines.launch

class DonationStepTwoFragment : Fragment() {

    private var _binding: FragmentDonationStepTwoBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBarText: TextView

    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var donationViewModel: DonationViewModel
    private var password_fix = ""
    private var event_id = ""
    private var event_name = ""
    private var event_type = ""
    private var gift_type = ""
    private var donation_type = ""
    private var amount = ""
    private var business_name = ""
    private var address = ""
    private var is_anonymous: Boolean = false

    private lateinit var sslCommerzInitialization: SSLCommerzInitialization

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonationStepTwoBinding.inflate(inflater, container, false)
        appBarText = requireActivity().findViewById(R.id.AppBarTV)
        appBarText.text = "Donation Step 2"

        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
        donationViewModel = ViewModelProvider(this).get(DonationViewModel::class.java)

        event_id = requireArguments().getString("event_id").toString()
        event_name = requireArguments().getString("event_name").toString()
        event_type = requireArguments().getString("event_type").toString()
        gift_type = requireArguments().getString("gift_type").toString()
        donation_type = requireArguments().getString("donation_type").toString()
        amount = requireArguments().getString("amount").toString()
        business_name = requireArguments().getString("business_name").toString()
        address = requireArguments().getString("address").toString()
        is_anonymous = requireArguments().getBoolean("anonymous")

        if (is_anonymous) {
            binding.addressLayout.visibility = View.GONE
            binding.statusText.visibility = View.VISIBLE
        } else {
            binding.addressLayout.visibility = View.VISIBLE
            binding.statusText.visibility = View.GONE
        }

        donationViewModel.donationResponse.observe(viewLifecycleOwner) {

            if (it?.response_status == "200") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_donationStepTwoFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }

        }

        activityViewModel.userInfoResponse.observe(viewLifecycleOwner) {

            if (it?.data?.fullname != "") {
                binding.fullNameET.setText(it?.data!!.fullname.toString())
                binding.fullNameET.isFocusable = false
            }

            if (it.data?.email != "") {
                binding.emailET.setText(it.data!!.email.toString())
                binding.emailET.isFocusable = false
            }

            if (it.data?.password_check == "0") {
                binding.registerDonationBtn.visibility = View.GONE
            }

            password_fix = it.data?.password_check.toString()

        }

        val wordtoSpan: Spannable =
            SpannableString("Donation Step 2 out of 2")

        wordtoSpan.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            ), 9, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.donationStep2TextView.text = wordtoSpan

        binding.registerDonationBtn.setOnCheckedChangeListener { button, p1 ->
            if (button!!.isChecked) {
                binding.passLayout.visibility = View.VISIBLE
            } else {
                binding.passLayout.visibility = View.GONE
            }
        }

        binding.continueBtn.setOnClickListener {

            if (is_anonymous){
                sslPaymentProcess(amount)
            }else{
                if (binding.fullNameET.text.toString() != "") {
                    if (binding.emailET.text.toString() != "") {
                        if (password_fix == "0") {
                            sslPaymentProcess(amount)
                        } else {
                            if (binding.registerDonationBtn.isChecked) {
                                if (binding.donationPassET.text.toString() != "") {
                                    if (binding.donationConfPassET.text.toString() != "" && binding.donationPassET.text.toString() == binding.donationConfPassET.text.toString()) {
                                        sslPaymentProcess(amount)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Please Check Confirm Password Again",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Please Fill Up Password Email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                sslPaymentProcess(amount)
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please Fill Up Your Email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Fill Up Your Full Name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun sslPaymentProcess(amount: String) {
        val timestamp = (System.currentTimeMillis() / 1000L).toString()


        sslCommerzInitialization = if (CustomSharedPref.read("SSL","").equals("0")){
            SSLCommerzInitialization(getString(R.string.ssl_store_id), getString(R.string.ssl_store_password),
                amount.toDouble(), SSLCCurrencyType.BDT, "Charity" + timestamp + (Math.random() * 5000000 + 1).toInt(),
                "Charity", SSLCSdkType.LIVE)
        }else{
            SSLCommerzInitialization(getString(R.string.ssl_sand_store_id), getString(R.string.ssl_sand_store_password), amount.toDouble(), SSLCCurrencyType.BDT, "Charity" + timestamp + (Math.random() * 5000000 + 1).toInt(), "Charity", SSLCSdkType.TESTBOX)
        }

        IntegrateSSLCommerz.getInstance(requireContext())
            .addSSLCommerzInitialization(sslCommerzInitialization)
            .buildApiCall(object : SSLCTransactionResponseListener {
                override fun transactionSuccess(sslcTransactionInfoModel: SSLCTransactionInfoModel) {
                    val transactionID = sslcTransactionInfoModel.tranId
                    val valId = sslcTransactionInfoModel.valId
                    Log.wtf("transaction", transactionID)
                    Log.wtf("valId", valId)
                    Log.wtf("date ", sslcTransactionInfoModel.tranDate)
                    Log.wtf("brand ", sslcTransactionInfoModel.cardBrand)

                    donationViewModel.event_id = event_id
                    donationViewModel.event_name = event_name
                    donationViewModel.event_type = event_type
                    donationViewModel.payment_date = sslcTransactionInfoModel.tranDate
                    donationViewModel.gift_type = gift_type
                    donationViewModel.donation_type = donation_type
                    donationViewModel.amount = amount
                    donationViewModel.method_name = sslcTransactionInfoModel.cardBrand
                    donationViewModel.tran_id = sslcTransactionInfoModel.tranId
                    donationViewModel.fullname = binding.fullNameET.text.toString()
                    donationViewModel.bussiness_name = business_name
                    donationViewModel.address = address
                    donationViewModel.email = binding.emailET.text.toString()

                    if (is_anonymous) {
                        donationViewModel.info_verify = "2"
                    } else {
                        if (password_fix == "0") {
                            donationViewModel.info_verify = "0"
                            donationViewModel.password = ""
                        } else {
                            donationViewModel.info_verify = "1"
                            donationViewModel.password = binding.donationPassET.text.toString()
                        }
                    }

                    lifecycleScope.launch {
                        donationViewModel.launchApiCall()
                    }


                }

                override fun transactionFail(s: String) {
                    Log.wtf("paymentFail", s)
                }

                override fun merchantValidationError(s: String) {}
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        appBarText.text = ""
    }
}