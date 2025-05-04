package com.example.mystudentmanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val student = Student(
                findViewById<EditText>(R.id.edtName).text.toString(),
                findViewById<EditText>(R.id.edtMssv).text.toString(),
                findViewById<EditText>(R.id.edtEmail).text.toString(),
                findViewById<EditText>(R.id.edtPhone).text.toString()
            )

            val resultIntent = Intent()
            resultIntent.putExtra("student", student)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
