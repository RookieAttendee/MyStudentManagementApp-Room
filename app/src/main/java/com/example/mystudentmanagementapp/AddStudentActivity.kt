package com.example.mystudentmanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    private lateinit var studentDao: StudentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        // Khởi tạo Room DAO
        val db = AppDatabase.getDatabase(this)
        studentDao = db.studentDao()

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val student = Student(
                name = findViewById<EditText>(R.id.edtName).text.toString(),
                mssv = findViewById<EditText>(R.id.edtMssv).text.toString(),
                email = findViewById<EditText>(R.id.edtEmail).text.toString(),
                phone = findViewById<EditText>(R.id.edtPhone).text.toString()
            )

            // Kiểm tra trùng MSSV trước khi insert
            val existing = studentDao.getById(student.mssv)
            if (existing != null) {
                Toast.makeText(this, "MSSV đã tồn tại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lưu vào Room
            studentDao.insert(student)
            Toast.makeText(this, "Đã thêm sinh viên!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
