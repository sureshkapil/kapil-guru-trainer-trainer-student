package com.kapilguru.trainer.addStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.R
import com.kapilguru.trainer.databinding.FragmentMyStudentsRecordedBinding
import com.kapilguru.trainer.network.Status

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyStudentMaterialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyStudentMaterialFragment : Fragment() {

    lateinit var binding: FragmentMyStudentsRecordedBinding
    val viewModel: AddStudentViewModel by viewModels({ requireActivity() })
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: MyStudentRecordedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my_students_recorded, container, false)
        binding = FragmentMyStudentsRecordedBinding.inflate(inflater, container, false)
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
        adapter = MyStudentRecordedAdapter()
        binding.recy.adapter = adapter
    }

    private fun viewModelObserver() {
        viewModel.getMyRecordedStudents()
        viewModel.myRecordedStudents.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    response?.data?.myStudentsRecordedStudyMaterialsResponseApi?.let { it ->
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

    private fun addDataToAdapter(it: List<MyStudentsRecordedStudyMaterialsResponseApi>) {
        val data = it.filter { value -> value.isRecorded != 1 }
        adapter.listItem = data as ArrayList<MyStudentsRecordedStudyMaterialsResponseApi>
    }

}