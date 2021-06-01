package com.vanganistan.aos.main.fragments.tests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.R
import com.vanganistan.aos.databinding.LectureListFragmentBinding
import com.vanganistan.aos.databinding.TestListFragmentBinding
import com.vanganistan.aos.main.fragments.tests.adapters.TestListAdapter
import com.vanganistan.aos.models.Lecture
import com.vanganistan.aos.models.Test

class TestListFragment : Fragment() {

    private var _binding: TestListFragmentBinding? = null
    private val binding: TestListFragmentBinding get() = _binding!!
    private var list = arrayListOf<Test>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TestListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelableArrayList<Test>(LECTURE_LIST)?.let {
            list = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myRecyclerView.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            if (list.isNotEmpty()){
                adapter = TestListAdapter(list, {
                    Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment
                    ).navigate(R.id.action_testsFragment_to_answerTestFragment, bundleOf(
                        "item" to it,
                        "title" to it.tests.first().lectureName
                    ))
                }, {
                    val test = Test()
                    it.forEach {
                        test.tests.addAll(it.tests)
                    }
                    test.tests.shuffled()

                    Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment
                    ).navigate(R.id.action_testsFragment_to_answerTestFragment, bundleOf(
                        "item" to test,
                        "title" to "Модуль №${test.tests.first().module}"
                    ))
                })
            }
            binding.myRecyclerView.scheduleLayoutAnimation()
            setHasFixedSize(true)
        }

        binding.myRecyclerView.layoutManager!!.onRestoreInstanceState(binding?.myRecyclerView?.layoutManager?.onSaveInstanceState())


        binding.myRecyclerView.isNestedScrollingEnabled = false
        binding.myRecyclerView.scheduleLayoutAnimation()
        initViews()
    }

    private fun initViews() {

    }

    companion object {
        private const val LECTURE_LIST= "LECTURE_LIST"

        fun newInstance(
            list: List<Test>
        ): TestListFragment {
            val arguments = bundleOf(
                LECTURE_LIST to list
            )
            return TestListFragment().apply { this.arguments = arguments }
        }
    }
}