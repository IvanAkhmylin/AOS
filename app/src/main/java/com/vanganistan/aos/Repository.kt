package com.vanganistan.aos

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vanganistan.aos.App.Companion.editor
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Constants.FAILURE
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.models.Lecture
import com.vanganistan.aos.models.Test
import com.vanganistan.aos.models.User
import com.vanganistan.aos.models.UserTestAction
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import android.R.attr.action
import com.google.firebase.firestore.FieldValue
import com.vanganistan.aos.main.fragments.labs.Lab


class Repository {
    val userData = MutableLiveData<User>()

    fun signUpUser(userData: HashMap<String, Any>, action: (Resource<String>) -> Unit) {
        App.mAuth.createUserWithEmailAndPassword(
            userData.get("email").toString(),
            userData.get("password").toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    App.mAuth.setLanguageCode("ru")
                    App.mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            userData.put("uid", App.mAuth.uid!!)
                            App.db.collection("users")
                                .document(App.mAuth.uid!!)
                                .set(userData)
                                .addOnSuccessListener {
                                    action(Resource.success(Constants.SIGN_UP_SUCCESS))
                                }
                                .addOnFailureListener {
                                    action(Resource.error(Exception(Constants.SIGN_UP_FAIL)))
                                }
                        } else {
                            action(Resource.error(Exception(Constants.SIGN_UP_ERROR_SENDING_EMAIL)))
                        }
                    }
                } else {
                    action(Resource.error(Exception(Constants.SIGN_UP_CANNOT_SIGN_UP_NOW)))
                }
            }
            .addOnFailureListener {
                if (it.message?.contains("email address is already in use by another account")!!) {
                    action(Resource.error(Exception(Constants.SIGN_UP_EMAIL_EXIST)))
                } else {
                    action(Resource.error(Exception(it.message!!)))
                }
            }

    }

    fun signInUser(email: String, pass: String, action: (Resource<String>) -> Unit) {
        App.mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (App.mAuth.currentUser!!.isEmailVerified) {
                        action(Resource.success(Constants.SIGN_IN_COMPLETE))
                    } else {
                        App.mAuth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    action(Resource.error(Exception(Constants.SIGN_IN_COMPLETE)))
                                } else {
                                    action(Resource.error(Exception(Constants.SIGN_IN_SOMETHING_WRONG)))
                                }
                            }
                    }
                } else {
                    action(Resource.error(Exception(it.exception.toString())))
                }
            }

    }

    fun getUserData(id: String): LiveData<User> {
        App.db.collection("users").document(id)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException == null) {
                    try {
                        userData.postValue(querySnapshot?.toObject(User::class.java))
                    }catch (e: java.lang.Exception){
                        if (querySnapshot is Map<*, *>) {

                        }
                    }
                }
            }

        return userData
    }

    fun setupUserImage(uri: Uri?, onSuccess: () -> Unit) {
        App.mStorage.child("UserProfileImage")
            .child(App.mAuth.currentUser?.uid!!)
            .putFile(uri!!)
            .addOnSuccessListener {
                App.mStorage.child("UserProfileImage")
                    .child(App.mAuth.currentUser?.uid!!).downloadUrl.addOnSuccessListener {
                        App.db.collection("users").document(App.mAuth.currentUser!!.uid).update("userImage", it.toString())
                            .addOnSuccessListener {
                                onSuccess()
                            }
                    }
            }
    }

    fun deleteUser(onResume: (String) -> Unit) {
        App.db.collection("users")
            .whereEqualTo("email", App.mAuth.currentUser?.email)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!.documents) {
                        App.mStorage.child("UserProfileImage")
                            .child(App.mAuth.currentUser?.uid.toString()).delete()
                        App.db.collection("users").document(document.id).delete()
                        App.mAuth.currentUser?.delete()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onResume(Constants.DELETE_USER_OK)
                                }
                            }?.addOnFailureListener {
                                onResume(it.toString())
                            }
                    }
                } else {
                    onResume(Constants.DELETE_USER_FAIL)
                }
            }
    }


    suspend fun uploadLecture(
        lecture: Lecture,
        action: (String) -> Unit
    ) {
        lecture.id = Date().time.toInt()
        val childName = UUID.randomUUID().toString()
        try {

            App.mLectureStorage.child(lecture.id.toString()).putFile(Uri.parse(lecture.fileUri)).await()

            App.mLectureStorage.child(lecture.id.toString()).downloadUrl.addOnSuccessListener {
                lecture.fileUri = it.toString()
            }.await()

            App.db.collection("lectures").document(lecture.id.toString()).set(lecture)
                .addOnSuccessListener {
                    action(Constants.SUCCESSFUL)

                }.addOnCanceledListener {
                    action(FAILURE)
                }
        } catch (e: java.lang.Exception) {
            action(FAILURE)
        }
    }
   suspend fun uploadLab(
        lab: Lab,
        action: (String) -> Unit
    ) {
        lab.labId = Date().time.toInt()
        lab.eduId = Date().time.toInt()
        try {

            App.mLabStorage.child(lab.labId.toString()).putFile(Uri.parse(lab.labUri)).await()
            App.mLabStorage.child(lab.eduId.toString()).putFile(Uri.parse(lab.eduUri)).await()

            App.mLabStorage.child(lab.labId.toString()).downloadUrl.addOnSuccessListener {
                lab.labUri = it.toString()
            }.await()

            App.mLabStorage.child(lab.eduId.toString()).downloadUrl.addOnSuccessListener {
                lab.eduUri = it.toString()
            }.await()

            App.db.collection("labs").document(lab.labId.toString()).set(lab)
                .addOnSuccessListener {
                    action(Constants.SUCCESSFUL)
                }.addOnCanceledListener {
                    action(FAILURE)
                }

        } catch (e: java.lang.Exception) {
            action(FAILURE)
        }
    }

    fun exitFromApp(onResume: (String) -> Unit) {
        App.mAuth.signOut()
        onResume(Constants.EXIT_FROM_APP)
    }

    fun changeUserPass(email: String, onResume: (String) -> Unit) {
        App.mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResume(Constants.SUCCESSFUL)
                } else {
                    onResume(Constants.FAILURE)
                }
            }
    }

    fun getLectures(action: (Resource<ArrayList<Lecture>>) -> Unit) {
        App.db.collection("lectures")
            .get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Lecture>()
                for (document in result) {
                    document.toObject(Lecture::class.java)?.let { it1 ->
                        list.add(it1)
                    }
                }

                action(Resource.success(list.reversed() as ArrayList<Lecture>))
            }
            .addOnFailureListener { exception ->
                action(Resource.error(error = java.lang.Exception(FAILURE)))
            }
    }


    fun getLabs(action: (Resource<ArrayList<Lab>>) -> Unit) {
        App.db.collection("labs")
            .get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Lab>()
                for (document in result) {
                    document.toObject(Lab::class.java)?.let { it1 ->
                        list.add(it1)
                    }
                }

                action(Resource.success(list.reversed() as ArrayList<Lab>))
            }
            .addOnFailureListener { exception ->
                action(Resource.error(error = java.lang.Exception(FAILURE)))
            }
    }
    fun getTests(action: (Resource<ArrayList<Test>>) -> Unit) {
        App.db.collection("tests")
            .get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Test>()
                for (document in result) {
                    document.toObject(Test::class.java)?.let { it1 ->
                        list.add(it1)
                    }
                }

                action(Resource.success(list))
            }
            .addOnFailureListener { exception ->
                action(Resource.error(error = java.lang.Exception(FAILURE)))
            }
    }

    fun getLecturesDetail(lecture: Lecture, action: (String) -> Unit) {
        if (App.sharedPreferences.contains(lecture.id.toString())){
            App.sharedPreferences.getString(lecture.id.toString(), "")?.let { action(it) }
        }else{
            val ref = App.mStorage.child("lectures").child(lecture.id.toString())
            val localFile = File.createTempFile(lecture.id.toString(), ".pdf")

            ref.getFile(localFile).addOnSuccessListener {
                editor.putString(lecture.id.toString(), localFile.toUri().toString())
                editor.apply()
                action(localFile.toUri().toString())
            }.addOnFailureListener {
                action(it.localizedMessage)
            }
        }

    }

    fun getLabDetail(lab: Lab, action: (String) -> Unit) {
        if (App.sharedPreferences.contains(lab.labId.toString())){
            App.sharedPreferences.getString(lab.labId.toString(), "")?.let { action(it) }
        }else{
            val ref = App.mStorage.child("labs").child(lab.labId.toString())
            val localFile = File.createTempFile(lab.labId.toString(), ".pdf")

            ref.getFile(localFile).addOnSuccessListener {
                editor.putString(lab.labId.toString(), localFile.toUri().toString())
                editor.apply()
                action(localFile.toUri().toString())
            }.addOnFailureListener {
                action(it.localizedMessage)
            }
        }

    }

    fun getEduDetail(lab: Lab, action: (String) -> Unit) {
        if (App.sharedPreferences.contains(lab.eduId.toString())){
            App.sharedPreferences.getString(lab.eduId.toString(), "")?.let { action(it) }
        }else{
            val ref = App.mStorage.child("labs").child(lab.eduId.toString())
            val localFile = File.createTempFile(lab.eduId.toString(), ".pdf")

            ref.getFile(localFile).addOnSuccessListener {
                editor.putString(lab.eduId.toString(), localFile.toUri().toString())
                editor.apply()
                action(localFile.toUri().toString())
            }.addOnFailureListener {
                action(it.localizedMessage)
            }
        }

    }

    fun uploadTest(test: Test, action: (String) -> Unit) {
        test.id = (0..10000000).random()
        try {
            App.db.collection("tests").document(test.id.toString()).set(test)
                .addOnSuccessListener {
                    action(Constants.SUCCESSFUL)

                }.addOnCanceledListener {
                    action(FAILURE)
                }
        } catch (e: java.lang.Exception) {
            action(FAILURE)
        }
    }

    fun uploadAnswer(info: UserTestAction, action: (String) -> Unit) {
        val id = (0..10000000).random()

        try {
            App.db.collection("users")
                .document(App.mAuth.uid.toString()).update("actions" , FieldValue.arrayUnion(info))
                .addOnSuccessListener {
                    action(Constants.SUCCESSFUL)

                }.addOnCanceledListener {
                    action(FAILURE)
                }
        } catch (e: java.lang.Exception) {
            action(FAILURE)
        }
    }

    fun getUsers(function: (Resource<ArrayList<User>>) -> Unit) {
        App.db.collection("users").get().addOnSuccessListener { result ->
            val list = arrayListOf<User>()
            for (document in result) {
                document.toObject(User::class.java)?.let { it1 ->
                    list.add(it1)
                }
            }

            function(Resource.success(list))
        }.addOnFailureListener{
            function(Resource.error(java.lang.Exception("Ошибка загрузки пользователей")))
        }
    }

}