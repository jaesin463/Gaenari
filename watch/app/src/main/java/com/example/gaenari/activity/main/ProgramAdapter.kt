package com.example.gaenari.activity.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.gaenari.R
import com.example.gaenari.activity.CountdownActivity
import com.example.gaenari.dto.response.FavoriteResponseDto

class ProgramAdapter(private val programs: List<FavoriteResponseDto>) : RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_program, parent, false)
        return ProgramViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        val program = programs[position]

        // 배경 리소스 설정
        val backgroundResource = when (program.type) {
            "D" -> R.drawable.distancecircle
            "T" -> R.drawable.timecircle
            "I" -> R.drawable.intercircle
            else -> R.drawable.circular_background
        }

        holder.icon.setBackgroundResource(backgroundResource)

        // 텍스트 뷰 업데이트
        val text = when (program.type) {
            "D" -> "거리"
            "T" -> "시간"
            "I" -> "인터벌"
            else -> "기본"
        }

        holder.menu.text = text

        // 아이콘 클릭 이벤트 수정
        holder.icon.setOnClickListener {
            val context = holder.itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null)
            val programNameTextView: TextView = dialogView.findViewById(R.id.programName)
            val startButton: Button = dialogView.findViewById(R.id.buttonStart)
            val cancelButton: Button = dialogView.findViewById(R.id.buttonCancel)

            programNameTextView.text = program.programTitle

            val alertDialog = AlertDialog.Builder(context).apply {
                setView(dialogView)
                setCancelable(false)  // 다이얼로그 바깥을 터치해도 닫히지 않도록 설정
            }.create()

            startButton.setOnClickListener {
                // 인텐트 시작
                val intent = Intent(context, CountdownActivity::class.java).apply {
                    putExtra("programId", program.programId)
                    putExtra("programTitle", program.programTitle)
                    putExtra("programType", program.type)
                    if (program.type == "D" ) {
                        putExtra("programData", program)
                        putExtra("programTarget", program.program.targetValue)
                    }
                    if (program.type == "T") {
                        putExtra("programData", program)
                        putExtra("programTarget", program.program.targetValue?.toInt())
                    }
                    if (program.type == "I") {
                        putExtra("programData", program)
                    }
                    if (program.type == "R") {
                        putExtra("programData", program)
                    }
                    if (program.type == "W") {
                        putExtra("programData", program)
                    }
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
                alertDialog.dismiss()
            }

            cancelButton.setOnClickListener {
                alertDialog.dismiss()
            }

            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(-0x30000000))
            }
            alertDialog.show()
        }

    }

    override fun getItemCount(): Int = programs.size

    class ProgramViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.programIcon)
        val menu: TextView = view.findViewById(R.id.menu)
    }
}