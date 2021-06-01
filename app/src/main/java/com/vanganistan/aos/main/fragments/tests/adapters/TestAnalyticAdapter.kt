package com.vanganistan.aos.main.fragments.tests.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.R
import com.vanganistan.aos.models.AnswerList

class TestAnalyticAdapter(
    val items: List<AnswerList>,
    val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<TestAnalyticAdapter.TestItemViewHolder>() {


    override fun onBindViewHolder(holder: TestItemViewHolder, position: Int) {
        holder.setData(
            items[position]
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestItemViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.analytic_item, parent, false)
        return TestItemViewHolder(root)
    }

    inner class TestItemViewHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        fun setData(data: AnswerList) {
            binding.findViewById<TextView>(R.id.question).text = data.question

            if (data.userAnswerTrue){
                binding.findViewById<ImageView>(R.id.tests_icon_cash).setImageDrawable(binding.context.getDrawable(R.drawable.test_done))
                binding.findViewById<TextView>(R.id.test_status).text = "Верный"
            }else{
                binding.findViewById<ImageView>(R.id.tests_icon_cash).setImageDrawable(binding.context.getDrawable(R.drawable.wrong))
                binding.findViewById<TextView>(R.id.test_status).text = "Не верный"
            }

            binding.findViewById<CardView>(R.id.move_to).setOnClickListener {
                onItemClick(data.lectureId!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}