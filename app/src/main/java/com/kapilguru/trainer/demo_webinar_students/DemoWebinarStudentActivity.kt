package com.kapilguru.trainer.demo_webinar_students


import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.trainer.*
import com.kapilguru.trainer.databinding.ActivityDemoWebinarStudentBinding
import com.kapilguru.trainer.network.RetrofitNetwork
import com.kapilguru.trainer.network.Status

class DemoWebinarStudentActivity : BaseActivity() {
    private val TAG = "DemoWebinarStudentActivity"
    lateinit var binding: ActivityDemoWebinarStudentBinding
    lateinit var viewModel: DemoWebinarStudentViewModel
    lateinit var demoWebinarStudentsAdapter: DemoWebinarStudentsAdapter
    lateinit var dialog: CustomProgressDialog
    var isFrom: String? = PARAM_IS_FROM_DEMO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo_webinar_student)
        viewModel = ViewModelProvider(this, DemoWebinarStudentViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(DemoWebinarStudentViewModel::class.java)
        binding.webinarModel = viewModel
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        dialog = CustomProgressDialog(this)
        setAdapter()
        observeViewModelData()
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar, getString(R.string.my_students))
    }

    private fun setAdapter() {
        demoWebinarStudentsAdapter = DemoWebinarStudentsAdapter()
        binding.demoWebinarStudentRecycler.adapter = demoWebinarStudentsAdapter
    }


    private fun observeViewModelData() {
        val webinarId = intent.getStringExtra(PARAM_DEMO_WEBINAR_ID)
        isFrom = intent.getStringExtra(PARAM_IS_FROM)
        if(isFrom == PARAM_IS_FROM_WEBINAR) {
            demoWebinarStudentsAdapter.type = 0
            viewModel.fetchStudentListApi(webinarId.toString())
        } else  {
            demoWebinarStudentsAdapter.type = 1
            viewModel.fetchDemoStudentListApi(webinarId.toString())
        }
        observeWebinarListApiResponse()
    }

    private fun observeWebinarListApiResponse() {
        viewModel.demoWebinarStudents.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { webinarListApi ->
                        webinarListApi.data?.let { data ->

                            demoWebinarStudentsAdapter.batchExamStudentsList = data

                            viewModel.totalWebinars.value = data.size.toString()

                        }
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok) { dialog, id ->
                    setCancelable(true)
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog.show()
    }

}

