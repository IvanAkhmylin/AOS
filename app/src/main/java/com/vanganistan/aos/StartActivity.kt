package com.vanganistan.aos

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.theartofdev.edmodo.cropper.CropImage
import com.vanganistan.aos.start.signIn.SignInViewModel


class StartActivity : AppCompatActivity() {
    private lateinit var mViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        application.setTheme(R.style.NoActionBar)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)
        mViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                SignInViewModel::class.java
            )
        val navController = Navigation.findNavController(this@StartActivity, R.id.nav_host)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if(App.mAuth.currentUser != null && App.mAuth.currentUser!!.isEmailVerified){
            navGraph.startDestination = R.id.mainFragment
        }else{
            navGraph.startDestination = R.id.startScreenFragment
        }


        navController.graph = navGraph
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val result = CropImage.getActivityResult(data)
            mViewModel.setupUserProfileImage(result.uri)
        }
    }


}