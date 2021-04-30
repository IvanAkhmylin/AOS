package com.vanganistan.aos.start.signIn

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Validation
import com.vanganistan.aos.databinding.FragmentAuthBinding
import com.vanganistan.aos.databinding.FragmentResetPassBinding

class ResetPassFragment : Fragment() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mViewModel: SignInViewModel
    private var _binding: FragmentResetPassBinding? = null
    private val binding: FragmentResetPassBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPassBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SignInViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun init() {
        progressDialog = ProgressDialog(requireActivity())

        mViewModel = ViewModelProviders.of(this ).get(SignInViewModel::class.java)
        mViewModel.userUtilsResult.observe(requireActivity() , Observer {userUtilsResult(it)})

        binding.btnNext.setOnClickListener {
            binding.passwordEditText.let {
                if (Validation.emailValid(it)){
                    mViewModel.changeUserPassWord(it.text.toString().trim())
                }
            }


        }
    }

    private fun userUtilsResult(it: String?) {
        if (it.equals(Constants.SUCCESSFUL)){
            Toast.makeText(requireActivity(), R.string.reset_message , Toast.LENGTH_LONG).show()
            Navigation.findNavController(
                requireActivity(),
                R.id.nav_host
            ).navigate(
                R.id.action_resetPassFragment_to_startScreenFragment
            )
        }else if (it.equals(Constants.FAILURE)){
            Toast.makeText(requireActivity(), "Данный электронный адрес не зарегистрирован", Toast.LENGTH_SHORT).show()
        }
    }


    private fun progressShow(it: Boolean) {
        if (it){
            progressDialog.setMessage("Отправка сообщения для восстановления пароля")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }
    }

}