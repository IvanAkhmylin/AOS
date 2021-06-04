package com.vanganistan.aos.main.fragments.search

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanganistan.aos.R
import com.vanganistan.aos.models.User
import kotlinx.android.synthetic.main.user_search_item.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SearchAdapter(
    val items: ArrayList<User>,
    val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(
            items[position]
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.user_search_item, parent, false)
        return ViewHolder(root)
    }

    inner class ViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        fun setData(data: User) {
            binding.findViewById<TextView>(R.id.fio).text = data.name
            binding.findViewById<TextView>(R.id.group).text = data.group
            try {
                binding.imageView.setImageURI(Uri.parse(data.userImage))
            }catch (e: Exception){
                binding.imageView.setImageDrawable(binding.context.getDrawable(R.drawable.ic_person_24_black))
            }
            binding.rootView.setOnClickListener {
                onItemClick(data)
            }
        }
    }


    fun updateRecyclerView(newValues: List<User>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtils(items, newValues))
        items.clear()
        items.addAll(newValues)
        diffResult.dispatchUpdatesTo(this)
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


class DiffUtils(val oldValues: List<User>, val newValues: List<User>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldValues[oldItemPosition].number == newValues[newItemPosition].number)
    }

    override fun getOldListSize(): Int {
        return oldValues.size
    }

    override fun getNewListSize(): Int {
        return newValues.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldValues[oldItemPosition].equals(newValues[newItemPosition])

    }
}
