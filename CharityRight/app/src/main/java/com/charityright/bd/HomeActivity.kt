package com.charityright.bd


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.charityright.bd.Fragments.ProfileFragment
import com.charityright.bd.Utils.CustomDialog
import com.charityright.bd.Utils.CustomSharedPref
import com.charityright.bd.ViewModel.ActivityViewModel
import com.charityright.bd.databinding.ActivityHomeBinding
import com.charityright.bd.databinding.DrawerHeaderBinding
import kotlinx.coroutines.launch
import java.lang.Exception
import android.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var navController: NavController

    private lateinit var activityViewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val headerView: View = binding.drawerNavigationView.getHeaderView(0)
        val headerBinding: DrawerHeaderBinding = DrawerHeaderBinding.bind(headerView)

        val navigationView: NavigationView = binding.drawerNavigationView
        val drawerMenu = navigationView.menu

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        val bottomMenu = bottomNavigationView.menu


        navController = findNavController(R.id.homeFragment)
        activityViewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        CustomSharedPref.init(this)
        CustomDialog.init(this)

        activityViewModel.userInfoResponse.observe(this) {
            if (it?.response_status == "200") {
                headerBinding.headerUserNameTV.text = it.data?.fullname
                headerBinding.headerPhoneTV.text = it.data?.mobile
            }
        }

        activityViewModel.donationListResponse.observe(this) {
            if (it?.response_status == "200") {
                try {
                    val intentData = CustomSharedPref.read("from", "")
                    println("intentData $intentData")
                    if (intentData != "" && intentData != "null") {
                        findNavController(R.id.homeFragment).navigate(R.id.action_homeFragment_to_donationStepOneFragment)
                    }
                } catch (e: Exception) {
                    Log.wtf("MainActivity getIntentExtra", e.message)
                }
            }
        }

        lifecycleScope.launch {
            activityViewModel.launchApiCall()
        }

        lifecycleScope.launch {
            activityViewModel.launchSchoolListApiCall("0")
        }

        binding.menuBtn.setOnClickListener {
            if (binding.homeDrawer.isDrawerOpen(Gravity.LEFT)){
                binding.homeDrawer.closeDrawer(GravityCompat.START)
            }else{
                binding.homeDrawer.openDrawer(GravityCompat.START)
            }
        }

        //bottomNavigationViewSetup
        binding.bottomNavigationView.setupWithNavController(navController)

        //drawerNavigationViewSetup
        binding.drawerNavigationView.setupWithNavController(navController)

        //navigation drawer and logout control
        if (CustomSharedPref.read("Token","") != "" && CustomSharedPref.read("Token","") != null){
            binding.logoutLayout.visibility = View.VISIBLE
        }else{
            binding.logoutLayout.visibility = View.GONE
            val notification = drawerMenu.findItem(R.id.noticeFragment)
            val donation = drawerMenu.findItem(R.id.donationHistoryFragment)
            val bottomNotification = bottomMenu.findItem(R.id.noticeFragment)
            notification.isVisible = false
            donation.isVisible = false
            bottomNotification.isEnabled = false
        }

        binding.donationBtn.setOnClickListener {
            findNavController(R.id.homeFragment).navigate(R.id.donationStepOneFragment)
        }

        binding.logoutLayout.setOnClickListener {

            CustomSharedPref.write("Token","")
            CustomSharedPref.write("from","")
            CustomSharedPref.write("id","")
            startActivity(Intent(this,AuthenticateMainActivity::class.java))
            finish()
        }

        headerBinding.headerLayout.setOnClickListener {

            if (CustomSharedPref.read("Token","") != "" && CustomSharedPref.read("Token","") != null){
                binding.homeDrawer.closeDrawer(GravityCompat.START)

                val profileFragment = ProfileFragment()
                profileFragment.isCancelable = false
                profileFragment.show(supportFragmentManager,profileFragment.tag)
            }else{
                Toast.makeText(this,"You Are Not Signed In",Toast.LENGTH_SHORT).show()
            }
        }
    }
}