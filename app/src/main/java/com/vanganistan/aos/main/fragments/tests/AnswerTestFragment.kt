package com.vanganistan.aos.main.fragments.tests

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanganistan.aos.App
import com.vanganistan.aos.R
import com.vanganistan.aos.Utils.Constants
import com.vanganistan.aos.Utils.Resource
import com.vanganistan.aos.Utils.Validation.getCurrentDate
import com.vanganistan.aos.Utils.hideKeyboard
import com.vanganistan.aos.databinding.AnswerTestFragmentBinding
import com.vanganistan.aos.main.fragments.lecture.ContentViewModel
import com.vanganistan.aos.main.fragments.tests.adapters.CreateTestRecyclerAdapter
import com.vanganistan.aos.models.AnswerList
import com.vanganistan.aos.models.Test
import com.vanganistan.aos.models.TestQuestion
import com.vanganistan.aos.models.UserTestAction
import java.util.*


class AnswerTestFragment : Fragment() {

    private var _binding: AnswerTestFragmentBinding? = null
    private val binding: AnswerTestFragmentBinding get() = _binding!!
    var list: ArrayList<TestQuestion> = arrayListOf(TestQuestion())
    var test = Test()

    private lateinit var mViewModel: ContentViewModel
    private lateinit var type: String
    private lateinit var info: UserTestAction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Test>("item").let {
            test = it!!
        }
        arguments?.getString("title").let {
            type = it ?: ""
        }


        mViewModel =
            ViewModelProvider(this@AnswerTestFragment, ViewModelProvider.NewInstanceFactory()).get(
                ContentViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AnswerTestFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.answerUpload.observe(viewLifecycleOwner, uploadObserver)
        binding.myRecyclerView.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = CreateTestRecyclerAdapter(false)
            binding.myRecyclerView.scheduleLayoutAnimation()
            setHasFixedSize(true)

            (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).updateRecyclerAdapter(
                test.tests
            )

        }

        binding.btnNext.setOnClickListener {
            val data = (binding.myRecyclerView.adapter as CreateTestRecyclerAdapter).data
            var validate = true
            val questionList = arrayListOf<AnswerList>()
            for ((index, value) in data.withIndex()) {
                if (!value.answerValid()) {
                    binding.myRecyclerView.smoothScrollToPosition(index)
                    validate = false
                    Toast.makeText(
                        requireContext(),
                        "Необходимо ответить на все вопросы",
                        Toast.LENGTH_SHORT
                    ).show()
                    break
                } else {
                    questionList.add(
                        AnswerList(
                            question = value.question,
                            userAnswerTrue = value.isUserAnswerTrue(),
                            testLectureName = test.tests.first().lectureName,
                            lectureId = test.tests.first().lectureId
                        )
                    )
                }
            }



            info = UserTestAction(
                date = Date().time,
                title = getTitle(type),
                questionList = questionList
            )

            if (validate) {
                test.tests.clear()
                test.tests.addAll(data)
                mViewModel.uploadAction(info)
                Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment
                ).navigate(
                    R.id.action_answerTestFragment_to_analyticTestFragment, bundleOf(
                        "info" to info,
                        type to type
                    )
                )
            } else {
                questionList.clear()
            }
        }
    }

    val uploadObserver = Observer<Resource<String>> {
        when (it.state) {
            Resource.State.LOADING -> {

            }

            else -> {

                when {
                    it.data?.equals(Constants.SUCCESSFUL) != null -> {
                        Toast.makeText(
                            requireActivity(),
                            "Ответ успешно загружен",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.myRecyclerView.hideKeyboard()
                    }

                    it.error?.message.equals(Constants.FAILURE) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Ошибка отправки данных на сервер.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun getTitle(type: String): String {
        return when {
            type.contains("Модуль") -> {
                "Прохождение модуля"
            }
            type.contains("Сессия") -> {
                "Прохождение итогового экзамена"
            }
            else -> "Прохождение теста"
        }
    }

    enum class TestType {
        JUST_TEST,
        MODULE,
        SESSION
    }

}