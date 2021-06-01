package com.vanganistan.aos.main.fragments.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.R
import com.vanganistan.aos.models.AnswerList
import com.vanganistan.aos.models.UserTestAction
import java.text.SimpleDateFormat
import java.util.*

class UserActionsAdapter(
    val items: List<UserTestAction>,
    val onItemClick: (UserTestAction) -> Unit
) : RecyclerView.Adapter<UserActionsAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(
            items[position]
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.user_status, parent, false)
        return ViewHolder(root)
    }

    inner class ViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        fun setData(data: UserTestAction) {
            binding.findViewById<TextView>(R.id.title).text = data.title

            binding.findViewById<TextView>(R.id.sub).text = "Дата: ${convertLongToTime(data.date!!)}"
            binding.rootView.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}