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

    lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter
    private var students = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo Room
        val db = AppDatabase.getDatabase(this)
        studentDao = db.studentDao()

        // Lấy dữ liệu từ Room
        students = studentDao.getAll().toMutableList()

        // Thiết lập adapter
        adapter = StudentAdapter(this, students)

        // Gắn vào RecyclerView
        val rvStudents = findViewById<RecyclerView>(R.id.rvStudents)
        rvStudents.adapter = adapter
        rvStudents.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        val newList = studentDao.getAll()
        students.clear()
        students.addAll(newList)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent) // ❗ Không cần startActivityForResult nữa
        }
        return true
    }

    fun showPopupMenu(student: Student, position: Int, view: View) {
        val popup = android.widget.PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_student, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_update -> {
                    val intent = Intent(this, UpdateStudentActivity::class.java)
                    intent.putExtra("student", student)
                    intent.putExtra("position", position)
                    startActivity(intent)
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

    fun deleteStudent(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc chắn muốn xóa?")
            .setPositiveButton("Xóa") { _, _ ->
                val student = students[position]
                studentDao.delete(student)
                students.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
