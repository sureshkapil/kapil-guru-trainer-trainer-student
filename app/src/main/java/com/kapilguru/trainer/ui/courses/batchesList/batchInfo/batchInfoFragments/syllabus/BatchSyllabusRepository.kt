package com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus

import com.kapilguru.trainer.ApiHelper

class BatchSyllabusRepository(val apiHelper: ApiHelper) {

    suspend fun getSyllabusInfo(courseId: String) = apiHelper.getBatchSyllabus(courseId)
}