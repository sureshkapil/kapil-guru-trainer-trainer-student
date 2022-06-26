package com.kapilguru.trainer.trainerGallery

import com.google.gson.annotations.SerializedName

data class DeleteImageResponse(@SerializedName("message")
                              var message: String? = "",
                               @SerializedName("status")
                              var status: Int? = 0)