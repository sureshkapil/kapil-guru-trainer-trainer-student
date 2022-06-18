package com.kapilguru.trainer.trainerFeatures

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kapilguru.trainer.R
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
}