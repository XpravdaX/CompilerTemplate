package com.pravdasempai.kodmobil

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.pravdasempai.kodmobil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentTabId: Int = R.id.nav_code
    private val tabOrder = listOf(R.id.nav_code, R.id.nav_profile)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Установка начального фрагмента
        if (savedInstanceState == null) {
            setActiveTab(R.id.nav_code, false)
            replaceFragment(CodeAdd(), false, true)
        }

        // Установка обработчиков кликов для навигации
        binding.navCode.setOnClickListener { onTabClicked(it) }
        binding.navProfile.setOnClickListener { onTabClicked(it) }
    }

    fun onTabClicked(view: View) {
        if (view.id == currentTabId) return

        val newPosition = tabOrder.indexOf(view.id)
        val currentPosition = tabOrder.indexOf(currentTabId)
        val slideToLeft = newPosition > currentPosition

        when (view.id) {
            R.id.nav_code -> {
                setActiveTab(R.id.nav_code, true)
                replaceFragment(CodeAdd(), true, slideToLeft)
            }
            R.id.nav_profile -> {
                setActiveTab(R.id.nav_profile, true)
                replaceFragment(Profil(), true, slideToLeft)
            }
        }
        currentTabId = view.id
    }

    private fun replaceFragment(fragment: Fragment, animate: Boolean, slideToLeft: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()

        if (animate) {
            if (slideToLeft) {
                transaction.setCustomAnimations(
                    R.anim.fragment_slide_in_right,
                    R.anim.fragment_slide_out_left,
                    R.anim.fragment_slide_in_left,
                    R.anim.fragment_slide_out_right
                )
            } else {
                transaction.setCustomAnimations(
                    R.anim.fragment_slide_in_left,
                    R.anim.fragment_slide_out_right,
                    R.anim.fragment_slide_in_right,
                    R.anim.fragment_slide_out_left
                )
            }
        }

        transaction.replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setActiveTab(activeTabId: Int, animate: Boolean) {
        val navCodeIcon = binding.navCode.findViewById<android.widget.ImageView>(R.id.image)
        val navProfileIcon = binding.navProfile.findViewById<android.widget.ImageView>(R.id.imageView)

        val navCodeText = binding.navCode.findViewById<android.widget.TextView>(R.id.nav_code_text)
        val navProfileText = binding.navProfile.findViewById<android.widget.TextView>(R.id.nav_profile_text)

        // Сбрасываем все иконки к неактивному состоянию
        listOf(navCodeIcon, navProfileIcon).forEach {
            it.setColorFilter(ContextCompat.getColor(this, R.color.nav_icon_inactive))
        }

        // Сбрасываем все тексты к неактивному состоянию
        listOf(navCodeText, navProfileText).forEach {
            if (animate) {
                it.animate()
                    .alpha(0f)
                    .setDuration(150)
                    .start()
            } else {
                it.alpha = 0f
            }
            it.setTextColor(ContextCompat.getColor(this, R.color.nav_text_inactive))
        }

        // Устанавливаем активную вкладку
        when (activeTabId) {
            R.id.nav_code -> {
                navCodeIcon.setColorFilter(ContextCompat.getColor(this, R.color.nav_icon_active))
                if (animate) {
                    navCodeText.animate()
                        .alpha(1f)
                        .setDuration(150)
                        .start()
                } else {
                    navCodeText.alpha = 1f
                }
                navCodeText.setTextColor(ContextCompat.getColor(this, R.color.nav_text_active))
            }
            R.id.nav_profile -> {
                navProfileIcon.setColorFilter(ContextCompat.getColor(this, R.color.nav_icon_active))
                if (animate) {
                    navProfileText.animate()
                        .alpha(1f)
                        .setDuration(150)
                        .start()
                } else {
                    navProfileText.alpha = 1f
                }
                navProfileText.setTextColor(ContextCompat.getColor(this, R.color.nav_text_active))
            }
        }
    }
}