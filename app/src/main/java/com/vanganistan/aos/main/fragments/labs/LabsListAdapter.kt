package com.vanganistan.aos.main.fragments.labs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.R
import com.vanganistan.aos.models.Lecture
import kotlinx.android.synthetic.main.labs_item.view.*
import kotlinx.android.synthetic.main.upload_labs_dialog.view.*

class LabsListAdapter(
    val onLabClick: (Lab) -> Unit,
    val onEduClick: (Lab) -> Unit
) : RecyclerView.Adapter<LabsListAdapter.LabItemViewHolder>() {

    var data = ArrayList<Lab>()


    fun updateRecyclerAdapter(lab: ArrayList<Lab>) {
        
        data.clear()
        data.addAll(lab)

        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: LabItemViewHolder, position: Int) {
        holder.setData(
            data[position]
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val root = LayoutInflater.from(parent.context).inflate(R.layout.labs_item, parent, false)
        return LabItemViewHolder(root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

  
    inner class LabItemViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        fun setData(data: Lab) {
            binding.findViewById<TextView>(R.id.question).text = data.fileDescription
            
            binding.lab.setOnClickListener {
                onLabClick(data)
            }   
            binding.edu.setOnClickListener {
                onEduClick(data)
            }
        }
    }

}