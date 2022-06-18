package com.kapilguru.trainer.student.homeActivity.dashboard;

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.trainer.student.homeActivity.studentGallery.StudentGalleryFragment
import com.kapilguru.trainer.student.homeActivity.studentTestimonials.StudentTestimonialsFragment
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.StudentFragmentDashBoardBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.DialogFragmentCustom
import com.kapilguru.trainer.student.homeActivity.models.CreateLeadRequest
import com.kapilguru.trainer.student.homeActivity.models.StudentDashBoardCustomTabModel
import com.kapilguru.trainer.student.homeActivity.models.StudentDashBoardItem
import com.kapilguru.trainer.student.homeActivity.popularAndTrending.PopularAndTrendingFragment
import com.kapilguru.trainer.student.homeActivity.recordedFragment.StudentRecordedCoursesFragment
import com.kapilguru.trainer.student.homeActivity.studentStudyMaterialFragment.StudentStudyMaterialFragment
import com.kapilguru.trainer.student.homeActivity.trendingWebinars.StudentTrendingWebinars
import kotlinx.android.synthetic.main.fragment_student_dash_board.*
import kotlinx.android.synthetic.main.fragment_student_dash_board.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class StudentDashBoardFragment  : Fragment(), StudentDashBoardAdapter.OnItemClickedForHome,CardClickListener{

    lateinit var viewBinding: StudentFragmentDashBoardBinding
    lateinit var homeAdapter: StudentDashBoardAdapter
    lateinit var homeViewPagerAdapter: DashBoardViewPagerAdapter
    lateinit var homeScreenViewModel: StudentDashBoardViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var dialogFragmentCustom: DialogFragmentCustom
    var currentTabIndex: Int = 0
    var topHomePagerposition: Int = 0
    private val TAG = "DashBoardFragment"
    private var isPermisionAccepted: MutableLiveData<Boolean?> = MutableLiveData()
    val timer = Timer()

    companion object {
        const val KEY_SHOULD_SCROLL_DOWN = "SHOULD_SCROLL_DOWN"
        fun newInstance() = StudentDashBoardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.student_fragment_dash_board, container, false)
        homeScreenViewModel = ViewModelProvider(this, StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(StudentDashBoardViewModel::class.java)
        viewBinding.viewModel = homeScreenViewModel
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireActivity())
        dialogFragmentCustom = DialogFragmentCustom()
        setPopularCourses()
        setRecordedCourses()
        setStudyMaterials()
        setGalleryRecycler()
        setTestimonials()

        return viewBinding.root
    }
    
    private fun setPopularCourses() {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.popularCourses, PopularAndTrendingFragment.newInstance())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun setRecordedCourses() {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.recordedCourses, StudentRecordedCoursesFragment.newInstance())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun setStudyMaterials() {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.study_material_frame_layout, StudentStudyMaterialFragment.newInstance())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun setGalleryRecycler() {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.gallery_frame_layout, StudentGalleryFragment.newInstance())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun setTestimonials() {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.testimonials_frame_layout, StudentTestimonialsFragment.newInstance())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGetInTouch()
        homeAdapter = StudentDashBoardAdapter(this as StudentDashBoardAdapter.OnItemClickedForHome)
        view.recyclerViewHome.adapter = homeAdapter
        viewModelObserver()
        viewPageAdapterSetup(view)
        setClickListeners()
        checkAndShowLetsGetInTouch()
        shouldShowActionBarSearch(false)
