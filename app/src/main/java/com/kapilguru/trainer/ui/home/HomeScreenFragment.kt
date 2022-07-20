package com.kapilguru.trainer.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.kapilguru.trainer.*
import com.kapilguru.trainer.addStudent.AllMyStudents
import com.kapilguru.trainer.announcement.AnnouncementActivity
import com.kapilguru.trainer.databinding.FragmentHomeScreenBinding
import com.kapilguru.trainer.enquiries.EnquiriesActivity
import com.kapilguru.trainer.exams.ExamsActivity
import com.kapilguru.trainer.feeManagement.FeeManagement
import com.kapilguru.trainer.myClassRoomDetails.MyClassDetails
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.studyMaterial.StudyMaterialActivity
import com.kapilguru.trainer.testimonials.TestimonialsActivity
import com.kapilguru.trainer.todaysSchedule.TodaysScheduele
import com.kapilguru.trainer.trainerFeatures.TrainerFeaturesFragment
import com.kapilguru.trainer.trainerGallery.TrainerAllGalleryPicksActivity
import com.kapilguru.trainer.ui.courses.courses_list.CourseActivity
import com.kapilguru.trainer.ui.guestLectures.GuestLecturesNewActivity
import com.kapilguru.trainer.ui.webiner.WebinarNewActivity
import com.kapilguru.trainer.ui.webiner.webinarDetailsActivity.WebinarDetailsActivity
import kotlinx.android.synthetic.main.fragment_home_screen.*
import kotlinx.android.synthetic.main.fragment_home_screen.view.*


class HomeScreenFragment : Fragment(), HomeAdapter.OnItemClickedForHome, TodayScheduleAdapter.OnItemClick, HomeViewPagerAdapter.CardClickListener {

    lateinit var homeViewBinding: FragmentHomeScreenBinding
    lateinit var homeAdapter: HomeAdapter
    lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    lateinit var todayScheduleAdapter: TodayScheduleAdapter
    lateinit var homeScreenViewModel: HomeScreenViewModel
    lateinit var progressDialog: CustomProgressDialog
    var viewPagerPosition: Int = 0

