package com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentBatchSyllabusBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.adapters.BatchSyllabusRecyclerAdapter
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.viewModel.BatchSyllabusViewModel
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.viewModel.BatchSyllabusViewModelFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BatchSyllabusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BatchSyllabusFragment : Fragment() {

    companion object {
        private const val TAG = "BatchSyllabusFragment"
    }

    lateinit var fragmentBatchSyllabusBinding: FragmentBatchSyllabusBinding
    lateinit var batchSyllabusVIewModel: BatchSyllabusViewModel
    lateinit var adapter: BatchSyllabusRecyclerAdapter
    lateinit var dialog : CustomProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentBatchSyllabusBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_batch_syllabus,
            container,
            false
        )
        dialog = CustomProgressDialog(requireContext())
        return fragmentBatchSyllabusBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        batchSyllabusVIewModel = ViewModelProvider(
            this,
            BatchSyllabusViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        ).get(
            BatchSyllabusViewModel::class.java
        )

        fragmentBatchSyllabusBinding.viewModel = batchSyllabusVIewModel
        adapter =
            BatchSyllabusRecyclerAdapter()
//        batchSyllabusVIewModel.fetchDataFromApi()

        batchSyllabusVIewModel.fetchSyllabusInfoAPI("29")

        observeData()
    }

    private fun observeData() {
//        batchSyllabusVIewModel.getSyllabusDataFromApi.observe(this.viewLifecycleOwner, Observer {
//            fragmentBatchSyllabusBinding.recyclerViewSyllabus.adapter = adapter
//            adapter.setBatchSyllabusRecyclerData(it as ArrayList<BatchSyllabusData>)
//
//        })


        batchSyllabusVIewModel.resultDat.observe(this.viewLifecycleOwner, Observer {
            var info = it
            when(it.status){
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    val data = String.convertBase64ToString(it.data?.batchSyllabusResponse?.get(0)?.syllabusTextContent)
//                    val jsonArray = JSONArray(data)
//                    val contentList = jsonArray.convertTOSyllabusContentItem()
//                    Log.v(TAG, "SIZE${contentList.size.toString()}")
//                    fragmentBatchSyllabusBinding.recyclerViewSyllabus.adapter = adapter
//                    adapter.setBatchSyllabusRecyclerData(contentList)
//                } else {
//                    var check = 12
//                }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

}
