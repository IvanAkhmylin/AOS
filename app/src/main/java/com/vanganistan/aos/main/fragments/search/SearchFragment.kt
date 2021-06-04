package com.vanganistan.aos.main.fragments.search

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vanganistan.aos.App
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.hideKeyboard
import com.vanganistan.aos.databinding.FragmentChatBinding
import com.vanganistan.aos.databinding.SearchFragmentBinding
import com.vanganistan.aos.models.User
import com.vanganistan.aos.start.signIn.SignInViewModel
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class SearchFragment : Fragment() {
    private var usersList: ArrayList<User>? = null
    private var _binding: SearchFragmentBinding? = null
    private val binding: SearchFragmentBinding get() = _binding!!
    private lateinit var mViewModel: SignInViewModel
    private val adapter = SearchAdapter(arrayListOf<User>()){
        binding.myRecyclerView.hideKeyboard()

        Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        ).navigate(
            R.id.action_searchFragment_to_profileFragment, bundleOf(
                "uid" to it.uid,
                "title" to it.name
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
                SignInViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myRecyclerView.adapter = adapter
        mViewModel.getUsers()

        binding.password.addTextChangedListener { text ->
            if (text.isNullOrEmpty()){
                binding.search.visibility =  View.VISIBLE
                binding.notFoundContainer.visibility =  View.GONE
                binding.myRecyclerView.visibility = View.GONE
            }else{
                val list = usersList?.filter { (it.name ?: "").toLowerCase().contains(text.toString().trim().toLowerCase())}
                if (list.isNullOrEmpty()){
                    binding.search.visibility =  View.GONE
                    binding.notFoundContainer.visibility =  View.VISIBLE
                    binding.myRecyclerView.visibility = View.GONE
                }else{
                    binding.search.visibility =  View.GONE
                    binding.notFoundContainer.visibility =  View.GONE
                    binding.myRecyclerView.visibility = View.VISIBLE
                    adapter.updateRecyclerView(list)
                }
            }
        }

        mViewModel.usersLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                    if (usersList.isNullOrEmpty()){
                        binding.progress.visibility = View.VISIBLE
                    }
                }

                Resource.State.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (usersList.isNullOrEmpty()){
                        binding.search.visibility =  View.VISIBLE
                    }
                    usersList = it.data
                }

                Resource.State.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), "${it.error?.message}", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }


}
