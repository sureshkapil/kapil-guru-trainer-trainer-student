package com.kapilguru.trainer.exams.createQuestion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.TextFormatEditorBinding

class TextFormat(val mcontext: Context, val root: ViewGroup) : View(mcontext) {
    private val TAG = "TextFormat"
    var isBoldSelected: Boolean = false
    var isItalicSelected: Boolean = false
    var isUnderLineSelected: Boolean = false
    var isBulletsSelected: Boolean = false
    var isNumberSelected: Boolean = false
    var binding: TextFormatEditorBinding

    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mcontext), R.layout.text_format_editor, root, false)
        root.addView(binding.root)
        setClickListeners()
    }

    fun setHeader(name : String,color : Int){
        binding.actvHeader.setText(name)
        binding.actvHeader.setTextColor(color)
    }

    fun getText() : String{
        return if(!binding.richEditor.html.isNullOrBlank()) {
            binding.richEditor.html
        } else {
            ""
        }
    }

    private fun setClickListeners() {
        setBoldClickListener()
        setItalicClickListener()
        setUnderClickListener()
        setBulletClickListener()
        setNumberClickListener()
    }

    private fun setBoldClickListener() {
        binding.actionBold.setOnClickListener {
            binding.richEditor.setBold()
            if (isBoldSelected) {
                isBoldSelected = false
                binding.actionBold.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
            } else {
                isBoldSelected = true
                binding.actionBold.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_active))
            }
        }

    }

    private fun setItalicClickListener() {
        binding.actionItalic.setOnClickListener {
            binding.richEditor.setItalic()
            if (isItalicSelected) {
                isItalicSelected = false
                binding.actionItalic.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
            } else {
                isItalicSelected = true
                binding.actionItalic.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_active))
            }
        }

    }

    private fun setUnderClickListener() {
        binding.actionUnderline.setOnClickListener {
            binding.richEditor.setUnderline()
            if (isUnderLineSelected) {
                isUnderLineSelected = false
                binding.actionUnderline.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
            } else {
                isUnderLineSelected = true
                binding.actionUnderline.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_active))
            }
        }
    }

    private fun setBulletClickListener() {
        binding.actionInsertBullets.setOnClickListener {
            binding.richEditor.setBullets()
            if (isBulletsSelected) {
                isBulletsSelected = false
                binding.actionInsertBullets.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
            } else {
                isBulletsSelected = true
                binding.actionInsertBullets.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_active))
            }
        }
    }

    private fun setNumberClickListener() {
        binding.actionInsertNumbers.setOnClickListener {
            binding.richEditor.setNumbers()
            if (isNumberSelected) {
                isNumberSelected = false
                binding.actionInsertNumbers.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_in_active))
//            richEditor.removeFormat()
            } else {
                isNumberSelected = true
                binding.actionInsertNumbers.setColorFilter(ContextCompat.getColor(mcontext, R.color.rich_txt_editor_active))
            }
        }
    }

}