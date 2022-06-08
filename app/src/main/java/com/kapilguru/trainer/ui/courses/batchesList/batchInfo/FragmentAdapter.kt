package com.kapilguru.trainer.ui.courses.batchesList.batchInfo

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.BatchEarningsFragment
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.BatchStudentsListFragment
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.syllabus.BatchSyllabusFragment
import com.kapilguru.trainer.ui.courses.batchesList.batchInfo.batchInfoFragments.BatchUpdateFragment

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private  val TAG = "FragmentAdapter"

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        when(position) {
            1 ->  return BatchSyllabusFragment()
            2 ->  return BatchStudentsListFragment()
            3 -> return BatchEarningsFragment()
            4 -> return BatchUpdateFragment()
            else -> return BatchSyllabusFragment()
        }
    }
}