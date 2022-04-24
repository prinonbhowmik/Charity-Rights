package com.charityright.charityauthority

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.charityright.charityauthority.databinding.ActivityMainBinding
import com.charityright.charityauthority.databinding.DrawerHeaderBinding
import com.charityright.charityauthority.util.CustomDialog
import com.charityright.charityauthority.util.CustomSharedPref
import com.charityright.charityauthority.viewModels.adminViewModel.adminHomeActivityViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var activityViewModel: adminHomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val headerView: View = binding.drawerNavigationView.getHeaderView(0)
        val headerBinding: DrawerHeaderBinding = DrawerHeaderBinding.bind(headerView)

        activityViewModel = ViewModelProvider(this).get(adminHomeActivityViewModel::class.java)

        activityViewModel.dashBoardResponse.observe(this,{
            headerBinding.userNameTV.text = it?.data?.name
        })

        navController = findNavController(R.id.mainFragment)
        CustomSharedPref.init(this)
        CustomDialog.init(this)

        lifecycleScope.launch {
            activityViewModel.launchDashboardApiCall()
        }

        binding.menuBtn.setOnClickListener {
            if (binding.homeDrawer.isDrawerOpen(Gravity.LEFT)){
                binding.homeDrawer.closeDrawer(GravityCompat.START)
            }else{
                binding.homeDrawer.openDrawer(GravityCompat.START)
            }
        }

        binding.notificationBtn.setOnClickListener {

            findNavController(R.id.mainFragment).navigate(R.id.notificationFragment)

        }

        binding.logoutLayout.setOnClickListener {
            CustomSharedPref.write("TOKEN",null)
            CustomSharedPref.write("TYPE",null)
            startActivity(Intent(this,AuthenticationActivity::class.java))
            finish()
        }

        //bottomNavigationViewSetup
        binding.bottomNavigationView.setupWithNavController(navController)

        //drawerNavigationViewSetup
        binding.drawerNavigationView.setupWithNavController(navController)
    }
}