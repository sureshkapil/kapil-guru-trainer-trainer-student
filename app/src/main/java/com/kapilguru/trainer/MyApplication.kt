package com.kapilguru.trainer

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.triggerEvents.MaintenanceWorker
import com.kapilguru.trainer.triggerEvents.MyAlarmReceiver
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.AddGuestLectureRepository
import com.kapilguru.trainer.ui.guestLectures.addGuestLecture.data.LanguageData
import com.kapilguru.trainer.ui.home.UpComingScheduleApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.VideomeetSDK
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import com.kapilguru.trainer.preferences.StorePreferences

class MyApplication : Application() {
    var mLanguagesList = ArrayList<LanguageData>()

    override fun onCreate() {
        super.onCreate()
        context = this
        initVideoMeet()
    }

    //init videoMeet
    private fun initVideoMeet(){
        try{
            VideomeetSDK.initSDK(VideomeetSDK.Configuration.Builder(this).setLicenseKey("kpwi81adun").setHostName("44.197.234.100").build())
        }catch (exception : RuntimeException){
            exception.printStackTrace()
        }
    }

    fun getLanguages(languagesMutableLiveData: MutableLiveData<ArrayList<LanguageData>>) {
        if (mLanguagesList.isEmpty()) {
            fetchLanguages(languagesMutableLiveData)
        } else {
            languagesMutableLiveData.postValue(mLanguagesList)
        }
    }

    private fun fetchLanguages(languagesMutableLiveData: MutableLiveData<ArrayList<LanguageData>>): ArrayList<LanguageData> {
        val addGuestLectureRepository = AddGuestLectureRepository(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        GlobalScope.launch {
            try {
                mLanguagesList = addGuestLectureRepository.getCourseLanguage().languageData
                languagesMutableLiveData.postValue(mLanguagesList)
            } catch (exception: HttpException) {

            }
        }
        return mLanguagesList
    }

    fun initMaintenanceWorker() {
        if (StorePreferences(this).userId == 0) {
            return
        }
        val workRequest = PeriodicWorkRequestBuilder<MaintenanceWorker>(15, TimeUnit.MINUTES).setInitialDelay(15, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Maintenance", ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    fun getPendingIntent(upComingSchedule: List<UpComingScheduleApi>) {
        getUpComingScheduleInOneHour(upComingSchedule)
    }

    private fun getUpComingScheduleInOneHour(upComingSchedule: List<UpComingScheduleApi>) {
        for(schedule in upComingSchedule) {
            if(schedule.isAboutToLiveInOneHour()) {
                setUpcomingAlarm(schedule)
            }
        }
    }

    private fun setUpcomingAlarm(schedule: UpComingScheduleApi) {
        val notifyIntent = Intent(this, MyAlarmReceiver::class.java)
        notifyIntent.putExtra(PARAM_TODAYS_SCHEDULE_NOTIFICATION,schedule)
        sendBroadcast(notifyIntent)
        val pendingIntent = PendingIntent.getBroadcast(context, 50, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val timeInTimeMillis = schedule.startTime!!.getEpochTime()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInTimeMillis, pendingIntent)
    }

    companion object {
        lateinit var context: Context
    }
}