package com.example.omnihub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setting layout
        setContentView(R.layout.activity_main)

        //variables
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Sirf bottom padding dein taake icons system bar se upar aa jayein
            view.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        // Edge-to-Edge display enable karein
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()
        hideSystemUI()



        // 1. Default Fragment (App khulte hi Weather dikhega)
        if (savedInstanceState == null) {
            loadFragment(WeatherFragment())
            bottomNav.selectedItemId = R.id.nav_weather
        }


        // 2. Navigation Click Listener
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_weather -> loadFragment(WeatherFragment())
                R.id.nav_news -> loadFragment(newsFragment())
                R.id.nav_contacts -> loadFragment(ContactsFragment())
                else -> false
            }
            true
        }



    }



    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) // Sleek transition
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    //hide system navbar
    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // System bars (status bar aur navigation bar) ko hide karein
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        // Swipe karne par buttons wapas ayein aur phir khud hide ho jayein
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}