package com.vanganistan.aos.main.fragments.tests.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanganistan.aos.main.fragments.lecture.LectureListFragment
import com.vanganistan.aos.main.fragments.tests.SessionFragment
import com.vanganistan.aos.main.fragments.tests.TestListFragment
import com.vanganistan.aos.models.Test

class TestsViewPagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private val salaryInfo = mutableListOf<Test>()

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            TestListFragment.newInstance(salaryInfo.filter { it.module == 1})
        }else if (position == 2){
            SessionFragment.newInstance(salaryInfo)
        }else{
            TestListFragment.newInstance(salaryInfo.filter { it.module == 2})
        }
    }

    fun setItems(items: List<Test>) {
        this.salaryInfo.clear()
        this.salaryInfo.addAll(items)
        notifyDataSetChanged()
    }
}