//        ((activity) as StudentHomeActivity).fetchLatestNotification()
    }

    private fun setGetInTouch() {
        viewBinding.contactLayout.phoneNumber.setOnClickListener {
//            checkPhonePermission()
        }

        viewBinding.send.setOnClickListener {
            if (homeScreenViewModel.validateCreateLead()) {
                homeScreenViewModel.createLeadApi()
            }
        }



        homeScreenViewModel.createLeadError.observe(viewLifecycleOwner) { it ->
            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
        }

        homeScreenViewModel.commonResponse.observe(viewLifecycleOwner) { it ->
            // Clear all the fields
            homeScreenViewModel.createLeadRequest.value = CreateLeadRequest()

            when (it.status) {
                Status.ERROR -> {
                    showToast("Something Went Wrong Please, Try After Some Time")
                }
                Status.SUCCESS -> {
                    showToast("Thanks For approaching us, Our concern team will Contact you")
                }
                Status.LOADING -> {
//                    Do Nothing
                }
            }
        }

        isPermisionAccepted.observe(viewLifecycleOwner) { it ->
            it?.let { isAccepted ->
                if (isAccepted) {
                    contactPhoneIntent(this.requireContext(), getString(R.string.contactNumber))
                }
            }
        }

        viewBinding.contactLayout.contactMail.setOnClickListener {
            contactEmailIntent(this.requireContext(), getString(R.string.info_mail))
        }
    }

    private fun viewModelObserver() {
        homeScreenViewModel.setHomeItems()
        homeScreenViewModel.listOfHomeItems.observe(HomeScreenFragment@ this) {it ->
            homeAdapter.homeItems = it as ArrayList<StudentDashBoardItem>
        }

        homeScreenViewModel.listOfHomeTopItems.observe(HomeScreenFragment@ this) {
            homeViewPagerAdapter.setViewPagerData(homeViewPagerItems = it)
            setOnboadingIndicator()
            // set default Indicator
            setCurrentOnboardingIndicators(0)
        }
        
        homeScreenViewModel.setDashBoardTabsItems()
        homeScreenViewModel.listOfTabItems.observe(requireActivity()) {it->
            setTabUI(it)
        }
        
    }

    private fun viewPageAdapterSetup(view: View) {
        homeViewPagerAdapter = DashBoardViewPagerAdapter(this)
        view.homeViewPager2.adapter = homeViewPagerAdapter
        viewPagerObserver()
        registerOnPageChangeCallBack()
        topSliderTimer()
    }

    private fun setClickListeners() {
        btn_trending_view_all.setOnClickListener {
            when (currentTabIndex) {
                0 -> navigateToAllWebinars()
                1 -> navigateToAllDemos()
                2 -> navigateToAllJobOpenings()
            }
        }

        viewBinding.searchCourse.setOnClickListener {
//            startActivity(Intent(requireActivity(), SearchCourseActivity::class.java))
        }
    }

    private fun checkAndShowLetsGetInTouch() {
        var shouldScrollDown: Boolean? = false
        shouldScrollDown = arguments?.getBoolean(KEY_SHOULD_SCROLL_DOWN)
        if (shouldScrollDown != null && shouldScrollDown) {
            GlobalScope.launch {
                delay(500)
                scrollToLetsGetInTouch()
            }
        }
    }

    private fun shouldShowActionBarSearch(b: Boolean) {
//        ((activity) as StudentHomeActivity).shouldShowSearchInActionBar(shouldShowActionSearchBar)
    }


    private fun setOnboadingIndicator() {
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in 0 until homeViewPagerAdapter.itemCount) {
            val indicators = ImageView(this.requireContext())

            indicators.setImageDrawable(
                ContextCompat.getDrawable(
                    this.requireContext(), R.drawable.onboarding_indicator_active_two
                )
            )
            indicators.layoutParams = layoutParams
            viewBinding.layoutOnboardingIndicators.addView(indicators)
            viewBinding.layoutOnboardingIndicators.refreshDrawableState()
        }
    }


    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount = viewBinding.layoutOnboardingIndicators.childCount
        for (i in 0..childCount) {
            val imageView = viewBinding.layoutOnboardingIndicators.getChildAt(i)?.let {
                it as ImageView
            }
            if (i == index) {
                imageView?.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.onboarding_indicator_active))
            } else {
                imageView?.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.onboarding_indicator_active_two))
            }
        }
    }


    private fun setTabUI(dashBoardTabModel: MutableList<StudentDashBoardCustomTabModel>) {
        // add tab Listeners
        addTabSelectListener()
        // add tabs UI
        for (i in 0 until dashBoardTabModel.size) {
            val tab = viewBinding.tabLayout.newTab().setCustomView(setCustomTabView(dashBoardTabModel[i]))
            viewBinding.tabLayout.addTab(tab)
          /*  if (i == 0) {
                viewBinding.tabLayout.selectTab(tab)
            }*/
        }
        // Default Tab
        changeSelectedFragment(StudentTrendingWebinars.newInstance())
    }

    private fun showToast(message: String) {
        val toast = Toast(activity)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.setText(message)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }


    private fun viewPagerObserver() {
        homeScreenViewModel.setHomeTopItems()
        homeScreenViewModel.listOfHomeTopItems.observe(HomeScreenFragment@this, androidx.lifecycle.Observer {it->
            homeViewPagerAdapter.setViewPagerData(homeViewPagerItems = it)
        })
    }

    private fun registerOnPageChangeCallBack() {
        homeViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                topHomePagerposition = position
                setCurrentOnboardingIndicators(position)
            }
        })
    }


    private fun addTabSelectListener() {
        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab)
                currentTabIndex = tab!!.position
                when (currentTabIndex) {
                    0 -> changeSelectedFragment(StudentTrendingWebinars.newInstance())

//                    0 -> changeSelectedFragment(TrendingWebinars.newInstance())
//                    1 -> changeSelectedFragment(TrendingDemos.newInstance())
//                    2 -> changeSelectedFragment(TrendingJobOpenings.newInstance())
                }
            }

            private fun tabBackGroundColors(tab: TabLayout.Tab?) {
                val view = tab?.customView
//                val varView = view?.findViewById<CardView>(R.id.cardView)
//                view?.let {
//                    if (tab.isSelected) {
//                        varView!!.background.setTint(ContextCompat.getColor(view.context, R.color.gold))
//                    } else {
//                        varView!!.background.setTint(Color.WHITE)
//                    }
//                }
            }
        })
    }

    private fun topSliderTimer() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                Log.d(TAG, "run_AMI_runing")
                viewBinding.homeViewPager2.post(Runnable {
                    viewBinding.homeViewPager2.homeViewPager2.currentItem = topHomePagerposition % 3
                    topHomePagerposition++
                })
            }
        }, 0, 3000)
    }

    fun setCustomTabView(dashBoardTabModel: StudentDashBoardCustomTabModel): View {
        val view: View = LayoutInflater.from(this.requireActivity()).inflate(R.layout.dash_board_tab_ui, null)
        val header = view.findViewById<View>(R.id.tv_title) as TextView
        header.text = dashBoardTabModel.title
        val subTitle = view.findViewById<View>(R.id.tv_sub_title) as TextView
        subTitle.text = dashBoardTabModel.subTitle
        val imageView = view.findViewById<View>(R.id.side_icon) as ImageView
        imageView.setImageResource(dashBoardTabModel.image!!)
        return view
    }

    private fun changeSelectedFragment(fragment: Fragment) {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.tabFrameLayout, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }


    override fun onItemClick(position: Int) {
        when (position) {
            0 -> navigateToAllDemos()
            1 -> navigateToAllWebinars()
            2 -> onPopularTrendingClick()
         /*   3 -> (activity as StudentHomeActivity).navigateToClassRooms()
            4 -> (activity as HomeActivity).navigateToUpcoming()
            5 -> startActivity(Intent(activity, DemoLectureActivity::class.java))
            6 -> startActivity(Intent(activity, WebinarActivity::class.java))
            7 -> startActivity(Intent(activity, AllExamsListActivity::class.java))
            8 -> startActivity(Intent(activity, CertificateListActivity::class.java))
            9 -> navigateToAllJobOpenings()
            10 -> startActivity(Intent(activity, AnnouncementActivity::class.java))
            11 -> startActivity(Intent(activity, EarningsActivity::class.java))*/
        }
    }

    override fun onPopularTrendingClick() {
        /*startActivity(Intent(this.requireContext(), AllPopularTrendingCourses::class.java).apply {
            putParcelableArrayListExtra(PARAM_ALL_POPULAR_TRENDING_LIST, homeScreenViewModel.popularTrendingList)
        })*/
    }

    override fun onWebinarClick() {
//        navigateToAllWebinars()
    }

    override fun onDemoLectureClick() {
        /*navigateToAllDemos()*/
    }


    private fun navigateToAllJobOpenings() {
//        startActivity(Intent(requireContext(), JobOpeningsActivity::class.java))
    }

    private fun navigateToAllDemos() {
//        startActivity(Intent(requireContext(), AllTrendingDemosActivity::class.java))
    }

    private fun navigateToAllWebinars() {
//        startActivity(Intent(requireContext(), AllTrendingWebinars::class.java))
    }

    fun scrollToLetsGetInTouch() {
        viewBinding.scDashBoard.post(Runnable {
            viewBinding.scDashBoard.fullScroll(View.FOCUS_DOWN)
        })
    }
    
}