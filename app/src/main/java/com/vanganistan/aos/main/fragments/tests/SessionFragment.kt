package com.vanganistan.aos.main.fragments.tests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.vanganistan.aos.R
import com.vanganistan.aos.databinding.SessionFragmentBinding
import com.vanganistan.aos.models.Test

class SessionFragment : Fragment() {

    private var _binding: SessionFragmentBinding? = null
    private val binding: SessionFragmentBinding get() = _binding!!
    private var list = arrayListOf<Test>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SessionFragmentBinding.inflate(inflater)
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
        val test = Test()
        list.forEach {
            test.tests.addAll(it.tests)
        }

        binding.btnNext.setOnClickListener {
          if (list.isNotEmpty()){
              Navigation.findNavController(requireActivity(),
                  R.id.nav_host_fragment
              ).navigate(
                  R.id.action_testsFragment_to_answerTestFragment, bundleOf(
                      "item" to test,
                      "title" to "Сессия"
                  ))
          }
        }

    }


    companion object {
        private const val LECTURE_LIST= "LECTURE_LIST"

        fun newInstance(
            list: List<Test>
        ): SessionFragment {
            val arguments = bundleOf(
                LECTURE_LIST to list
            )
            return SessionFragment().apply { this.arguments = arguments }
        }
    }
}