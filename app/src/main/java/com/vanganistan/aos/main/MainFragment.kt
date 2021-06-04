package com.vanganistan.aos

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.vanganistan.aos.databinding.ActivityMainBinding


class MainFragment : Fragment() {
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val mNavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        (activity as StartActivity).setSupportActionBar(binding.toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.lectureFragment,
                R.id.testsFragment,
                R.id.labsFragment,
                R.id.profileFragment,
                R.id.chatFragment,
                R.id.searchFragment
            ), binding.drawerLayout
        )
        setupDrawerLayout()
    }

    private fun setupDrawerLayout() {
        NavigationUI.setupWithNavController(binding.navView, mNavController)
        NavigationUI.setupActionBarWithNavController(
            (requireActivity() as StartActivity),
            mNavController,
            appBarConfiguration
        )

        binding.toolbar.setupWithNavController(mNavController, appBarConfiguration)
    }

}