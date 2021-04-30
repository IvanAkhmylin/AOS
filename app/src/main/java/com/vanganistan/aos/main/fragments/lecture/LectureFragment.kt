package com.vanganistan.aos.main.fragments.lecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.ZoomOutPageTransformer
import com.vanganistan.aos.databinding.FragmentLectureBinding
import com.vanganistan.aos.main.fragments.lecture.adapters.LectureViewPagerAdapter
import com.vanganistan.aos.models.Lecture

class LectureFragment : Fragment() {

    private var _binding: FragmentLectureBinding? = null
    private val binding: FragmentLectureBinding get() = _binding!!

    private lateinit var mViewModel: LectureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this@LectureFragment, ViewModelProvider.NewInstanceFactory()).get(LectureViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLectureBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mViewModel.lecturesLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                else -> {
                    binding.progress.visibility = View.GONE

                    when {
                        it.data?.equals(Constants.SUCCESSFUL) != null -> {
                            Toast.makeText(
                                requireActivity(),
                                "Лекции успешно загружены",
                                Toast.LENGTH_LONG
                            ).show()

                            initViewPager(it.data)

                        }
                        else -> {
                            Toast.makeText(
                                requireActivity(),
                                "Ошибка загрузки лекций",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            initViews()

        })
    }

    private fun initViewPager(list: List<Lecture>) {
        val viewPagerAdapter = LectureViewPagerAdapter(requireActivity())

        binding.viewPager.apply {
            setPageTransformer(ZoomOutPageTransformer())
            adapter = viewPagerAdapter
            isUserInputEnabled = true
            viewPagerAdapter.setSalary(list)
            setCurrentItem(0, false)
        }

        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, pos ->
            tab.text = "Модуль №${pos + 1}"
        }.attach()

    }

    private fun initViews() {
        binding.fab.setOnClickListener {
            val dialog = UploadLectureDialog()
            dialog.setTargetFragment(this@LectureFragment, 404)
            dialog.show(
                parentFragmentManager,
                UploadLectureDialog::class.java.name
            )
        }


    }
}