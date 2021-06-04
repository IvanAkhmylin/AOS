package com.vanganistan.aos.main.fragments.labs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.showLoading
import com.vanganistan.aos.databinding.FragmentLabsBinding
import com.vanganistan.aos.main.fragments.lecture.ContentViewModel

class LabsFragment : Fragment() {

    private var _binding: FragmentLabsBinding? = null
    private val binding: FragmentLabsBinding get() = _binding!!
    private var list = arrayListOf<Lab>()
    private lateinit var mViewModel: ContentViewModel
    private val myAdapter = LabsListAdapter({ lab ->
        Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        ).navigate(
            R.id.action_labsFragment_to_labDetailsFragment, bundleOf(
                "labDetails" to lab,
                "isEdu" to false,
                "title" to lab.fileDescription
            )
        )
    }, { edu ->
        Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        ).navigate(
            R.id.action_labsFragment_to_labDetailsFragment, bundleOf(
                "labDetails" to edu,
                "isEdu" to true,
                "title" to edu.fileDescription
            )
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel =
            ViewModelProvider(this@LabsFragment, ViewModelProvider.NewInstanceFactory()).get(
                ContentViewModel::class.java
            )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLabsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myRecyclerView.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = myAdapter
            binding.myRecyclerView.scheduleLayoutAnimation()
            setHasFixedSize(true)
        }

        binding.myRecyclerView.layoutManager!!.onRestoreInstanceState(binding?.myRecyclerView?.layoutManager?.onSaveInstanceState())
        (binding.myRecyclerView.adapter as LabsListAdapter).updateRecyclerAdapter(
            list
        )

        binding.myRecyclerView.isNestedScrollingEnabled = false
        binding.myRecyclerView.scheduleLayoutAnimation()
        initViews()

        mViewModel.labsLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                    requireActivity().showLoading(true)
                }

                else -> {
                    requireActivity().showLoading(false)
                    it.data?.let {
                        myAdapter.updateRecyclerAdapter(it)
                    }

                }
            }
        })

    }

    private fun initViews() {
        binding.fab.setOnClickListener {
            val dialog = UploadLabsDialog()
            dialog.setTargetFragment(this@LabsFragment, 404)
            dialog.show(
                parentFragmentManager,
                UploadLabsDialog::class.java.name
            )
        }
    }
}



