package com.kapilguru.trainer.trainerFeatures

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kapilguru.trainer.R
import com.kapilguru.trainer.faculty.FacultyActivity
import com.kapilguru.trainer.coupons.CouponsActivity
import com.kapilguru.trainer.feeManagement.FeeManagement
import kotlinx.android.synthetic.main.fragment_trainer_features.*

/**
 * A simple [Fragment] subclass.
 * Use the [TrainerFeaturesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrainerFeaturesFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trainer_features, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrainerFeaturesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners() {
        add_coupons.setOnClickListener {
            startActivity(Intent(activity, CouponsActivity::class.java))
        }

        faculty.setOnClickListener {
            startActivity(Intent(activity, FacultyActivity::class.java))
        }


        fee_management.setOnClickListener {
            startActivity(Intent(activity, FeeManagement::class.java))
        }

    }

}