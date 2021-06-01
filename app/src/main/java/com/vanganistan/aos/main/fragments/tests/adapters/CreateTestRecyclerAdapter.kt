package com.vanganistan.aos.main.fragments.tests.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.vanganistan.aos.R
import com.vanganistan.aos.models.TestQuestion
import android.widget.LinearLayout




class CreateTestRecyclerAdapter(
    val isInputMutable: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = ArrayList<TestQuestion>()
    var animatedPosition = -1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TestItemHolder -> holder.setData(
                data[position - 1]
            )
            is TestHeaderHolder -> holder.setData(
                data[position]
            )

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TestListAdapter.SHEET_ITEM -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.create_test_item, parent, false)
                TestItemHolder(root)
            }
            else -> {
                val root = LayoutInflater.from(parent.context).inflate(R.layout.create_test_control_item, parent, false)

                val param = root.getLayoutParams()
                if (isInputMutable) {
                    param.height = LinearLayout.LayoutParams.WRAP_CONTENT
                    param.width = LinearLayout.LayoutParams.MATCH_PARENT
                    root.setVisibility(View.VISIBLE)
                } else {
                    root.setVisibility(View.GONE)
                    param.height = 0
                    param.width = 0
                }
                root.setLayoutParams(param)

                TestHeaderHolder(root)
            }
        }


    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            TestListAdapter.HEADER_ITEM
        else
            TestListAdapter.SHEET_ITEM
    }

    inner class TestItemHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {
        fun setData(model: TestQuestion) {
            initListeners()
            initData(model)
        }

        private fun initData(model: TestQuestion){
            binding.findViewById<TextView>(R.id.title).text = "Вопрос № ${adapterPosition}"
            binding.findViewById<TextInputLayout>(R.id.question).editText?.setText(model.question)
            binding.findViewById<TextInputLayout>(R.id.question).editText!!.isFocusable = isInputMutable

            binding.findViewById<TextInputLayout>(R.id.answer1).editText?.setText(model.answers[0])
            binding.findViewById<TextInputLayout>(R.id.answer1).editText!!.isFocusable = isInputMutable

            binding.findViewById<TextInputLayout>(R.id.answer2).editText?.setText(model.answers[1])
            binding.findViewById<TextInputLayout>(R.id.answer2).editText!!.isFocusable = isInputMutable

            binding.findViewById<TextInputLayout>(R.id.answer3).editText?.setText(model.answers[2])
            binding.findViewById<TextInputLayout>(R.id.answer3).editText!!.isFocusable = isInputMutable

            binding.findViewById<TextInputLayout>(R.id.answer4).editText?.setText(model.answers[3])
            binding.findViewById<TextInputLayout>(R.id.answer4).editText!!.isFocusable = isInputMutable

            val group =  binding.findViewById<RadioGroup>(R.id.module_group)
            val id = group.getChildAt(model.trueAnswerIndex).id

            if (isInputMutable) {
                group.check(id)
            }
        }
        private fun initListeners(){
            val context = binding.context
            binding.findViewById<TextInputLayout>(R.id.question).editText?.addTextChangedListener {
                data[adapterPosition- 1].question = it.toString()
            }
            binding.findViewById<TextInputLayout>(R.id.answer1).editText?.addTextChangedListener {
                data[adapterPosition- 1].answers[0] = it.toString()
            }
            binding.findViewById<TextInputLayout>(R.id.answer2).editText?.addTextChangedListener {
                data[adapterPosition- 1].answers[1] = it.toString()
            }
            binding.findViewById<TextInputLayout>(R.id.answer3).editText?.addTextChangedListener {
                data[adapterPosition- 1].answers[2] = it.toString()
            }
            binding.findViewById<TextInputLayout>(R.id.answer4).editText?.addTextChangedListener {
                data[adapterPosition- 1].answers[3] = it.toString()
            }

            if (isInputMutable){
                binding.findViewById<RadioGroup>(R.id.module_group).setOnCheckedChangeListener { group, checkedId ->
                    data[adapterPosition- 1].trueAnswerIndex = binding.findViewById<RadioButton>(checkedId).text.toString().toInt() - 1
                }
            }else{
                binding.findViewById<RadioGroup>(R.id.module_group).setOnCheckedChangeListener { group, checkedId ->
                    data[adapterPosition- 1].userAnswerIndex = binding.findViewById<RadioButton>(checkedId).text.toString().toInt() - 1
                }

            }
            setRecyclerViewAnimation(context, binding, position)
        }
    }

    inner class TestHeaderHolder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {

        fun setData(model: TestQuestion) {

            binding.findViewById<MaterialButton>(R.id.add).setOnClickListener {
                try {
                    if (data.size == 12){
                        throw Exception("ERROR")
                    }else{
                        data.add(TestQuestion())
                        notifyDataSetChanged()
                    }
                }catch (e: Exception){
                    Toast.makeText(
                        binding.context,
                        "Невозможно добавить больше 12 вопросов",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.findViewById<MaterialButton>(R.id.minus).setOnClickListener {
                try {
                    if (data.size == 1){
                        throw Exception("ERROR")
                    }else{
                        data.removeAt(data.size - 1)
                        notifyDataSetChanged()
                    }
                }catch (e: Exception){
                    Toast.makeText(
                        binding.context,
                        "Невозможно удалить элемент списка",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun updateRecyclerAdapter(news: ArrayList<TestQuestion>) {
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

    
    private fun setRecyclerViewAnimation(context: Context, itemView: View, position: Int) {
        if (position > animatedPosition) {
            AnimationUtils.loadAnimation(context, R.anim.fall_down_animation).apply {
                itemView.animation = this
                start()
            }
            animatedPosition = position
        }
    }

    fun clearAdapterData() {
        data.clear()
        notifyDataSetChanged()
    }
    

}