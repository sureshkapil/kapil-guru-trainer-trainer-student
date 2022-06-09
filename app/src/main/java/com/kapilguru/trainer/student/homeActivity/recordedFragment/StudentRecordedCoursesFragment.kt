package com.kapilguru.trainer.student.homeActivity.recordedFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.trainer.student.homeActivity.popularAndTrending.PopularAndTrendingAdapter
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModel
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModelFactory
import com.kapilguru.trainer.student.homeActivity.models.PopularAndTrendingApi
import kotlinx.android.synthetic.main.student_fragment_recorded_courses.view.*


class StudentRecordedCoursesFragment : Fragment(), PopularAndTrendingAdapter.CardItem, StudentRecordedCoursesAdapter.CardItem {
    lateinit var  viewModel: StudentDashBoardViewModel
    lateinit var adapter: StudentRecordedCoursesAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.student_fragment_recorded_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        viewModel = ViewModelProvider(this.requireParentFragment(), StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(StudentDashBoardViewModel::class.java)
        setUpRecycler(view)
        viewModelObserver()
        clickListerens()
    }

    private fun clickListerens() {
        /*view_all.setOnClickListener {
            startActivity(Intent(this.requireContext(), AllPopularTrendingCourses::class.java).apply {
                putParcelableArrayListExtra(PARAM_ALL_POPULAR_TRENDING_LIST,viewModel.popularTrendingList)
            })
        }*/
    }

    private fun viewModelObserver() {
        viewModel.fetchAllPopularAndTrending()

        viewModel.popularAndTrendingResponse.observe(requireActivity(), Observer { response->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.popularAndTrendingApi?.let { it ->
                        adapter.listItem = it
                        viewModel.popularTrendingList = it as ArrayList<PopularAndTrendingApi>
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setUpRecycler(view: View) {
        adapter = StudentRecordedCoursesAdapter(this,false)
        view.recordedRecy.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.recordedRecy.adapter  = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = StudentRecordedCoursesFragment()
    }

    override fun onCardClick(popularAndTrendingApi: PopularAndTrendingApi) {
        /*startActivity(Intent(requireContext(), CourseDetailsActivity::class.java).apply {
            putExtra(PARAM_COURSE_ID,popularAndTrendingApi.id.toString())
        })*/
    }
}