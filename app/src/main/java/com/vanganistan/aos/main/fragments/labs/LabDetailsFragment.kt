package com.vanganistan.aos.main.fragments.labs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vanganistan.aos.R
import com.vanganistan.aos.StartActivity
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.databinding.LabDetailBinding
import com.vanganistan.aos.main.fragments.lecture.ContentViewModel
import java.io.File
import java.net.URI
import java.time.temporal.TemporalAdjusters.next
import androidx.core.app.ShareCompat
import java.lang.Exception
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION

import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION

import com.firebase.ui.auth.AuthUI.getApplicationContext

import androidx.core.content.FileProvider
import com.firebase.ui.auth.AuthUI


class LabDetailsFragment : Fragment() {

    private var _binding: LabDetailBinding? = null
    private val binding: LabDetailBinding get() = _binding!!

    private lateinit var mViewModel: ContentViewModel
    private lateinit var Lab: Lab
    private var isEdu = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Lab>("labDetails").let {
            if (it == null){
                Toast.makeText(
                    requireContext(),
                    "Произошла ошибка загрузки лекции",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Lab = it
            }
        }
        arguments?.getBoolean("isEdu", false)?.let {
            isEdu = it
        }

        mViewModel = ViewModelProvider(this@LabDetailsFragment, ViewModelProvider.NewInstanceFactory()).get(ContentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LabDetailBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.getLab(Lab, isEdu)
        mViewModel.labDetailLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }



                else -> {
                    binding.progress.visibility = View.GONE
                    binding.pdfViewer.visibility = View.VISIBLE
                    binding.pdfViewer.fromUri(Uri.parse(it.data)).load()
//                    if (isEdu){
//                        binding.pptViewer.visibility = View.VISIBLE
//                        binding.pdfViewer.visibility = View.GONE
//                        val docUri =Uri.parse(it.data?.replace("file://", "content://"))
//                        val intent = Intent(Intent.ACTION_VIEW)
//                        intent.setDataAndType(docUri, "application/vnd.ms-powerpoint")
//                        try {
//                            intent.flags =
//                                FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
//                            val chooser = Intent.createChooser(intent, "Open With..")
//                            startActivity(chooser)
//                        } catch (e: ActivityNotFoundException) {
//                            //user does not have a pdf viewer installed
//
//                        }
//
//                    }else{
//
//                    }
                }
            }

        })
    }
    class AppWebViewClients() : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            // TODO Auto-generated method stub
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
        }
    }

}