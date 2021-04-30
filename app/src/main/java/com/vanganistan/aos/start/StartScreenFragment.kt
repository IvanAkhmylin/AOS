package com.vanganistan.aos.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.vanganistan.aos.R
import com.vanganistan.aos.databinding.FragmentStartScreenBinding

class StartScreenFragment : Fragment() {

    private var _binding: FragmentStartScreenBinding? = null
    private val binding: FragmentStartScreenBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartScreenBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            Navigation.findNavController(requireActivity(),
                R.id.nav_host
            ).navigate(R.id.action_startScreenFragment_to_authFragment2)
        }
        binding.buttonSignUp.setOnClickListener {
            Navigation.findNavController(requireActivity(),
                R.id.nav_host
            )
                .navigate(
                    R.id.action_startScreenFragment_to_registrationFragment
                )
        }
    }
}