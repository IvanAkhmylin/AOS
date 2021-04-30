package com.vanganistan.aos.main.fragments.lecture.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanganistan.aos.main.fragments.lecture.LectureListFragment
import com.vanganistan.aos.models.Lecture

class LectureViewPagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private val salaryInfo = mutableListOf<Lecture>()

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            LectureListFragment.newInstance(salaryInfo.filter { it.module == 1})
        }else{
            LectureListFragment.newInstance(salaryInfo.filter { it.module == 2})
        }
    }

    fun setSalary(salary: List<Lecture>) {
        this.salaryInfo.clear()
        this.salaryInfo.addAll(salary)
        notifyDataSetChanged()
    }
}
