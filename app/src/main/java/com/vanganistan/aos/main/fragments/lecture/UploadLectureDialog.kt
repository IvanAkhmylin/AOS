package com.vanganistan.aos.main.fragments.lecture

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.CheckPermission
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.databinding.UploadLectureDialogBinding
import com.vanganistan.aos.models.Lecture
import com.vanganistan.aos.start.signIn.SignInViewModel

class UploadLectureDialog() : BottomSheetDialogFragment() {

    private var _binding: UploadLectureDialogBinding? = null
    private val binding: UploadLectureDialogBinding get() = _binding!!

    private lateinit var mViewModel: LectureViewModel
    private lateinit var progressDialog: ProgressDialog

    private val lecture = Lecture()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this@UploadLectureDialog, ViewModelProvider.NewInstanceFactory()).get(LectureViewModel::class.java)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UploadLectureDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.lectureUploadLiveData.observe(viewLifecycleOwner, uploadLectureObserver)

        initViews()
        bindListeners()
    }

    private fun initViews() {

    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View? =
            dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val bottomSheetBehavior = BottomSheetBehavior.from(it)
            bottomSheetBehavior.halfExpandedRatio = 0.0001f
            bottomSheetBehavior.peekHeight = 1
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }


    private fun bindListeners() {
        binding.fileImage.setOnClickListener {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (CheckPermission.permissionGranted((activity as Activity), permissions)) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                startActivityForResult(intent, 111)

            } else {
                CheckPermission.requestPermission((activity as Activity), permissions, 1)
            }
        }

        binding.lectureName.addTextChangedListener {
            lecture.fileDescription = it.toString()
        }

        binding.btnNext.setOnClickListener {
            if (lecture.fileUri.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Необходимо загрузить PDF файл лекции",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (lecture.fileDescription.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Необходимо ввести название лекции",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lecture.module = if (binding.yes.isChecked) 1 else 2
                mViewModel.uploadLecture(lecture)
            }
        }
    }

    val uploadLectureObserver = Observer<Resource<String>> {
        when(it.state){
            Resource.State.LOADING -> {
                progressDialog = ProgressDialog(requireContext())
                progressDialog.setCancelable(false)
                progressDialog.show()
                progressDialog.setMessage("Загружаю лекцию в базу данных")
            }

            else -> {

                when {
                    it.data?.equals(Constants.SUCCESSFUL) != null -> {
                        Toast.makeText(requireActivity(),"Лекция успешно загружена",Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                        this.dismiss()
                    }
                    it.error?.message.equals(Constants.FAILURE) -> {
                        Toast.makeText(requireActivity(),"Ошибка отправки данных на сервер.",Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }
                    else -> { }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && data != null) {
            lecture.fileUri = data.data.toString()
            binding.statusLoad.setImageResource(R.drawable.ic_done)
            binding.statusLoadTitle.text = "PDF Файл загружен"
        }
    }


    private fun setResultAndDismiss(result: Boolean) {
        val resultCode = if (result) Activity.RESULT_OK else Activity.RESULT_CANCELED
        targetFragment?.onActivityResult(targetRequestCode, resultCode, null)

        dismiss()
    }
}