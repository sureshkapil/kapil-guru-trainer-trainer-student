package com.kapilguru.trainer.myClassroom.totalActiveBatches

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.announcement.newMessage.data.NewMessageData
import com.kapilguru.trainer.databinding.FragmentActiveCoursesBinding
import com.kapilguru.trainer.myClassroom.viewModel.MyClassroomViewModel
import com.kapilguru.trainer.network.Status
import java.util.LinkedHashMap

class ActiveCoursesFragment : Fragment() ,ActiveCoursesAdapter.CourseClickListerner{
    private val TAG = "ActiveBatchesFragment"
    lateinit var binding: FragmentActiveCoursesBinding
    val viewModel: MyClassroomViewModel by viewModels({ requireParentFragment() })
    lateinit var adapter: ActiveCoursesAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveCoursesBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        adapter = ActiveCoursesAdapter(this)
        binding.rvBatch.adapter = adapter
        categoriseBatchesAndSetData(viewModel.activeBatchesList.value as ArrayList<NewMessageData>)
    }

    private fun categoriseBatchesAndSetData(batchList: ArrayList<NewMessageData>) {
        val courseBatchList = LinkedHashMap<Int, ArrayList<NewMessageData>>()
        for (batch in batchList) {
            if (courseBatchList.containsKey(batch.courseId)) {
                val list = courseBatchList.get(batch.courseId)
                list?.add(batch)
                courseBatchList.put(batch.courseId!!, list!!)
            } else {
                val list = ArrayList<NewMessageData>()
                list.add(batch)
                courseBatchList.put(batch.courseId!!, list)
            }
        }
        adapter.setData(courseBatchList)
    }

    override fun onCourseClicked(batchList: ArrayList<NewMessageData>) {
        val intent = Intent(requireContext(),ActiveBatchesActivity::class.java)
        intent.putParcelableArrayListExtra(ActiveBatchesActivity.batch_list,batchList)
        intent.putExtra(ActiveBatchesActivity.course_name,batchList[0].courseTitle)
        startActivity(intent)
    }
}