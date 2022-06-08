package com.kapilguru.trainer.allSubscription.packageSubscription.viewModel

import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.allSubscription.models.AllSubscriptionsData
import com.kapilguru.trainer.allSubscription.packageSubscription.PackageSubscriptionRepository

class PackageSubscriptionViewModel(packageSubscriptionRepository: PackageSubscriptionRepository) : ViewModel(){
    lateinit var selectedPackage : AllSubscriptionsData
    var descriptionList = ArrayList<String>()


}