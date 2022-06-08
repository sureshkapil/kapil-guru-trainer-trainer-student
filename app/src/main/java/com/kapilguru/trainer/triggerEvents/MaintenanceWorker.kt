package com.kapilguru.trainer.triggerEvents

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.MyApplication
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.preferences.StorePreferences
import com.kapilguru.trainer.ui.home.UpComingScheduleResponse
import kotlinx.coroutines.*

class MaintenanceWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    val TAG = "MaintainanceWorker"
    var abc = 1

    override suspend fun doWork(): Result {
        fetchUpComingClasses()
        return Result.success()
    }

    private suspend fun fetchUpComingClasses()  {
        val apiUpcomingScheduleResponse: Deferred<UpComingScheduleResponse> = GlobalScope.async {
            val trainerId = StorePreferences(appContext).userId
            val apiHelper = ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)
            apiHelper.fetchUpcomingSchedule(trainerId.toString())
        }
        val abc = apiUpcomingScheduleResponse.await()
        abc.data?.let {(appContext.applicationContext as MyApplication).getPendingIntent(it)}
    }

}