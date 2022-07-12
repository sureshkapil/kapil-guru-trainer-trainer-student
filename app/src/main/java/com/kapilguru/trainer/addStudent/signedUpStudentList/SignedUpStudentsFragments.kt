package com.kapilguru.trainer.addStudent.signedUpStudentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kapilguru.trainer.CustomProgressDialog
import com.kapilguru.trainer.addStudent.AddStudentViewModel
import com.kapilguru.trainer.databinding.FragmentSignedUpStudentsFragmentsBinding
import com.kapilguru.trainer.network.Status

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignedUpStudentsFragments.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignedUpStudentsFragments : Fragment() {

    lateinit var binding: FragmentSignedUpStudentsFragmentsBinding
    val viewModel: AddStudentViewModel by viewModels({ requireActivity() })
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: MyStudentSignedUpAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentSignedUpStudentsFragmentsBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(this.requireContext())
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewModel.getSignedUpStudentsList()
        viewModel.signedUpStudentsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    response?.data?.signedUpStudentsListResponseApi?.let { it ->
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

    private fun addDataToAdapter(it: List<SignedUpStudentsListResponseApi>) {
        adapter.listItem = it as ArrayList<SignedUpStudentsListResponseApi>
    }

    private fun setUpRecyclerView() {
        adapter = MyStudentSignedUpAdapter()
        binding.recy.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignedUpStudentsFragments.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = SignedUpStudentsFragments().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}