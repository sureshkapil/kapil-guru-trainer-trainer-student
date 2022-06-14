package com.kapilguru.trainer.trainerGallery

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentAddImageBinding
import com.kapilguru.trainer.databinding.FragmentAddImageBindingImpl
import com.kapilguru.trainer.databinding.StudentAllTrendingWebinarsItemViewBinding
import com.kapilguru.trainer.ui.courses.addcourse.viewModel.AddCourseViewModel
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [AddImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddImageFragment : Fragment() {

    lateinit var binding: FragmentAddImageBinding
//    lateinit var viewModel: ViewModel
     val viewModel : AddCourseViewModel by activityViewModels()
//    val viewModel: AddCourseViewModel by activityViewModels()
    lateinit var mContext: Context
    lateinit var appUri: Uri
    var imageFile: File? = null
    private lateinit var croppedImageUri : Uri
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private val imageRequest = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        cropActivityResultLauncher.launch(null)
    }
    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        appUri = it
        cropActivityResultLauncher.launch(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_image, container, false)



        binding.lifecycleOwner = this

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddImageFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadImageCard.setOnClickListener {

        }


    }

}