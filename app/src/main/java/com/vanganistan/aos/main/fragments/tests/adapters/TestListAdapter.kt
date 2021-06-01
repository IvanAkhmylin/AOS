package com.vanganistan.aos.main.fragments.tests.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.R
import com.vanganistan.aos.models.Test

class TestListAdapter(
    val items: List<Test>,
    val onItemClick: (Test) -> Unit,
    val onHeaderClick: (List<Test>) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TestItemViewHolder -> holder.setData(
                items[position - 1]
            )
            is ItemSalaryHeaderViewHolder -> holder.setData(
                items[position]
            )

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            SHEET_ITEM -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.test_list_item, parent, false)
                TestItemViewHolder(root)
            }
            else -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.test_item_header, parent, false)
                ItemSalaryHeaderViewHolder(root)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            HEADER_ITEM
        else
            SHEET_ITEM
    }


    inner class TestItemViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        fun setData(data: Test) {
            binding.findViewById<TextView>(R.id.question).text = data.tests.first().lectureName
            binding.findViewById<TextView>(R.id.test_status).text = data.tests.size.toString()
            binding.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    inner class ItemSalaryHeaderViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {

         fun setData(data: Test) {
             binding.findViewById<TextView>(R.id.test_module).text = "Пройти тестирование за ${data.tests.first().module.toString()} модуль"

             binding.setOnClickListener {
                 onHeaderClick(items)
             }
         }
    }

    companion object {
        const val HEADER_ITEM = 0
        const val SHEET_ITEM = 1
    }

}