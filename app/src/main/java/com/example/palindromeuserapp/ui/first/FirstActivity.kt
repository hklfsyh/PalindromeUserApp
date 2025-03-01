package com.example.palindromeuserapp.ui.first

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.palindromeuserapp.R
import com.example.palindromeuserapp.ui.second.SecondActivity

class FirstActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPalindrome: EditText
    private lateinit var btnCheck: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        etName = findViewById(R.id.etName)
        etPalindrome = findViewById(R.id.etPalindrome)
        btnCheck = findViewById(R.id.btnCheck)
        btnNext = findViewById(R.id.btnNext)

        // Button Check
        btnCheck.setOnClickListener {
            val text = etPalindrome.text.toString()
            if (text.isEmpty()) {
                showDialog("Please input a sentence!")
            } else {
                if (isPalindrome(text)) {
                    showDialog("isPalindrome")
                } else {
                    showDialog("not palindrome")
                }
            }
        }

        btnNext.setOnClickListener {
            val name = etName.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                showDialog("Please input your name!")
                return@setOnClickListener
            }
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("EXTRA_NAME", name)
            startActivity(intent)
        }
    }

    private fun isPalindrome(sentence: String): Boolean {
        val cleaned = sentence.replace("\\s".toRegex(), "").lowercase()
        return cleaned == cleaned.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", null).show()
    }
}
