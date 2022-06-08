package com.kapilguru.trainer.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.kapilguru.trainer.*
import com.kapilguru.trainer.announcement.AnnouncementActivity
import com.kapilguru.trainer.login.LoginActivity
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.changePassword.model.LogoutRequest
import com.kapilguru.trainer.ui.reports.ReportsActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.home_custom_action_bar.*
import kotlinx.android.synthetic.main.home_custom_action_bar.view.*

class HomeActivity : AppCompatActivity() {

    lateinit var home_drawer_layout: DrawerLayout
    lateinit var homeScreenViewModel: HomeScreenViewModel
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  initialise view and viewModel
        init()
        // ask camera permissions, read and write permissions
        askUserPermission()
        // set up bottom navigation
        setUpBottomNav()
        // action bar
        setCustomActionBar()
        // drawer layout setup
        setUpSlider()

        viewModelObservers()
    }

    private fun setUpSlider() {
        val drawerLlayout: NavigationView = findViewById(R.id.home_drawer_layout)
        drawerLlayout.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.log_out -> {
                    //write your implementation here
                    if (home_drawer_layout.isDrawerOpen(GravityCompat.END)) {
                        home_drawer_layout.closeDrawer(GravityCompat.END)
                    }
                    val pref = StorePreferences(this)
                    homeScreenViewModel.logoutUser(LogoutRequest(pref.trainerId, pref.trainerToken))

                    true
                }
                R.id.reports -> {
                    if (home_drawer_layout.isDrawerOpen(GravityCompat.END)) {
                        home_drawer_layout.closeDrawer(GravityCompat.END)
                    }
                    startActivity(Intent(this, ReportsActivity::class.java))
                    true
                }
                else -> {
                    if (home_drawer_layout.isDrawerOpen(GravityCompat.END)) {
                        home_drawer_layout.closeDrawer(GravityCompat.END)
                    }
                    false
                }
            }
        }
    }

    private fun setUpBottomNav() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.itemIconTintList = null
        home_drawer_layout = findViewById(R.id.home_container)
        val navController = findNavController(R.id.navigation_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_profile, R.id.navigation_myclassroom,
            R.id.navigation_referandearn, R.id.navigation_howtouse))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        enableOrDisableBottomNavigationMenuItems()
    }

    private fun enableOrDisableBottomNavigationMenuItems() {
        if (StorePreferences(this).isProfileUpdated == 1) {
            shouldEnableBottomNavMenuItems(true)
        } else {
            shouldEnableBottomNavMenuItems(false)
        }
    }

    /*
    * This function sets the menu click enable or disable functionality to menu items*/
    fun shouldEnableBottomNavMenuItems(enableBottomNavMenuItems: Boolean) {
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        navView.menu.findItem(R.id.navigation_home).isEnabled = enableBottomNavMenuItems
        navView.menu.findItem(R.id.navigation_myclassroom).isEnabled = enableBottomNavMenuItems
        navView.menu.findItem(R.id.navigation_referandearn).isEnabled = enableBottomNavMenuItems
        navView.menu.findItem(R.id.navigation_howtouse).isEnabled = enableBottomNavMenuItems
        if (!enableBottomNavMenuItems) {
            val navController = findNavController(R.id.navigation_host_fragment)
            navController.navigate(R.id.navigation_profile)
        }
    }

    private fun init() {
        setContentView(R.layout.activity_home)
        homeScreenViewModel = ViewModelProvider(this, HomeScreenViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(HomeScreenViewModel::class.java)
        dialog = CustomProgressDialog(this)
    }

    private fun viewModelObservers() {
        homeScreenViewModel.logoutResponse.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    clearAllSharedPreferences()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
            }
        }

        homeScreenViewModel.notificationCountResponse.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let { it ->
                        it.notificationCountResponseApi?.let { list ->
                            if (list.isNotEmpty()) setNotificationCount(list[0])
                        }
                    }

                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    private fun setNotificationCount(info: DataItem?) {
        info?.let {
            tv_Count.text = if (it.currCount!! - it.prevCount!! > 0) {
                (it.currCount!! - it.prevCount!!).toString()
            } else {
                ""
            }
        }
    }

    private fun clearAllSharedPreferences() {
        StorePreferences(this).clearStorePreferences()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun setCustomActionBar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mCustomView: View = inflater.inflate(R.layout.home_custom_action_bar, null)
        mCustomView.menu.setOnClickListener {
            if (home_drawer_layout.isDrawerOpen(GravityCompat.END)) {
                home_drawer_layout.closeDrawer(GravityCompat.END)
            } else {
                home_drawer_layout.openDrawer(GravityCompat.END)
            }
        }
        mCustomView.aCImg_notification.setOnClickListener {
            navigateToMessages()
        }
        supportActionBar?.customView = mCustomView
        val toolbar: Toolbar = (mCustomView.parent) as Toolbar
        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
        )
        supportActionBar!!.setCustomView(mCustomView, layoutParams)
        toolbar.setContentInsetsAbsolute(0, 0)
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))
        toolbar.setPadding(0, 0, 0, 0)
        toolbar.setContentInsetsAbsolute(0, 0)
    }

    private fun navigateToMessages() {
        startActivity(Intent(this, AnnouncementActivity::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_HOME_ACTIVITY))
    }

    companion object {
        var showProfile = "showProfile"
    }

    fun askUserPermission() {
        Dexter.withContext(this).withPermissions(
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    var data = p0?.isAnyPermissionPermanentlyDenied
                }

                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    //
                }
            }

            ).check()
    }

    override fun onResume() {
        super.onResume()
        homeScreenViewModel.notificationCountApiCall()
    }

}