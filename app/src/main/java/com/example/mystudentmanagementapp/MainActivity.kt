package com.example.mystudentmanagementapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val students = mutableListOf<Student>()
    lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo adapter
        adapter = StudentAdapter(this, students)

        // Thiết lập RecyclerView
        val rvStudents = findViewById<RecyclerView>(R.id.rvStudents)
        rvStudents.adapter = adapter
        rvStudents.layoutManager = LinearLayoutManager(this)
    }

    // Tạo Option Menu (nút thêm sinh viên)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivityForResult(intent, 100)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Thêm mới sinh viên
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val student = data?.getSerializableExtra("student") as? Student
            if (student != null) {
                students.add(student)
                adapter.notifyDataSetChanged()
            }
        }

        // Cập nhật sinh viên
        if (requestCode == 101 && resultCode == RESULT_OK) {
            val updatedStudent = data?.getSerializableExtra("updated_student") as? Student
            val position = data?.getIntExtra("position", -1) ?: -1
            if (updatedStudent != null && position != -1) {

                // Cập nhật từng trường của student cũ
                students[position].name = updatedStudent.name
                students[position].mssv = updatedStudent.mssv
                students[position].email = updatedStudent.email
                students[position].phone = updatedStudent.phone

                adapter.notifyItemChanged(position)
            }
        }
    }

    // Hàm hiển thị popup menu (gọi từ Adapter)
    fun showPopupMenu(student: Student, position: Int, view: View) {
        val popup = android.widget.PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_student, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_update -> {
                    val intent = Intent(this, UpdateStudentActivity::class.java)
                    intent.putExtra("student", student)
                    intent.putExtra("position", position)
                    startActivityForResult(intent, 101)
                }
                R.id.menu_delete -> {
                    deleteStudent(position)
                }
                R.id.menu_call -> {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${student.phone}")
                    startActivity(intent)
                }
                R.id.menu_email -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:${student.email}")
                    startActivity(intent)
                }
            }
            true
        }

        popup.show()
    }

    // Xóa sinh viên (hộp thoại xác nhận)
    fun deleteStudent(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc chắn muốn xóa?")
            .setPositiveButton("Xóa") { _, _ ->
                students.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
