package com.vanganistan.aos.start.signUp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.Validation
import com.vanganistan.aos.databinding.FragmentAuthBinding
import com.vanganistan.aos.databinding.FragmentRegistrationBinding
import com.vanganistan.aos.databinding.FragmentStartScreenBinding
import com.vanganistan.aos.start.signIn.SignInViewModel


class RegistrationFragment : Fragment() {
    var userData = HashMap<String, Any>()
    private lateinit var progressDialog: ProgressDialog

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding get() = _binding!!
    private lateinit var mViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel =
            ViewModelProvider(this@RegistrationFragment, ViewModelProvider.NewInstanceFactory())
                .get(SignUpViewModel::class.java)
    }

    val signUpObserver = Observer<Resource<String>> {
        when (it.state) {
            Resource.State.LOADING -> {
                progressDialog = ProgressDialog(requireActivity())
                progressDialog.setMessage("Регистрация...")
                progressDialog.setCancelable(false)
                progressDialog.show()
            }

            else -> {
                progressDialog.dismiss()

                when {
                    it.data?.equals(Constants.SIGN_UP_SUCCESS) != null -> {
                        requireActivity().onBackPressed()
                        Toast.makeText(
                            requireActivity(),
                            "Регистрация прошла успешно. Проверьте свою почту для верификации",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    it.error?.message.equals(Constants.SIGN_UP_FAIL) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Ошибка отправки данных на сервер.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    it.error?.message.equals(Constants.SIGN_UP_ERROR_SENDING_EMAIL) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Ошибка отправки сообщения на вашу почту, попробуйте зарегистрироваться позже",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    it.error?.message.equals(Constants.SIGN_UP_CANNOT_SIGN_UP_NOW) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Ошибка регистрации, попробуйте зарегистрироваться позже",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    it.error?.message?.contains("already exist") != null -> {
                        Toast.makeText(
                            requireActivity(),
                            "Пользователь с таким email адресом уже существует",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.signInLiveData.observe(viewLifecycleOwner, signUpObserver)
        mViewModel.updateImageLiveData.observe(viewLifecycleOwner, loadimage)

        binding.btnNext.setOnClickListener {

            if (Validation.emailValid(binding.email) && Validation.passValid(binding.password) &&
                Validation.groupValid(binding.group) && Validation.nameValid(binding.name) && Validation.numberValid(binding.number)
            ) {

                userData["email"] = binding.email.text.toString().trim()
                userData["name"] = binding.name.text.toString().trim()
                userData["group"] = binding.group.text.toString().trim()
                userData["number"] = binding.number.text.toString().trim()
                userData["password"] = binding.password.text.toString().trim()
                mViewModel.signUpUser(userData)
            }
        }
    }


    val loadimage = Observer<Boolean> {
        if (it){
            progressDialog.setMessage("Загрузка изобрания...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }
    }
}
