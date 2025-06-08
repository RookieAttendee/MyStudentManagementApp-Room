package com.example.mystudentmanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateStudentActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtMssv: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var btnUpdate: Button

    private lateinit var student: Student
    private var position: Int = -1
    private lateinit var studentDao: StudentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        // Khởi tạo DAO
        studentDao = AppDatabase.getDatabase(this).studentDao()

        edtName = findViewById(R.id.edtName)
        edtMssv = findViewById(R.id.edtMssv)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Nhận dữ liệu từ intent
        student = intent.getSerializableExtra("student") as Student
        position = intent.getIntExtra("position", -1)

        // Gán dữ liệu vào form
        edtName.setText(student.name)
        edtMssv.setText(student.mssv) // MSSV không cho sửa nếu dùng làm primary key
        edtMssv.isEnabled = false
        edtEmail.setText(student.email)
        edtPhone.setText(student.phone)

        btnUpdate.setOnClickListener {
            // Cập nhật dữ liệu từ form
            student.name = edtName.text.toString()
            student.email = edtEmail.text.toString()
            student.phone = edtPhone.text.toString()

            // Cập nhật trong Room
            studentDao.update(student)

            Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
