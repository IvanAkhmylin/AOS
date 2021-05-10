package com.vanganistan.aos.main.fragments.tests

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
import com.google.android.material.tabs.TabLayoutMediator
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.ZoomOutPageTransformer
import com.vanganistan.aos.Utils.showLoading
import com.vanganistan.aos.databinding.FragmentTestsBinding
import com.vanganistan.aos.main.fragments.lecture.ContentViewModel
import com.vanganistan.aos.main.fragments.lecture.adapters.LectureViewPagerAdapter
import com.vanganistan.aos.main.fragments.tests.adapters.TestsViewPagerAdapter
import com.vanganistan.aos.models.Lecture
import com.vanganistan.aos.models.Test

class TestsFragment : Fragment() {

    private var _binding: FragmentTestsBinding? = null
    private val binding: FragmentTestsBinding get() = _binding!!

    private lateinit var mViewModel: ContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this@TestsFragment, ViewModelProvider.NewInstanceFactory()).get(ContentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mViewModel.testsLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                    requireActivity().showLoading(true)
                }

                else -> {
                    requireActivity().showLoading(false)

                    when {
                        it.data?.equals(Constants.SUCCESSFUL) != null -> {

                            initViewPager(it.data)

                        }
                        else -> {
                            Toast.makeText(
                                requireActivity(),
                                "Ошибка загрузки тестов",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            initViews()

        })
    }

    private fun initViewPager(list: List<Test>) {
        val viewPagerAdapter = TestsViewPagerAdapter(requireActivity())

        binding.viewPager.apply {
            setPageTransformer(ZoomOutPageTransformer())
            adapter = viewPagerAdapter
            isUserInputEnabled = true
            viewPagerAdapter.setItems(list)
            setCurrentItem(0, false)
        }

        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, pos ->
            if (pos == 2){
                tab.text = "Сессия"
            }else{
                tab.text = "Модуль №${pos + 1}"
            }
        }.attach()

    }

    private fun initViews() {
        binding.fab.setOnClickListener {
            Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment
            ).navigate(
                R.id.action_testsFragment_to_createTestFragment
            )
        }


    }
}