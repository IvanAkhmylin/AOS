package com.vanganistan.aos.main.fragments.tests

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.hideKeyboard
import com.vanganistan.aos.Utils.showLoading
import com.vanganistan.aos.databinding.AnalyticTestFragmentBinding
import com.vanganistan.aos.databinding.AnswerTestFragmentBinding
import com.vanganistan.aos.main.fragments.lecture.ContentViewModel
import com.vanganistan.aos.main.fragments.tests.adapters.CreateTestRecyclerAdapter
import com.vanganistan.aos.main.fragments.tests.adapters.TestAnalyticAdapter
import com.vanganistan.aos.models.*
import java.util.*
import kotlin.collections.ArrayList


class AnalyticTestFragment : Fragment() {

    private lateinit var lectures: java.util.ArrayList<Lecture>
    private var _binding: AnalyticTestFragmentBinding? = null
    private val binding: AnalyticTestFragmentBinding get() = _binding!!

    var list: ArrayList<TestQuestion> = arrayListOf(TestQuestion())
    lateinit var test: UserTestAction

    private lateinit var mViewModel: ContentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<UserTestAction>("info")?.let {
            test = it
        }

        mViewModel = ViewModelProvider(this@AnalyticTestFragment, ViewModelProvider.NewInstanceFactory()).get(ContentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AnalyticTestFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myRecyclerView.adapter = TestAnalyticAdapter(test.questionList as List<AnswerList>){ id ->
            val item = lectures.find { it.id == id }
            Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment
            ).navigate(
                R.id.action_analyticTestFragment_to_lectureDetailsFragment, bundleOf(
                    "LectureDetails" to item,
                    "title" to item?.fileDescription
                )
            )
        }


        binding.btnNext.setOnClickListener {
            requireActivity().onBackPressed()
        }
        mViewModel.lecturesLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                }

                else -> {

                    when {
                        it.data?.equals(Constants.SUCCESSFUL) != null -> {
                            lectures = it.data
                        }
                    }
                }
            }

        })
    }

}