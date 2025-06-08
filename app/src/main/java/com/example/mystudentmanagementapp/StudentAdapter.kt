package com.example.mystudentmanagementapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val context: Context,
    private val students: MutableList<Student>
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMssv: TextView = itemView.findViewById(R.id.tvMssv)

        init {
            // Khi click vào item -> hiện menu
            itemView.setOnClickListener {
                showPopupMenu(it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvName.text = student.name
        holder.tvMssv.text = student.mssv
    }

    override fun getItemCount(): Int = students.size

    // --------------------------
    // Popup Menu xử lý sự kiện
    private fun showPopupMenu(view: View, position: Int) {
        val student = students[position]
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.menu_student, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_update -> {
                    // Cập nhật sinh viên
                    if (context is MainActivity) {
                        val intent = Intent(context, UpdateStudentActivity::class.java)
                        intent.putExtra("student", student)
                        intent.putExtra("position", position)
                        context.startActivityForResult(intent, 101)
                    }
                }
                R.id.menu_delete -> {
                    AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa sinh viên này không?")
                        .setPositiveButton("Xóa") { _, _ ->
                            if (context is MainActivity) {
                                val studentToDelete = students[position]
                                context.studentDao.delete(studentToDelete)  // gọi DAO từ MainActivity
                                students.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, students.size)
                            }
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                }
                R.id.menu_call -> {
                    // Gọi điện
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${student.phone}")
                    context.startActivity(intent)
                }
                R.id.menu_email -> {
                    // Gửi email an toàn với createChooser
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "message/rfc822"
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(student.email))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Thông tin sinh viên")
                    intent.putExtra(Intent.EXTRA_TEXT, "Chào ${student.name},\n\n")

                    val chooser = Intent.createChooser(intent, "Chọn ứng dụng email")
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(chooser)
                    } else {
                        Toast.makeText(context, "Không tìm thấy ứng dụng email nào", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        popup.show()
    }
}
