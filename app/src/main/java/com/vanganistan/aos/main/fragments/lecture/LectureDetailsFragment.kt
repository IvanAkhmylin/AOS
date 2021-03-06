package com.vanganistan.aos.main.fragments.lecture

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.databinding.LectureFragmentDetailsBinding
import com.vanganistan.aos.models.Lecture

class LectureDetailsFragment : Fragment() {

    private var _binding: LectureFragmentDetailsBinding? = null
    private val binding: LectureFragmentDetailsBinding get() = _binding!!

    private lateinit var mViewModel: ContentViewModel
    private lateinit var lecture: Lecture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Lecture>("LectureDetails").let {
            if (it == null){
                Toast.makeText(
                    requireContext(),
                    "Произошла ошибка загрузки лекции",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                lecture = it
            }
        }
        mViewModel = ViewModelProvider(this@LectureDetailsFragment, ViewModelProvider.NewInstanceFactory()).get(ContentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LectureFragmentDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.getLectureDetail(lecture)
        mViewModel.lectureDetailLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    binding.pdfViewer.fromUri(Uri.parse(it.data)).load()
                }
            }

        })
    }


}