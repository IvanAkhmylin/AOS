package com.vanganistan.aos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        application.setTheme(R.style.NoActionBar)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)

        val navController = Navigation.findNavController(this, R.id.nav_host)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if(App.mAuth.currentUser != null && App.mAuth.currentUser!!.isEmailVerified){
            navGraph.startDestination = R.id.mainFragment
        }else{
            navGraph.startDestination = R.id.startScreenFragment
        }


        navController.graph = navGraph
    }



}