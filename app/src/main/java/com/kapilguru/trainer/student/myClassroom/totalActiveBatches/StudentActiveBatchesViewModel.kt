package com.kapilguru.trainer.student.myClassroom.totalActiveBatches

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.student.myClassroom.liveClasses.model.StudentActiveBatchData

class StudentActiveBatchesViewModel : ViewModel() {
    private val TAG = "ActiveBatchesViewModel"

    /*batch lit and courseName are stroed in viewModel instead of using saved instances.*/
    var batchList = ArrayList<StudentActiveBatchData>()
    var courseName = ""
}