package com.pravdasempai.kodmobil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pravdasempai.kodmobil.databinding.FragmentCodeEditorBinding

class CodeEditorFragment : Fragment() {

    private var _binding: FragmentCodeEditorBinding? = null
    private val binding get() = _binding!!

    private lateinit var codeFile: CodeFile

    companion object {
        private const val ARG_FILE_ID = "file_id"
        private const val ARG_FILE_NAME = "file_name"
        private const val ARG_LANGUAGE = "language"
        private const val ARG_CONTENT = "content"

        fun newInstance(codeFile: CodeFile): CodeEditorFragment {
            val args = Bundle().apply {
                putString(ARG_FILE_ID, codeFile.id)
                putString(ARG_FILE_NAME, codeFile.fileName)
                putString(ARG_LANGUAGE, codeFile.language)
                putString(ARG_CONTENT, codeFile.content)
            }
            return CodeEditorFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            codeFile = CodeFile(
                id = it.getString(ARG_FILE_ID) ?: "",
                fileName = it.getString(ARG_FILE_NAME) ?: "",
                language = it.getString(ARG_LANGUAGE) ?: "",
                content = it.getString(ARG_CONTENT) ?: ""
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodeEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        // Устанавливаем иконку языка
        binding.languageIcon.setImageResource(codeFile.getLanguageIcon())

        // Устанавливаем информацию о файле
        binding.fileName.text = codeFile.getFullFileName()
        binding.languageName.text = codeFile.language

        // Загружаем код в редактор
        binding.codeEditor.setText(codeFile.content)

        // Устанавливаем подсказку с синтаксисом
        setSyntaxHint()
    }

    private fun setSyntaxHint() {
        val hint = when (codeFile.language) {
            "Python" -> "# Напишите ваш Python код здесь...\nprint(\"Hello, World!\")\n"
            "C#" -> "// Напишите ваш C# код здесь...\nConsole.WriteLine(\"Hello, World!\");\n"
            "Java" -> "// Напишите ваш Java код здесь...\nSystem.out.println(\"Hello, World!\");\n"
            else -> "// Напишите ваш код здесь...\n"
        }

        if (binding.codeEditor.text.isEmpty()) {
            binding.codeEditor.setText(hint)
        }
    }

    private fun setupClickListeners() {
        // Кнопка сохранения
        binding.btnSave.setOnClickListener {
            saveCode()
        }

        // Кнопка запуска кода
        binding.btnRun.setOnClickListener {
            runCode()
        }

        // Кнопка очистки консоли
        binding.btnClearConsole.setOnClickListener {
            clearConsole()
        }
    }

    private fun saveCode() {
        val newContent = binding.codeEditor.text.toString()
        codeFile = codeFile.copy(content = newContent)

        // TODO: Сохранить изменения в хранилище

        Snackbar.make(binding.root, "Файл сохранен", Snackbar.LENGTH_SHORT).show()
    }

    private fun runCode() {
        val code = binding.codeEditor.text.toString()

        // Показываем в консоли имитацию выполнения
        appendToConsole("> Запуск кода...")
        appendToConsole("> Код выполнен успешно")
        appendToConsole("> Hello, World!")
        appendToConsole("> Программа завершена с кодом 0")
        appendToConsole("") // Пустая строка для разделения

        Snackbar.make(binding.root, "Код выполнен (имитация)", Snackbar.LENGTH_SHORT).show()
    }

    private fun clearConsole() {
        binding.consoleOutput.text = "> Консоль очищена\n> Готов к выполнению кода..."
    }

    private fun appendToConsole(text: String) {
        val currentText = binding.consoleOutput.text.toString()
        binding.consoleOutput.text = "$currentText\n$text"

        // Прокручиваем вниз
        binding.consoleOutput.parent?.let { parent ->
            if (parent is View) {
                parent.post {
                    (binding.consoleOutput.parent as? View)?.scrollTo(0, binding.consoleOutput.bottom)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}