package com.vanganistan.aos.main.fragments.tests

import android.R
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.hideKeyboard
import com.vanganistan.aos.Utils.showLoading
import com.vanganistan.aos.databinding.CreateTestFragmentBinding
import com.vanganistan.aos.main.fragments.lecture.ContentViewModel
import com.vanganistan.aos.main.fragments.tests.adapters.CreateTestRecyclerAdapter
import com.vanganistan.aos.models.Lecture
import com.vanganistan.aos.models.Test
import com.vanganistan.aos.models.TestQuestion
import java.util.*


class CreateTestFragment : Fragment() {

    private var _binding: CreateTestFragmentBinding? = null
    private val binding: CreateTestFragmentBinding get() = _binding!!
    val lectures = ArrayList<Lecture>()
    var list: ArrayList<TestQuestion> = arrayListOf(TestQuestion())
    val test = Test()
    private lateinit var progressDialog: ProgressDialog

    private lateinit var mViewModel: ContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this@CreateTestFragment, ViewModelProvider.NewInstanceFactory()).get(ContentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateTestFragmentBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.testUpload.observe(viewLifecycleOwner,uploadObserver )
        mViewModel.lecturesLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                Resource.State.LOADING -> {
                    requireActivity().showLoading(true)
                }

                else -> {
                    requireActivity().showLoading(false)

                    when {
                        it.data?.equals(Constants.SUCCESSFUL) != null -> {

                            if (it.data.isNotEmpty()){
                                lectures.addAll(it.data)

                                ArrayAdapter(
                                    requireContext(),
                                    R.layout.simple_spinner_dropdown_item,
                                    it.data.map { it.fileDescription }
                                ).apply {
                                    binding.spinner.setAdapter(this)
                                    binding.spinner.setText(this.getItem(0))
                                    this.filter.filter(null)

                                }




                                binding.spinner.setOnItemClickListener { parent, view, position, id ->
                                    val data = (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).data
                                    data.forEach { data ->
                                        data.lectureId = it.data[position].id
                                        data.lectureName = it.data[position].fileDescription
                                        data.module = it.data[position].module
                                    }

                                    (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).data = data
                                }

                                val data = (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).data
                                data.forEach { data ->
                                    data.lectureId = lectures[0].id
                                    data.lectureName = lectures[0].fileDescription
                                    data.module = lectures[0].module
                                }

                                (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).data = data
                            }

                        }

                        else -> {
                            requireActivity().showLoading(false)

                            Toast.makeText(
                                requireActivity(),
                                "Ошибка загрузки лекций",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

        })

        binding.myRecyclerView.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = CreateTestRecyclerAdapter()
            binding.myRecyclerView.scheduleLayoutAnimation()
            setHasFixedSize(true)

            (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).updateRecyclerAdapter(
                list
            )

        }

        binding.btnNext.setOnClickListener {
            val data = (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).data
            var validate = true
            for ((index, value) in data.withIndex()){
                if (!value.validate()){
                    binding.myRecyclerView.smoothScrollToPosition(index)
                    validate = false
                    Toast.makeText(
                        requireContext(),
                        "Данные для вопроса № ${index + 1} введены не верно",
                        Toast.LENGTH_SHORT
                    ).show()

                    break
                }
            }

            if (validate){
                test.tests.clear()
                test.tests.addAll(data)
                mViewModel.uploadTest(test)
            }
        }
    }

    val uploadObserver = Observer<Resource<String>> {
        when(it.state){
            Resource.State.LOADING -> {
                progressDialog = ProgressDialog(requireContext())
                progressDialog.setCancelable(false)
                progressDialog.show()
                progressDialog.setMessage("Загружаю тест в базу данных")
            }

            else -> {

                when {
                    it.data?.equals(Constants.SUCCESSFUL) != null -> {
                        Toast.makeText(requireActivity(),"Тест успешно загружен",Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                        binding.myRecyclerView.hideKeyboard()
                        requireActivity().onBackPressed()
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
}