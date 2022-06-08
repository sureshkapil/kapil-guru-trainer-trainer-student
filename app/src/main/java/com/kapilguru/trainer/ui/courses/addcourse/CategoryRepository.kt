package com.kapilguru.trainer.ui.courses.addcourse

import com.kapilguru.trainer.ApiHelper

class CategoryRepository(private val apiHelper: ApiHelper) {

    suspend fun getCategory()=apiHelper.getCategory()
}