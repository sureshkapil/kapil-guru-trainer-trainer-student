package com.kapilguru.trainer.addStudent.coursesStudentList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.addStudent.AddStudentViewModel
import com.kapilguru.trainer.databinding.FragmentMyStudentsOnlineBinding
import com.kapilguru.trainer.network.Status
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyStudentsOnlineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyStudentsOnlineFragment : Fragment() {

    lateinit var binding: FragmentMyStudentsOnlineBinding
    val viewModel: AddStudentViewModel by viewModels({ requireActivity() })
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: MyStudentCourseRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyStudentsOnlineBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(this.requireContext())
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModelObserver()
    }

    private fun setUpRecyclerView() {
        adapter = MyStudentCourseRecyclerAdapter()
        binding.recy.adapter = adapter
    }

    private fun viewModelObserver() {
        viewModel.getMyCourseStudents()
        viewModel.myCourseStudents.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    response?.data?.myStudentsOnlineApi?.let { it ->
                        addDataToAdapter(it)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun addDataToAdapter(it: List<MyCourseStudentsApi>) {
        adapter.listItem = it as ArrayList<MyCourseStudentsApi>
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MyStudentsOnlineFragment().apply {}
    }
}