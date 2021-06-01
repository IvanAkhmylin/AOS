package com.vanganistan.aos.main.fragments.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.vanganistan.aos.App
import com.vanganistan.aos.R
import com.vanganistan.aos.StartActivity
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.databinding.FragmentProfileBinding
import com.vanganistan.aos.models.User
import com.vanganistan.aos.start.signIn.SignInViewModel
import com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE

import android.app.Activity

import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import com.theartofdev.edmodo.cropper.CropImage.getPickImageChooserIntent
import com.vanganistan.aos.models.UserTestAction
import android.icu.lang.UCharacter.GraphemeClusterBreak.V


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private lateinit var mViewModel: SignInViewModel
    private var uID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uID = arguments?.getString("uid") ?: App.mAuth.uid.toString()
        mViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
                SignInViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getUserData(uID).observe(viewLifecycleOwner, Observer { initUser(it) })
        mViewModel.signInLiveData.observe(viewLifecycleOwner, signInObserver)

        binding.btnNext.setOnClickListener {
            mViewModel.exitFromApp()
        }
    }

    private fun initUser(it: User?) {
        it?.let {
            binding.email.text = "Email: ${it.email}"
            binding.group.text = "Группа: ${it.group}"
            binding.fio.text = "${it.name}"
            binding.phone.text = "Телефон: ${it.number}"
            if (it.userImage != null) {
                binding.avatar.setImageURI(Uri.parse(it.userImage))
            }

            binding.avatar.setOnClickListener {
                CropImage.activity()
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .start(requireActivity() as StartActivity)
            }
            it.actions?.let {
                if (it.isNullOrEmpty()) {
                    binding.empty.visibility = View.VISIBLE
                    binding.myRecyclerView.visibility = View.GONE
                } else {
                    binding.empty.visibility = View.GONE
                    binding.myRecyclerView.visibility = View.VISIBLE
                    val list: List<UserTestAction> = ArrayList<UserTestAction>(it.values)
                    binding.myRecyclerView.adapter = UserActionsAdapter(list.sortedByDescending { it.date }) {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.nav_host_fragment
                        ).navigate(
                            R.id.action_profileFragment_to_analyticTestFragment, bundleOf(
                                "info" to it
                            )
                        )
                    }
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val result = CropImage.getActivityResult(data)
            mViewModel.setupUserProfileImage(result.uri)
        }
    }


    private fun userUtilsResult(it: String?) {
        Navigation.findNavController(
            requireActivity(),
            R.id.nav_host
        ).navigate(R.id.action_mainFragment_to_startScreenFragment)
    }


    private val signInObserver = Observer<Resource<String>> {
        userUtilsResult(it.data)
    }


}




