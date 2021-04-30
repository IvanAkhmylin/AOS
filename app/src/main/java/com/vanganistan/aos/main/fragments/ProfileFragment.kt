package com.vanganistan.aos.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.databinding.FragmentProfileBinding
import com.vanganistan.aos.start.signIn.SignInViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private lateinit var mViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this@ProfileFragment, ViewModelProvider.NewInstanceFactory()).get(
            SignInViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.signInLiveData.observe(viewLifecycleOwner, signInObserver)

        binding.btnNext.setOnClickListener {
          mViewModel.exitFromApp()
        }
    }

    private fun userUtilsResult(it: String?) {
        Navigation.findNavController(requireActivity(),
            R.id.nav_host
        ).navigate(R.id.action_mainFragment_to_startScreenFragment)
    }


    private val signInObserver = Observer<Resource<String>> {
        userUtilsResult(it.data)
    }
}




