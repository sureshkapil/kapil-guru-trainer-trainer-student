package com.kapilguru.trainer.student.homeActivity.studentTestimonials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModel
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModelFactory
import kotlinx.android.synthetic.main.fragment_popular_and_trending.*
import kotlinx.android.synthetic.main.student_fragment_testimonials.view.*


class StudentTestimonialsFragment : Fragment(), StudentTestimonialPagerAdapter.CardClickListener {
    lateinit var viewModel: StudentDashBoardViewModel
    lateinit var adapter: StudentTestimonialPagerAdapter
    lateinit var dialog: CustomProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.student_fragment_testimonials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        viewModel = ViewModelProvider(this.requireParentFragment(), StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application)).get(
                StudentDashBoardViewModel::class.java
            )
        viewPageAdapterSetup(view)
        viewModelObserver()
        clickListerens()
    }

    private fun clickListerens() {
        view_all.setOnClickListener {
            /*  startActivity(Intent(this.requireContext(), AllPopularTrendingCourses::class.java).apply {
                  putParcelableArrayListExtra(PARAM_ALL_POPULAR_TRENDING_LIST,viewModel.popularTrendingList)
              })*/
        }
    }

    private fun viewModelObserver() {
        viewModel.fetchAllPopularAndTrending()
    }

    private fun viewPageAdapterSetup(view: View) {
        adapter = StudentTestimonialPagerAdapter(this)
        view.view_pager.adapter = adapter
        viewPagerObserver()
//            registerOnPageChangeCallBack()
//            topSliderTimer()
    }

    private fun viewPagerObserver() {
//        homeScreenViewModel.setHomeTopItems()
//        homeScreenViewModel.listOfHomeTopItems.observe(HomeScreenFragment@ this, Observer {
//            homeViewPagerAdapter.setViewPagerData(homeViewPagerItems = it)
//        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = StudentTestimonialsFragment()
    }

    override fun onCardClick() {
        //
    }

}