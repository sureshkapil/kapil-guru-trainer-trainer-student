package com.kapilguru.trainer.ui.guestLectures.addGuestLecture

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AddGuestLecturePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AddGuestLectureDeatilsFragment()
            1-> AddGuestLectureImageFragment()
            else -> AddGuestLectureVideoFragment()
        }
    }
}