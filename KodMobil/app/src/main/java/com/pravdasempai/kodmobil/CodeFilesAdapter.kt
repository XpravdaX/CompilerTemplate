package com.pravdasempai.kodmobil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pravdasempai.kodmobil.databinding.ItemCodeFileBinding

class CodeFilesAdapter(
    private var files: List<CodeFile> = emptyList(),
    private val onFileClick: (CodeFile) -> Unit
) : RecyclerView.Adapter<CodeFilesAdapter.CodeFileViewHolder>() {

    inner class CodeFileViewHolder(private val binding: ItemCodeFileBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(codeFile: CodeFile) {
            binding.languageIcon.setImageResource(codeFile.getLanguageIcon())
            binding.fileName.text = codeFile.fileName
            binding.fileFormat.text = ".${codeFile.getFileExtension()}"
            
            binding.root.setOnClickListener {
                onFileClick(codeFile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeFileViewHolder {
        val binding = ItemCodeFileBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return CodeFileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CodeFileViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount(): Int = files.size

    fun updateFiles(newFiles: List<CodeFile>) {
        files = newFiles
        notifyDataSetChanged()
    }
}