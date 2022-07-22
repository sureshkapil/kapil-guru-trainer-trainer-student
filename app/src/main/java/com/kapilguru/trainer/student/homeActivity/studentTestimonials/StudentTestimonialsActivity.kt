package com.kapilguru.trainer.student.homeActivity.studentTestimonials

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityStudentTestimonialsBinding
import com.kapilguru.trainer.student.StudentBaseActivity

class StudentTestimonialsActivity : StudentBaseActivity() {
    private val TAG = "StudentGalleryActivity"
    private lateinit var binding: ActivityStudentTestimonialsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_testimonials)
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.testimonials))
        showFragment(StudentTestimonialsFragment.newInstance(false))
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fl_testimonials, fragment).commit()
    }

    companion object {
        fun launchActivity(activity: Activity) {
            activity.startActivity(Intent(activity, StudentTestimonialsActivity::class.java))
        }
    }
}