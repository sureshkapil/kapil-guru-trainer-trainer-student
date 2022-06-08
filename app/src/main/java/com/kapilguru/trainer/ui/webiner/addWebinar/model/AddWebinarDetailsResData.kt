package com.kapilguru.trainer.ui.webiner.addWebinar.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.trainer.ui.webiner.model.ActiveWebinarData

data class AddWebinarDetailsResData(
    @SerializedName("insertId") val insertId: Int = 0
) : ActiveWebinarData()
