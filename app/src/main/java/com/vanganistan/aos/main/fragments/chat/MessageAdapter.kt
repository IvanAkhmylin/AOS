package com.vanganistan.aos.main.fragments.chat
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.vanganistan.aos.R
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(val context: Context,
                     val mChat: List<Chat>,
                     val uid: String): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    val MSG_TYPE_LEFT = 0
    val MSG_TYPE_RIGHT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MSG_TYPE_RIGHT){
            val view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false)
            return ViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false)
            return ViewHolder(view)
        }
    }

    override fun getItemCount(): Int = mChat.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = mChat[position]

        holder.show_message.text = chat.message
        Glide.with(context).load(chat.senderImage)
            .placeholder(R.drawable.ic_person_24_black)
            .error(R.drawable.ic_person_24_black).into(holder.profile_image)


        holder.txt_seen.text = convertLongToTime(chat.date ?: 0L)
        holder.fio.text = chat.senderName
    }
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var show_message: TextView
        var profile_image: ImageView
        var txt_seen: TextView
        var fio: TextView

        init {
            show_message = itemView.findViewById(R.id.show_message)
            profile_image = itemView.findViewById(R.id.profile_image)
            txt_seen = itemView.findViewById(R.id.txt_seen)
            fio = itemView.findViewById(R.id.fio)
        }


    }

    private var fuser: FirebaseUser? = null
    override fun getItemViewType(position: Int): Int {
        fuser = FirebaseAuth.getInstance().currentUser
        if (mChat[position].senderId.equals(fuser?.uid)){
            return MSG_TYPE_RIGHT
        } else {
            return MSG_TYPE_LEFT
        }

    }
}

