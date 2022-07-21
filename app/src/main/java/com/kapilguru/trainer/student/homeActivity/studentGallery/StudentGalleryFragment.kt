package com.kapilguru.trainer.student.homeActivity.studentGallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.databinding.StudentFragmentGalleryBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModel
import com.kapilguru.trainer.student.homeActivity.dashboard.StudentDashBoardViewModelFactory
import com.kapilguru.trainer.student.homeActivity.models.PopularAndTrendingApi
import com.kapilguru.trainer.student.homeActivity.studentGallery.model.ImageResData
import kotlinx.android.synthetic.main.student_fragment_gallery.view.*

class StudentGalleryFragment : Fragment(), StudentGalleryAdapter.CardItemClickListener {
    private val TAG = "StudentGalleryFragment"
    private lateinit var binding: StudentFragmentGalleryBinding
    lateinit var viewModel: StudentDashBoardViewModel
    lateinit var adapter: StudentGalleryAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = StudentFragmentGalleryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        viewModel = ViewModelProvider(this.requireParentFragment(), StudentDashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application)).get(
            StudentDashBoardViewModel::class.java
        )
        setClickListeners()
        viewModelObserver()
        fetchImages()
    }

    private fun setClickListeners() {
        binding.viewAll.setOnClickListener {
            StudentGalleryActivity.launchActivity(requireActivity())
        }
    }

    private fun setUpRecycler(imageList : ArrayList<ImageResData>) {
        if(this::adapter.isInitialized){
            //do nothing
        }else{
            adapter = StudentGalleryAdapter(this,false)
            binding.galleryRecy.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.galleryRecy.adapter = adapter
        }
        adapter.imageList = imageList
    }

    private fun viewModelObserver() {
        viewModel.imagesListApiRes.observe(requireActivity(), Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.imageList?.let { it ->
                        setUpRecycler(it)
                        viewModel.popularTrendingList = it as ArrayList<PopularAndTrendingApi>
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun fetchImages() {
        viewModel.getImages()
    }

    override fun onCardClicked(popularAndTrendingApi: PopularAndTrendingApi) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = StudentGalleryFragment()
    }
}