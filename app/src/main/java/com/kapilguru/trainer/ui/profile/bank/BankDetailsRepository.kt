package com.kapilguru.trainer.ui.profile.bank

import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.ui.profile.bank.data.BankDetailsUploadRequest

class BankDetailsRepository(private val apiHelper: ApiHelper) {
    suspend fun getBankDetails(userId : String) = apiHelper.getBankDetails(userId)
    suspend fun saveBakDetails(bankDetailsUploadRequest: BankDetailsUploadRequest) = apiHelper.saveBankDetails(bankDetailsUploadRequest)
    suspend fun updateBakDetails(bankId : String, bankDetailsUploadRequest: BankDetailsUploadRequest) = apiHelper.updateBankDetails(bankId,bankDetailsUploadRequest)
}