package com.kapilguru.trainer.student

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityLiveCourseListBinding
import com.kapilguru.trainer.student.homeActivity.liveCourses.LiveCoursesFragment

/*This activity is launched to show list of live Courses, recorded courses, Study materials*/
class LiveCourseListActivity : StudentBaseActivity() {
    private val TAG = "LiveCourseListActivity"
    private lateinit var binding: ActivityLiveCourseListBinding
    private var mListType = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_course_list)
        getIntentData()
        setCustomActionBar()
        setFragment()
    }

    private fun setCustomActionBar() {
        var activityName = ""
        when (mListType) {
            LIVE_COURSE -> {
                activityName = getString(R.string.live_courses)
            }
            RECORDED_COURSE -> {
                activityName = getString(R.string.recorded_courses)
            }
            STUDY_MATERIAL -> {
                activityName = getString(R.string.study_materials)
            }
        }
        setActionbarBackListener(this, binding.customActionBar.root, activityName)
    }

    private fun getIntentData() {
        mListType = intent.getStringExtra(LIST_TYPE) ?: ""
    }

    private fun setFragment() {
        when (mListType) {
            LIVE_COURSE -> {
                showFragment(LiveCoursesFragment.newInstance(LiveCoursesFragment.LIVE_COURSE,false))
            }
            RECORDED_COURSE -> {
                showFragment(LiveCoursesFragment.newInstance(LiveCoursesFragment.RECORDED_COURSE,false))
            }
            STUDY_MATERIAL -> {
                showFragment(LiveCoursesFragment.newInstance(LiveCoursesFragment.STUDY_MATERIAL,false))
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().show(fragment).commit()
        supportFragmentManager.beginTransaction().replace(R.id.fl_live_course,fragment).commit()
    }

    companion object {
        const val LIST_TYPE = "list_type"
        const val LIVE_COURSE = "live_course"
        const val RECORDED_COURSE = "recorded_course"
        const val STUDY_MATERIAL = "study_material"
        fun launchActivity(activity: Activity, listType: String) {
            val intent = Intent(activity, LiveCourseListActivity::class.java)
            intent.putExtra(LIST_TYPE, listType)
            activity.startActivity(intent)
        }
    }
}