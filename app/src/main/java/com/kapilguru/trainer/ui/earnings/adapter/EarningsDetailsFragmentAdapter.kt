package com.kapilguru.trainer.ui.earnings.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kapilguru.trainer.ui.earnings.fragments.CourseAmountFragment
import com.kapilguru.trainer.ui.earnings.fragments.ReferralAmountFragment
import com.kapilguru.trainer.ui.earnings.fragments.WebinarAmountFragment

class EarningsDetailsFragmentAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):
                             FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
       return when(position){
           1 -> ReferralAmountFragment()
           2 -> WebinarAmountFragment()
           else -> CourseAmountFragment()
       }
    }

}