package com.vanganistan.aos.Utils

import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Validation {

    fun emailValid(signUpEmail: AppCompatEditText?): Boolean {
        var valid = true
        if (!TextUtils.isEmpty(signUpEmail?.text.toString().trim()) &&
            !android.util.Patterns.EMAIL_ADDRESS.matcher(signUpEmail?.text.toString().trim()).matches()
        ) {
            valid = false
            signUpEmail?.error = "Введенный вами адрес не подходит"
        }
        if (!signUpEmail?.text!!.toString().endsWith(".com", true) && !signUpEmail.text.toString()
                .endsWith(".ru", true)
        ) {
            valid = false
            signUpEmail.error = "Не верное окончание email адреса"
        }

        return valid
    }

    fun passValid(signupPass: AppCompatEditText?): Boolean {
        var valid = true

        if (signupPass?.text!!.trim().isEmpty() || signupPass.text.toString().length < 6) {
            signupPass.error = "Пароль должен состоять из 6 и более символов"
            valid = false
        }

        return valid
    }

    fun nameValid(name: AppCompatEditText): Boolean {
        var valid = true

        if (name.text!!.trim().isEmpty() || name.text.toString().length < 6) {
            name.error = "ФИО должен состоять из 6 и более символов"
            valid = false
        }

        return valid
    }

    fun groupValid(group: AppCompatEditText): Boolean {
        var valid = true

        if (group.text!!.trim().isEmpty() || group.text.toString().length < 2) {
            group.error = "Группа должна состоять из 2 и более символов"
            valid = false
        }

        return valid
    }

    fun getCurrentDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy / HH:mm")
        val date = Date()
        return dateFormat.format(date)
    }

    fun numberValid(number: AppCompatEditText): Boolean {
        var valid = true

        if (number.text!!.trim().isEmpty() || number.text.toString().length < 9) {
            number.error = "Номер не может быть таким коротким"
            valid = false
        }

        return valid
    }

}