package com.example.palindromeuserapp.ui.second

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.palindromeuserapp.R
import com.example.palindromeuserapp.ui.third.ThirdActivity
import com.google.android.material.appbar.MaterialToolbar

class SecondActivity : AppCompatActivity() {

    private lateinit var tvNameFromFirst: TextView
    private lateinit var tvSelectedUser: TextView
    private lateinit var btnChooseUser: Button

    private val selectUserLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedUserName = result.data?.getStringExtra("EXTRA_SELECTED_USER") ?: ""
            tvSelectedUser.text = selectedUserName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)

        topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        tvNameFromFirst = findViewById(R.id.tvNameFromFirst)
        tvSelectedUser = findViewById(R.id.tvSelectedUser)
        btnChooseUser = findViewById(R.id.btnChooseUser)

        val name = intent.getStringExtra("EXTRA_NAME") ?: ""
        tvNameFromFirst.text = name

        btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            selectUserLauncher.launch(intent)
        }
    }
}
