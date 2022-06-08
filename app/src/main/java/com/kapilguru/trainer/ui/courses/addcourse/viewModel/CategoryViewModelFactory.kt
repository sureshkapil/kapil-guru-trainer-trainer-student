package com.kapilguru.trainer.ui.courses.addcourse.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.courses.addcourse.CategoryRepository


class CategoryViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(
                    CategoryRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}