package com.example.mystudentmanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class UpdateStudentActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtMssv: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var btnUpdate: Button

    private lateinit var student: Student
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        edtName = findViewById(R.id.edtName)
        edtMssv = findViewById(R.id.edtMssv)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Nhận dữ liệu
        student = intent.getSerializableExtra("student") as Student
        position = intent.getIntExtra("position", -1)

        edtName.setText(student.name)
        edtMssv.setText(student.mssv)
        edtEmail.setText(student.email)
        edtPhone.setText(student.phone)

        btnUpdate.setOnClickListener {
            student.name = edtName.text.toString()
            student.mssv = edtMssv.text.toString()
            student.email = edtEmail.text.toString()
            student.phone = edtPhone.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("updated_student", student)
            resultIntent.putExtra("position", position)
            setResult(RESULT_OK, resultIntent)

            finish()
        }
    }
}