    companion object {
        fun newInstance() = HomeScreenFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        homeScreenViewModel =
            ViewModelProvider(this, HomeScreenViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application)).get(HomeScreenViewModel::class.java)
        homeViewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())
        return homeViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTodaySchedule()
        // gridlayout is set
        setGridlayout(view)
        // fetching grid layout data
        viewModelObserver()
        // set Schedule Page adapter
        setScheduleViewPager(view)
        // fetching Schedule Page adapter data
        viewPagerObserver()
        // call register for Page adapter
        registerOnPageChangeCallBack()
        // set features fragment
        setFeaturesFragment()

        setAndClickListenersExploreGallery()

        setAndClickListenersExploreTestimonials()

        todaysFollowUpFeeClickListener()

        todaysFollowUpEnquiriesClickListener()

    }

    private fun setAndClickListenersExploreGallery() {
        homeViewBinding.exploreGallery.setOnClickListener {
            startActivity(Intent(this.context,TrainerAllGalleryPicksActivity::class.java))
        }
    }

    private fun setAndClickListenersExploreTestimonials() {
        homeViewBinding.exploreTestimonials.setOnClickListener {
            startActivity(Intent(this.context,TestimonialsActivity::class.java))
        }
    }

    private fun todaysFollowUpFeeClickListener() {
        homeViewBinding.feeRemainderLayout.setOnClickListener {
            startActivity(Intent(this.context,FeeManagement::class.java).putExtra(PARAM_IS_FROM_TODAYS_FOLLOWUP,true))
        }
    }

    private fun todaysFollowUpEnquiriesClickListener() {
        homeViewBinding.enquiryRemainderLayout.setOnClickListener {
            startActivity(Intent(this.context,EnquiriesActivity::class.java).putExtra(PARAM_IS_FROM_TODAYS_FOLLOWUP,true))
        }
    }

    private fun setTodaySchedule() {
        homeScreenViewModel.fetchUpcomingSchedule()
        todayScheduleAdapter = TodayScheduleAdapter(this)
    }

    private fun setScheduleViewPager(view: View) {
        homeViewPagerAdapter = HomeViewPagerAdapter(this)
        view.homeViewPager2.adapter = homeViewPagerAdapter
    }

    private fun setGridlayout(view: View) {
        val spanCount = 3 // 3 columns
        val spacing = 10 // 50px
        val includeEdge = false
        homeViewBinding.recyclerViewHome.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        homeViewBinding.recyclerViewHome.layoutManager = GridLayoutManager(context, 3)
        homeViewBinding.recyclerViewHome.isNestedScrollingEnabled = false
        homeAdapter = HomeAdapter(this as HomeAdapter.OnItemClickedForHome)


        view.recyclerViewHome.adapter = homeAdapter
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
            homeViewBinding.layoutOnboardingIndicators.addView(indicators)
            homeViewBinding.layoutOnboardingIndicators.refreshDrawableState()
        }
    }

    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount = homeViewBinding.layoutOnboardingIndicators.childCount
        for (i in 0..childCount) {
            val imageView = homeViewBinding.layoutOnboardingIndicators.getChildAt(i)?.let {
                it as ImageView
            }
            if (i == index) {
                imageView?.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.onboarding_indicator_active))
            } else {
                imageView?.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.onboarding_indicator_active_two))
            }
        }
    }

    private fun registerOnPageChangeCallBack() {
        homeViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewPagerPosition=0
                setCurrentOnboardingIndicators(position)
            }
        })
    }

    private fun navigateToAddCourse() {
        startActivity(Intent(this.requireContext(),CourseActivity::class.java))
    }

    private fun setFeaturesFragment() {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.feature_frame_layout, TrainerFeaturesFragment.newInstance())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun viewModelObserver() {
        homeScreenViewModel.setHomeItems()
        homeScreenViewModel.listOfHomeItems.observe(HomeScreenFragment@ this, Observer {
            homeAdapter.setData(homeItems = it)
        })
    }

    private fun viewPagerObserver() {
        homeScreenViewModel.setHomeTopItems()
        homeScreenViewModel.listOfHomeTopItems.observe(HomeScreenFragment@ this, Observer {
            homeViewPagerAdapter.setViewPagerData(homeViewPagerItems = it)
            // set Indicator
            setOnboadingIndicator()
            // set default Indicator
            setCurrentOnboardingIndicators(0)
        })
    }

    override fun onItemClick(position: Int) {
        when (position) {
            0 -> startActivity(Intent(activity, CourseActivity::class.java))
//            0 ->   VideoCallInterfaceImplementation.launchVideoCall(requireContext(),  "1640164942153bt16941",
//                "PartiTrainerName", "hostTrainerName")
            1 -> navigateToStudyMaterial(false) // done
            2 -> navigateToStudyMaterial(true) // done

            3 -> startActivity(Intent(activity, GuestLecturesNewActivity::class.java)) // done


            4 -> startActivity(Intent(activity, TodaysScheduele::class.java)) // done

            5 -> startActivity(Intent(activity, ExamsActivity::class.java)) // done

            6 -> startActivity(Intent(activity, EnquiriesActivity::class.java)) // enquiries ??

//            7 -> startActivity(Intent(activity, StudentList::class.java)) // pending
            7 -> startActivity(Intent(activity, AllMyStudents::class.java)) // pending

            8 -> startActivity(Intent(activity, AnnouncementActivity::class.java)) // done
        }
    }

    private fun navigateToStudyMaterial(isStudyMateria: Boolean) {
        startActivity(Intent(activity, StudyMaterialActivity::class.java).putExtra(PARAM_IS_FROM_DASHBOARD_AS_STUDY_MATERIAL, isStudyMateria))
    }

    override fun onCardClick(upComingScheduleApi: UpComingScheduleApi) {
        when (upComingScheduleApi.activityType!!.toLowerCase()) {
            WEBINAR -> {
                val intent = Intent(activity, WebinarDetailsActivity::class.java)
                intent.putExtra("webinarData", upComingScheduleApi.activityId.toString())
                startActivity(intent)
            }
            LECTURE -> {
                startActivity(Intent(activity, WebinarNewActivity::class.java))
            }
            COURSE -> {
                MyClassDetails.launchActivity(upComingScheduleApi.activityId.toString(), requireActivity(), 0)
            }
        }
    }

    override fun onCourseClicked() {
        navigateToAddCourse()
    }

    override fun onWebinarClicked() {
        navigateToStudyMaterial(false)
    }

    override fun onGuestLectureClicked() {
        navigateToStudyMaterial(true)
    }
}