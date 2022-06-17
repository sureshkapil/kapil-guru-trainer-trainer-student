package com.kapilguru.trainer.ui.courses.tax

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.addcourse.AddCourseRepository

class TaxCalculationFragmentViewModelFactory(
    private val apiHelper: ApiHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaxCalculationFragmentViewModel::class.java)) {
            return TaxCalculationFragmentViewModel(
                    AddCourseRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}