package com.kapilguru.trainer.ui.courses.batchesList.batchDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kapilguru.trainer.R
import com.kapilguru.trainer.ui.courses.batchesList.batchStudents.BatchStudentsListActivity
import kotlinx.android.synthetic.main.activity_batch_details.*

class BatchDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_details)

        lLayoutStudents.setOnClickListener {
            val intent = Intent(this, BatchStudentsListActivity::class.java)
            startActivity(intent)
        }
    }
}