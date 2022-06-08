package com.kapilguru.trainer.exams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.BaseActivity
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityExamsBinding
import com.kapilguru.trainer.exams.conductExams.CourseListExamFragment
import com.kapilguru.trainer.exams.viewModel.ExamsViewModel
import com.kapilguru.trainer.exams.viewModel.ExamsViewModelFactory
import com.kapilguru.trainer.network.RetrofitNetwork

class ExamsActivity : BaseActivity() {
    private val TAG = "ExamsActivity"
    lateinit var binding: ActivityExamsBinding
    lateinit var viewmodel: ExamsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exams)
        binding.lifecycleOwner = this
        viewmodel = ViewModelProvider(this, ExamsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(ExamsViewModel::class.java)
        getIntentData()
        setCustomActionBarListener()
        showCourseListExamFragment()
    }

    private fun getIntentData(){
        if(intent.hasExtra(KEY_COURSE_ID)){
            val courseId = intent.getIntExtra(KEY_COURSE_ID,viewmodel.emptyCourseId)
            viewmodel.courseIdFromClassRoom = courseId
        }
    }

    private fun showCourseListExamFragment(){
        supportFragmentManager.beginTransaction().add(R.id.fl_conduct_exams,CourseListExamFragment()).commit()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.exams_schedule))
    }

    companion object{
        const val KEY_COURSE_ID = "COURSE_ID_KEY"
        fun launchActivity(activity  : Activity, courseId : Int){
            val intent = Intent(activity,ExamsActivity::class.java)
            intent.putExtra(KEY_COURSE_ID,courseId)
            activity.startActivity(intent)
        }
    }
}