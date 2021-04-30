package com.vanganistan.aos.main.fragments.lecture.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.vanganistan.aos.R
import com.vanganistan.aos.models.Lecture

class LectureRecyclerAdapter(
    val onClick: (Lecture) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = ArrayList<Lecture>()
    var animatedPosition = -1

    inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.lecture_item, parent, false)
        )
    }


    fun updateRecyclerAdapter(news: ArrayList<Lecture>) {
        if (news.size == 1) {
            animatedPosition = -1
        }

        data.clear()
        data.addAll(news)

        notifyItemChanged(data.size - 1)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = holder.itemView.findViewById<CardView>(R.id.item)
        val title = holder.itemView.findViewById<TextView>(R.id.title)
        val image = holder.itemView.findViewById<SimpleDraweeView>(R.id.news_image)

        image.setImageResource(
            LectureImages.getImage(
                holder.adapterPosition,
                data[position].module
            )
        )
        title.text = data[position].fileDescription

        item.setOnClickListener {
            onClick(data[position])
        }


        setRecyclerViewAnimation(context, holder.itemView, position)
    }

    private fun setRecyclerViewAnimation(context: Context, itemView: View, position: Int) {
        if (position > animatedPosition) {
            AnimationUtils.loadAnimation(context, R.anim.fall_down_animation).apply {
                itemView.animation = this
                start()
            }
            animatedPosition = position
        }
    }

    object LectureImages {
        val list = arrayListOf<Int>(
            R.drawable.lecture_picture1,
            R.drawable.lecture_picture2,
            R.drawable.lecture_picture3,
            R.drawable.lecture_picture4,
            R.drawable.lecture_picture5

        )
        val list2 = arrayListOf<Int>(
            R.drawable.lecture_picture6,
            R.drawable.lecture_picture7,
            R.drawable.lecture_picture8,
            R.drawable.lecture_picture9,
            R.drawable.lecture_picture10
        )

        fun getImage(adapterPosition: Int, module: Int): Int {
            return if (module == 1) {
                if (adapterPosition < 0) {
                    list[0 % list.size]
                } else {
                    list[adapterPosition % list.size]
                }
            } else {
                if (adapterPosition < 0) {
                    list2[0 % list2.size]
                } else {
                    list2[adapterPosition % list2.size]
                }
            }

        }
    }

    fun clearAdapterData() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size


}