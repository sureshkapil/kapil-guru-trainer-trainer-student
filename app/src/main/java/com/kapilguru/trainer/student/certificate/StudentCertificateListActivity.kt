package com.kapilguru.trainer.student.certificate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.ActivityStudentCertificateListBinding

class StudentCertificateListActivity : AppCompatActivity() {
    private val TAG = "CertificateListActivity"
    lateinit var binding: ActivityStudentCertificateListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActivityName()
        setClickListeners()
        setEmptyCertificateText()
    }

    private fun setEmptyCertificateText() {
        binding.noDataAvailable.tvNoData.text = "No Certificate Available"
        binding.noDataAvailable.btnEnrollNow.text = "Enroll For a Course"
        binding.noDataAvailable.btnEnrollNow.setOnClickListener(View.OnClickListener { navigateToCourseSearch() })
    }

    private fun navigateToCourseSearch() {
//        SearchCourseActivity.launchActivity(this)
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_certificate_list)
    }

    private fun setActivityName() {
        binding.actionbar.actvActivityName.text = getString(R.string.certificates)
    }

    private fun setClickListeners() {
        binding.actionbar.acivBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun launchActivity(activity: Activity) {
            val intent = Intent(activity, StudentCertificateListActivity::class.java)
            activity.startActivity(intent)
        }
    }
}