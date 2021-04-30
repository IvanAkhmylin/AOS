package com.vanganistan.aos.shared

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import com.vanganistan.aos.R
import com.vanganistan.aos.databinding.ToolbarViewBinding

class ToolbarView @JvmOverloads constructor(
    context: Context,
    @Nullable attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private lateinit var binding: ToolbarViewBinding
    private var onBackPressedListener: (() -> Unit)? = null

    init {
        setupView(context)
        setupAttributes(attributeSet)
        setupListeners()
    }

    private fun setupView(context: Context) {
        binding = ToolbarViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun setupAttributes(attributeSet: AttributeSet?) = with(binding) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ToolbarView)
        val title = typedArray.getString(R.styleable.ToolbarView_title) ?: ""
        typedArray.recycle()

        toolbarTitleTv.text = title
    }

    private fun setupListeners() = with(binding) {
        backButton.setOnClickListener {
            onBackPressedListener?.invoke() ?: (context as? Activity)?.onBackPressed()
        }
    }

    fun setTitle(title: CharSequence) = with(binding) {
        toolbarTitleTv.text = title
    }

    fun setOnBackPressedListener(onBackPressedListener: (() -> Unit)?) {
        this.onBackPressedListener = onBackPressedListener
    }
}
