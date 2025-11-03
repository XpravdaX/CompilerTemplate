package com.pravdasempai.kodmobil

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pravdasempai.kodmobil.databinding.FragmentCodeAddBinding
import java.util.UUID

class CodeAdd : Fragment() {

    private var _binding: FragmentCodeAddBinding? = null
    private val binding get() = _binding!!

    private val codeFiles = mutableListOf<CodeFile>()
    private lateinit var adapter: CodeFilesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodeAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCreateButton()
        loadFiles()
    }

    private fun setupRecyclerView() {
        adapter = CodeFilesAdapter(codeFiles) { codeFile ->
            // Обработка клика по файлу - переход в редактор
            openFileEditor(codeFile)
        }

        binding.schemesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = this@CodeAdd.adapter
        }
    }

    private fun setupCreateButton() {
        binding.buttonCreate.setOnClickListener {
            showCreateFileDialog()
        }
    }

    private fun showCreateFileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_new_file, null)

        val etFileName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etFileName)
        val spinnerLanguage = dialogView.findViewById<android.widget.Spinner>(R.id.spinnerLanguage)
        val btnCancel = dialogView.findViewById<android.widget.Button>(R.id.btnCancel)
        val btnCreate = dialogView.findViewById<android.widget.Button>(R.id.btnCreate)

        // Настройка Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.programming_languages,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnCreate.setOnClickListener {
            val fileName = etFileName.text.toString().trim()
            val selectedLanguage = spinnerLanguage.selectedItem as? String

            // Очищаем предыдущие ошибки
            etFileName.error = null

            // Проверка названия файла
            if (fileName.isEmpty()) {
                etFileName.error = "Введите название файла"
                return@setOnClickListener
            }

            // Проверка допустимых символов в имени файла
            if (!isValidFileName(fileName)) {
                etFileName.error = "Название файла содержит недопустимые символы"
                return@setOnClickListener
            }

            // Проверка выбора языка
            if (selectedLanguage == null) {
                showError("Пожалуйста, выберите язык программирования")
                return@setOnClickListener
            }

            // Проверка на уникальность имени файла
            if (isFileNameExists(fileName, selectedLanguage)) {
                etFileName.error = "Файл с таким именем уже существует"
                return@setOnClickListener
            }

            createNewFile(fileName, selectedLanguage)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun isValidFileName(fileName: String): Boolean {
        val invalidChars = arrayOf("/", "\\", ":", "*", "?", "\"", "<", ">", "|", "=", "-")
        return invalidChars.none { fileName.contains(it) }
    }

    private fun isFileNameExists(fileName: String, language: String): Boolean {
        return codeFiles.any { it.fileName == fileName && it.language == language }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(resources.getColor(R.color.error_color, null))
            .show()
    }

    private fun createNewFile(fileName: String, language: String) {
        val newFile = CodeFile(
            id = UUID.randomUUID().toString(),
            fileName = fileName,
            language = language,
            content = getDefaultContent(fileName, language)
        )

        // Добавляем файл в список
        codeFiles.add(0, newFile) // Добавляем в начало списка
        adapter.updateFiles(codeFiles)

        // Сохраняем файлы
        saveFiles()

        // Показываем сообщение об успешном создании
        Snackbar.make(binding.root, "Файл ${newFile.getFullFileName()} создан!", Snackbar.LENGTH_SHORT).show()
    }

    private fun getDefaultContent(fileName: String, language: String): String {
        return when (language) {
            "Python" -> "#!/usr/bin/env python3\n# $fileName.py\n\nprint(\"Hello, World!\")\n"
            "C#" -> "using System;\n\nclass $fileName\n{\n    static void Main(string[] args)\n    {\n        Console.WriteLine(\"Hello, World!\");\n    }\n}"
            "Java" -> "public class $fileName {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}"
            else -> "// $fileName\n"
        }
    }

    private fun openFileEditor(codeFile: CodeFile) {
        val editorFragment = CodeEditorFragment.newInstance(codeFile)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editorFragment)
            .addToBackStack("editor")
            .commit()
    }

    private fun saveFiles() {
        // TODO: Реализовать сохранение файлов в SharedPreferences или базе данных
        // Временно сохраняем в памяти
    }

    private fun loadFiles() {
        // TODO: Реализовать загрузку файлов из SharedPreferences или базы данных
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}