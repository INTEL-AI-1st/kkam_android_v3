package com.example.kkam_backup.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kkam_backup.R

class BranchListActivity : AppCompatActivity() {

    private val branches = listOf("1지점", "2지점", "3지점", "4지점", "5지점")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch_list)

        val rvBranches = findViewById<RecyclerView>(R.id.rvBranches)
        rvBranches.layoutManager = LinearLayoutManager(this)
        rvBranches.adapter = BranchAdapter(branches) { branchName: String ->
            // 클릭된 지점 이름을 MainActivity에 전달
            val intent = Intent(this@BranchListActivity, MainActivity::class.java)
            intent.putExtra("EXTRA_BRANCH_NAME", branchName)
            startActivity(intent)
            finish()
        }
    }
}
