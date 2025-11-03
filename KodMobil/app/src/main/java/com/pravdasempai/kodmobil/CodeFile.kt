package com.pravdasempai.kodmobil

data class CodeFile(
    val id: String,
    val fileName: String,
    val language: String,
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getFileExtension(): String {
        return when (language) {
            "Python" -> "py"
            "C#" -> "cs"
            "Java" -> "java"
            else -> "txt"
        }
    }

    fun getFullFileName(): String {
        return "$fileName.${getFileExtension()}"
    }

    fun getLanguageIcon(): Int {
        return when (language) {
            "Python" -> R.drawable.ic_python
            "C#" -> R.drawable.ic_csharp
            "Java" -> R.drawable.ic_java
            else -> R.drawable.ic_code
        }
    }
}