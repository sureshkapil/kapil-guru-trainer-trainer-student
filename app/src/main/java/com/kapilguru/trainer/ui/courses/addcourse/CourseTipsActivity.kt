package com.kapilguru.trainer.ui.courses.addcourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kapilguru.trainer.R
import kotlinx.android.synthetic.main.activity_course_tips.*

class CourseTipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_tips)
        nextbutton.setOnClickListener {
            startActivity(Intent(this, AddCourseActivity::class.java))
        }
    }
}