package com.charityright.bd.Fragments.MealsDonation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charityright.bd.databinding.FragmentDonationStepOneBinding
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.charityright.bd.AuthenticateMainActivity
import com.charityright.bd.R
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.ActivityViewModel
import java.lang.Exception


class DonationStepOneFragment : Fragment() {

    private var _binding: FragmentDonationStepOneBinding? = null
    private val binding get() = _binding!!

    //private lateinit var donationViewPagerAdapter: DonationViewPagerAdapter
    private lateinit var activityViewModel: ActivityViewModel

    private lateinit var appBarText: TextView

    private var packageId = ArrayList<String>()
    private var packageName = ArrayList<String>()
    private var packageType = ArrayList<String>()
    private var flag: Boolean = false

    private var gift_type = 0
    private var donation_type = 0
    private var is_anonymous: Boolean = false
    private var gift_amount = ""

    private var token: String? = ""
    private var id: String? = ""
    private var spinnerFlag: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonationStepOneBinding.inflate(inflater, container, false)


        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
        CustomSharedPref.init(requireContext())

        token = CustomSharedPref.read("Token", "")

        try {
            id = CustomSharedPref.read("id", "")
            CustomSharedPref.write("from", "")
            CustomSharedPref.write("id", "")
        } catch (e: Exception) {
            Log.wtf("DonationStepOneFragment Bundle 1", e.message)
        }

        try {
            spinnerFlag = requireArguments().getString("id")
            println("Spinner Flag $spinnerFlag")
        } catch (e: Exception) {
            Log.wtf("DonationStepOneFragment Bundle 2", e.message)
        }

