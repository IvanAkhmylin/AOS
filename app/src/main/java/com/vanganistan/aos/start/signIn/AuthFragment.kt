package com.vanganistan.aos.start.signIn

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.vanganistan.aos.MainFragment
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.Validation.emailValid
import com.vanganistan.aos.Utils.Validation.passValid
import com.vanganistan.aos.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding get() = _binding!!
    private lateinit var mViewModel: SignInViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this@AuthFragment, ViewModelProvider.NewInstanceFactory()).get(SignInViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.signInLiveData.observe(viewLifecycleOwner, signInObserver)

        binding.btnNext.setOnClickListener {
            if (emailValid(binding.email) && passValid(binding.password)) {
                mViewModel.signInUser(binding.email.text.toString().trim(),binding.password.text.toString().trim())
            }
        }
        binding.resetLoginPass.setOnClickListener {
            Navigation.findNavController(
                requireActivity(),
                R.id.nav_host
            ).navigate(
                R.id.action_authFragment2_to_resetPassFragment
            )
        }
    }




    private val signInObserver = Observer<Resource<String>> {
        val msg = it.data ?: it.error?.message ?: ""
        when(it.state){
            Resource.State.LOADING -> {
                progressDialog = ProgressDialog(requireActivity())
                progressDialog.setMessage("Вход...")
                progressDialog.setCancelable(false)
                progressDialog.show()
            }


            else -> {
                progressDialog.dismiss()
                when {
                    msg == Constants.SIGN_IN_COMPLETE -> {
                        Toast.makeText(requireContext(), "WELCOME", Toast.LENGTH_LONG).show()
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.nav_host
                        ).navigate(
                            R.id.action_authFragment2_to_mainFragment
                        )
                    }
                    msg == Constants.SIGN_IN_EMAIL_VERIFIED_SANDE -> Toast.makeText(
                        requireContext(),
                        "На вашу почту был выслано сообщение для верификации вашего email адреса , " +
                                "Пожалуйста верифицируйте свой email адрес",
                        Toast.LENGTH_LONG
                    ).show()
                    msg == Constants.SIGN_IN_SOMETHING_WRONG -> Toast.makeText(
                        requireContext(),
                        "Упсс... что то пошло не так, попробуйте войти позже",
                        Toast.LENGTH_LONG
                    ).show()
                    msg?.contains(Constants.SIGN_IN_INVALID_PASS)!! -> Toast.makeText(
                        requireContext(),
                        "Не верно введен пароль",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    msg.contains(Constants.SIGN_IN_INVALID_EMAIL) -> Toast.makeText(
                        requireContext(),
                        "Данный электронный адрес не зарегистрирован",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    msg.contains(Constants.SIGN_IN_LOGIN_ATTEMPTS) -> Toast.makeText(
                        requireContext(),
                        "Слишком много запросов, попробуйте войти позже",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(
                        requireContext(),
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


