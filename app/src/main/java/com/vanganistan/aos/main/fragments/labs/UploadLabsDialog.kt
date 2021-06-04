package com.vanganistan.aos.main.fragments.labs

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
import com.vanganistan.aos.databinding.UploadLabsDialogBinding
import com.vanganistan.aos.main.fragments.lecture.ContentViewModel

class UploadLabsDialog() : BottomSheetDialogFragment() {

    private var _binding: UploadLabsDialogBinding? = null
    private val binding: UploadLabsDialogBinding get() = _binding!!

    private lateinit var mViewModel: ContentViewModel
    private lateinit var progressDialog: ProgressDialog

    private val lab = Lab()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this@UploadLabsDialog, ViewModelProvider.NewInstanceFactory()).get(ContentViewModel::class.java)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UploadLabsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.labUploadLiveData.observe(viewLifecycleOwner, uploadLabObserver)

        initViews()
        bindListeners()
    }

    private fun initViews() {

    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)

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
        binding.labCard.setOnClickListener {
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

        binding.educationCard.setOnClickListener {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (CheckPermission.permissionGranted((activity as Activity), permissions)) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                startActivityForResult(intent, 222)

            } else {
                CheckPermission.requestPermission((activity as Activity), permissions, 1)
            }
        }

        binding.lectureName.addTextChangedListener {
            lab.fileDescription = it.toString()
        }

        binding.btnNext.setOnClickListener {
            if (lab.labUri.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Необходимо загрузить PDF файл",
                    Toast.LENGTH_SHORT
                ).show()
            }else if (lab.eduUri.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Необходимо загрузить PDF файл",
                    Toast.LENGTH_SHORT
                ).show()
            }else if (lab.fileDescription.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Необходимо ввести название лекции",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mViewModel.uploadLab(lab)
            }
        }
    }

    val uploadLabObserver = Observer<Resource<String>> {
        when(it.state){
            Resource.State.LOADING -> {
                progressDialog = ProgressDialog(requireContext())
                progressDialog.setCancelable(false)
                progressDialog.show()
                progressDialog.setMessage("Загружаю файлы в базу данных, это может занять пару минут")
            }

            else -> {
                when {
                    it.data?.equals(Constants.SUCCESSFUL) != null -> {
                        Toast.makeText(requireActivity(),"Лабараторная работа успешно загружена",Toast.LENGTH_LONG).show()
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
            lab.labUri = data.data.toString()
            binding.statusLoadLab.setImageResource(R.drawable.ic_done)
            binding.statusLoadTitleLab.text = "PDF Файл загружен"
        }else if (requestCode == 222 && data != null){
            lab.eduUri = data.data.toString()
            binding.statusLoadLabEdu.setImageResource(R.drawable.ic_done)
            binding.statusLoadTitleEdu.text = "PDF Файл загружен"
        }
    }


    private fun setResultAndDismiss(result: Boolean) {
        val resultCode = if (result) Activity.RESULT_OK else Activity.RESULT_CANCELED
        targetFragment?.onActivityResult(targetRequestCode, resultCode, null)

        dismiss()
    }
}