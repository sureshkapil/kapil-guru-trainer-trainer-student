package com.kapilguru.trainer.ui.reports.batch.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.reports.ReportsRepository
import com.kapilguru.trainer.ui.reports.batch.BatchReportRepository
import com.kapilguru.trainer.ui.reports.viewModel.ReportsViewModel

class BatchReportViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BatchReportViewModel::class.java)) {
            return BatchReportViewModel(
                BatchReportRepository(apiHelper),
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}