        //set spinner info
        activityViewModel.donationListResponse.observe(viewLifecycleOwner) {

            if (it?.data?.isNotEmpty() == true) {

                if (!flag) {
                    for (i in it.data.indices) {
                        packageId.add(i, it.data[i]?.package_id.toString())
                        packageName.add(i, it.data[i]?.package_name.toString())
                        packageType.add(i, it.data[i]?.package_type.toString())
                    }
                    flag = true
                }

                val spinnerArrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    R.id.textItem,
                    packageName
                )
                binding.spinner.adapter = spinnerArrayAdapter

                if (id != "") {
                    for (i in packageId.indices) {
                        if (id == packageId[i]) {
                            binding.spinner.setSelection(i)
                            binding.spinner.isEnabled = false
                            break
                        }
                    }
                }

                if (spinnerFlag != "") {
                    for (i in packageId.indices) {
                        if (spinnerFlag == packageId[i]) {
                            binding.spinner.setSelection(i)
                            binding.spinner.isEnabled = false
                            break
                        }
                    }
                }

            }
        }

        activityViewModel.userInfoResponse.observe(viewLifecycleOwner) {

            if (it?.data?.address != "") {
                binding.addressET.setText(it?.data!!.address.toString())
                binding.addressET.isFocusable = false
            }

        }

        //set donation type by default
        donation_type = 2
        binding.zakatBtn.isChecked = false
        binding.donationBtn.isChecked = true

        //set donation system
        if (is_anonymous) {
            binding.anonymousBtn.isChecked = true
            binding.addressLayout.visibility = View.GONE
            binding.addressET.setText("")
        }

        binding.oneGift.setOnClickListener {
            gift_type = 1
            binding.oneGift.isChecked = true
            binding.foodGift.isChecked = false
        }

        binding.foodGift.setOnClickListener {
            gift_type = 2
            binding.oneGift.isChecked = false
            binding.foodGift.isChecked = true
        }

        binding.tenCard.setOnClickListener {

            binding.amountET.setText("10")

            binding.tenCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCard
                )
            )
            binding.hundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveHundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.thousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.threeThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.tenThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        binding.hundredCard.setOnClickListener {

            binding.amountET.setText("100")

            binding.tenCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.hundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCard
                )
            )
            binding.fiveHundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.thousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.threeThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.tenThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        binding.fiveHundredCard.setOnClickListener {

            binding.amountET.setText("500")

            binding.tenCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.hundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveHundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCard
                )
            )
            binding.thousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.threeThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.tenThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        binding.thousandCard.setOnClickListener {

            binding.amountET.setText("1000")

            binding.tenCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.hundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveHundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.thousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCard
                )
            )
            binding.threeThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.tenThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        binding.threeThousandCard.setOnClickListener {

            binding.amountET.setText("3000")

            binding.tenCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.hundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveHundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.thousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.threeThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCard
                )
            )
            binding.fiveThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.tenThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        binding.fiveThousandCard.setOnClickListener {

            binding.amountET.setText("5000")

            binding.tenCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.hundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveHundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.thousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.threeThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCard
                )
            )
            binding.tenThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
        binding.tenThousandCard.setOnClickListener {

            binding.amountET.setText("10000")

            binding.tenCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.hundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveHundredCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.thousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.threeThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.fiveThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.tenThousandCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorCard
                )
            )
        }
        binding.amountET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                gift_amount = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

                binding.tenCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.hundredCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.fiveHundredCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.thousandCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.threeThousandCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.fiveThousandCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.tenThousandCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )

            }
        })

        binding.zakatBtn.setOnClickListener {
            donation_type = 1
            binding.zakatBtn.isChecked = true
            binding.donationBtn.isChecked = false
        }

        binding.donationBtn.setOnClickListener {
            donation_type = 2
            binding.zakatBtn.isChecked = false
            binding.donationBtn.isChecked = true
        }

        binding.nextBtn.setOnClickListener {
            if (gift_type != 0) {
                if (binding.amountET.text.toString() != "") {
                    binding.nextBtn.visibility = View.GONE
                    binding.DetailsLayout.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Select Gift Amount",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please Select Any Gift Type", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.anonymousBtn.setOnClickListener {

            if (is_anonymous) {
                is_anonymous = false
                binding.anonymousBtn.isChecked = false
                binding.addressLayout.visibility = View.VISIBLE
            } else {
                is_anonymous = true
                binding.anonymousBtn.isChecked = true
                binding.addressLayout.visibility = View.GONE
            }
        }

        binding.continueBtn.setOnClickListener {

            if (token != "" && token != null) {
                if (gift_type != 0) {
                    if (binding.amountET.text.toString() != "") {
                        if (donation_type != 0) {

                            val bundle = Bundle()
                            bundle.putString(
                                "event_id",
                                packageId[binding.spinner.selectedItemPosition]
                            )
                            bundle.putString(
                                "event_name",
                                packageName[binding.spinner.selectedItemPosition]
                            )
                            bundle.putString(
                                "event_type",
                                packageType[binding.spinner.selectedItemPosition]
                            )
                            bundle.putString("gift_type", gift_type.toString())
                            bundle.putString("donation_type", donation_type.toString())
                            bundle.putString("amount", gift_amount)
                            bundle.putString(
                                "business_name",
                                binding.businessNameET.text.toString()
                            )
                            bundle.putString("address", binding.addressET.text.toString())
                            bundle.putBoolean("anonymous", is_anonymous)

                            findNavController().navigate(
                                R.id.action_donationStepOneFragment_to_donationStepTwoFragment,
                                bundle
                            )

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please Select Donation Type",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please Select Gift Amount",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Select Any Gift Type",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(requireActivity())
                builder.setMessage("You Are Not Signed In. Want to Sign In for Using This Feature?")
                builder.setCancelable(true)

                builder.setPositiveButton("Yes") { dialog, id ->

                    val intent = Intent(
                        requireActivity().applicationContext,
                        AuthenticateMainActivity::class.java
                    )

                    CustomSharedPref.write("from", "donation")
                    CustomSharedPref.write("id", packageId[binding.spinner.selectedItemPosition])

                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }

                builder.setNegativeButton("No") { dialog, id -> dialog.cancel() }

                val alert: AlertDialog = builder.create()
                alert.show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
//        appBarText.text = ""
    }
}