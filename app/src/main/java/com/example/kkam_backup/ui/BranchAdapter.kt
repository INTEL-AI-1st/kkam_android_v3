package com.example.kkam_backup.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.kkam_backup.R

class BranchAdapter(
    private val branches: List<String>,
    private val onBranchClick: (branchName: String) -> Unit
) : RecyclerView.Adapter<BranchAdapter.BranchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_branch, parent, false)
        return BranchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BranchViewHolder, position: Int) {
        val branchName = branches[position]
        holder.btnBranch.text = branchName
        holder.btnBranch.setOnClickListener {
            onBranchClick(branchName)
        }
    }

    override fun getItemCount(): Int = branches.size

    class BranchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnBranch: Button = itemView.findViewById(R.id.btnBranch)
    }
}
