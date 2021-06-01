package com.vanganistan.aos.main.fragments.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vanganistan.aos.App
import com.vanganistan.aos.databinding.FragmentChatBinding
import com.vanganistan.aos.models.User
import com.vanganistan.aos.start.signIn.SignInViewModel
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class ChatFragment : Fragment() {
    private var user = User()
    var fuser: FirebaseUser? = null
    var reference: DatabaseReference? = null

    var messageAdapter: MessageAdapter? = null
    lateinit var mChat: MutableList<Chat>

    lateinit var seenListener: ValueEventListener


    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding get() = _binding!!

    private lateinit var mViewModel: SignInViewModel
    private var uID = ""
    private val event = object : ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {
            binding.loader.visibility = View.GONE
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            mChat.clear()
            binding.loader.visibility = View.GONE

            for (snapshot: DataSnapshot in dataSnapshot.children){
                val chat = snapshot.getValue(Chat::class.java)
                mChat.add(chat!!)
            }
           if (isAdded){
               // adapter
               messageAdapter = MessageAdapter(requireActivity(), mChat,  uID)
               binding.recyclerView.adapter = messageAdapter
           }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uID = arguments?.getString("uid") ?: App.mAuth.uid.toString()
        mViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
                SignInViewModel::class.java
            )
        binding.loader.visibility = View.VISIBLE
        fuser = FirebaseAuth.getInstance().currentUser

        binding.btnSend.setOnClickListener {
            val msg = binding.textSend.text.toString()
            if (!msg.equals("")){
                sendMessage(fuser?.uid, msg)
            } else {
                Toast.makeText(requireContext(), "You can't send empty message", Toast.LENGTH_SHORT).show()
            }
            // kosongkan lagi
            binding.textSend.setText("")
        }

        // firebase
        App.db.collection("users").document(fuser?.uid ?: "0")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                Log.e("onSnapshot", "$querySnapshot, $firebaseFirestoreException")
                if (firebaseFirestoreException == null) {
                    user = querySnapshot?.toObject(User::class.java) ?: User()
                    readMessages()
                }
            }

    }


    private fun readMessages() {
        mChat = arrayListOf()

        reference = FirebaseDatabase.getInstance().getReference("chat")
        reference!!.addValueEventListener(event)
    }

    override fun onPause() {
        super.onPause()
        reference?.removeEventListener(event)
    }

    private fun sendMessage(sender: String?, msg: String) {

        val reference = FirebaseDatabase.getInstance().reference
        val chat = Chat(
            senderId = sender,
            senderName = user.name,
            senderImage = user.userImage,
            message = msg,
            isseen = false,
            date = Date().time
        )

        reference.child("chat").push().setValue(chat)


//        // for notification use
//        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser?.uid!!)
//        reference.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val user = dataSnapshot.getValue(User::class.java)
//                // fot notification
//            }
//
//        })
    }

}
