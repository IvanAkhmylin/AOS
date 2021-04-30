package com.vanganistan.aos.main.fragments.lecture

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
import com.vanganistan.aos.main.fragments.lecture.adapters.LectureRecyclerAdapter
import com.vanganistan.aos.models.Lecture

class LectureListFragment : Fragment() {

    private var _binding: LectureListFragmentBinding? = null
    private val binding: LectureListFragmentBinding get() = _binding!!
    private var list = arrayListOf<Lecture>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LectureListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelableArrayList<Lecture>(LECTURE_LIST)?.let {
            list = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myRecyclerView.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = LectureRecyclerAdapter{ item ->
                Navigation.findNavController(requireActivity(),
                    R.id.nav_host_fragment
                ).navigate(R.id.action_lectureFragment_to_lectureDetailsFragment2, bundleOf(
                    "LectureDetails" to item
                ))
            }
            binding.myRecyclerView.scheduleLayoutAnimation()
            setHasFixedSize(true)
        }

        binding.myRecyclerView.layoutManager!!.onRestoreInstanceState(binding?.myRecyclerView?.layoutManager?.onSaveInstanceState())
        (binding.myRecyclerView.adapter as LectureRecyclerAdapter).updateRecyclerAdapter(
            list
        )

        binding.myRecyclerView.isNestedScrollingEnabled = false
        binding.myRecyclerView.scheduleLayoutAnimation()
        initViews()
    }

    private fun initViews() {

    }

    companion object {
        private const val LECTURE_LIST= "LECTURE_LIST"

        fun newInstance(
            list: List<Lecture>
        ): LectureListFragment {
            val arguments = bundleOf(
                LECTURE_LIST to list
            )
            return LectureListFragment().apply { this.arguments = arguments }
        }
    }
}