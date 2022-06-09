package com.kapilguru.trainer.student.homeActivity.trendingWebinars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModel
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModelFactory
import com.kapilguru.trainer.student.homeActivity.models.AllWebinarsApi
import kotlinx.android.synthetic.main.student_fragment_trending_webinars.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [StudentTrendingWebinars.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentTrendingWebinars : Fragment(), StudentTrendingWebinarAdapter.TrendingWebinarCardClick {

    lateinit var  viewModel: StudentDashBoardViewModel
    lateinit var studentTrendingWebinarAdapter: StudentTrendingWebinarAdapter
    lateinit var dialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.student_fragment_trending_webinars, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        studentTrendingWebinarAdapter = StudentTrendingWebinarAdapter(this,false)

        viewModel = ViewModelProvider(this.requireParentFragment(), StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(StudentDashBoardViewModel::class.java)
//        viewModel = ViewModelProviders.of(parentFragment!!).get(DashBoardViewModel::class.java)


        setUPRecycler(view)
        viewModelObserver()
    }

    private fun setUPRecycler(view: View) {
        view.trendingWebinarsRecy.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.trendingWebinarsRecy.adapter  = studentTrendingWebinarAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = StudentTrendingWebinars()
    }


   fun viewModelObserver() {

       viewModel.fetchAllWebinars()

       viewModel.allWebinarsData.observe(requireActivity(), Observer { response->
           when (response.status) {

               Status.LOADING -> {
                   dialog.showLoadingDialog()
               }

               Status.SUCCESS -> {
                   response.data?.data?.let { upComingSchedule ->
                       studentTrendingWebinarAdapter.upComingScheduleApiList = upComingSchedule
                       viewModel.trendingWebinarsList = upComingSchedule as ArrayList<AllWebinarsApi>
                   }
                   dialog.dismissLoadingDialog()
               }

               Status.ERROR -> {
                   dialog.dismissLoadingDialog()
               }
           }
       })

   }

    override fun onTrendingWebinarCardClick(webinarDetails: AllWebinarsApi) {
//        webinarDetails.id?.let { id->
//            WebinarDetailsActivity.launchActivity(requireActivity(), id)
//        }
    }

    override fun onShareClick(webinarDetails: AllWebinarsApi) {
//        shareIntent(BuildConfig.SHARE_URL + WEBINAR_DETAILS + webinarDetails.code, requireContext())
    }


}