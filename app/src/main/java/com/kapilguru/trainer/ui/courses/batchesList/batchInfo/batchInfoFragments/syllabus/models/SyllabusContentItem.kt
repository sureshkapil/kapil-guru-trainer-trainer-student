package com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.models

import android.os.Build
import android.text.Html

import android.text.Spanned




data class SyllabusContentItem(
    val id: Int,
    val syllabusHeading: String,
    val syllabusSubHeading: String
) {

    fun getHtmlText(): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this.syllabusSubHeading, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(this.syllabusHeading);
        }
    }
}