package com.vanganistan.aos

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class App: Application() {

    companion object{
        lateinit var mAuth: FirebaseAuth
        lateinit var mStorage: StorageReference
        lateinit var mLectureStorage: StorageReference
        lateinit var mLectureLectureDB: CollectionReference
        @SuppressLint("StaticFieldLeak")
        lateinit var db: FirebaseFirestore
        lateinit var usersDB: CollectionReference
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        fbInit()
    }

    private fun fbInit() {
        mAuth = FirebaseAuth.getInstance()

        mStorage = FirebaseStorage.getInstance().reference
        mLectureStorage = FirebaseStorage.getInstance().reference.child("lectures")
        sharedPreferences = getSharedPreferences("AOS", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        usersDB = FirebaseFirestore.getInstance().collection("users")
        mLectureLectureDB = FirebaseFirestore.getInstance().collection("lectures")
        db = FirebaseFirestore.getInstance()
    }
}