package com.kapilguru.trainer.ui.webiner.addWebinar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AddWebinarPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
  FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
             0 -> AddWebinarDetailsFragment()
             1 -> AddWebinarImageFragment()
            else -> AddWebinarVideoFragment()
        }
    }

}