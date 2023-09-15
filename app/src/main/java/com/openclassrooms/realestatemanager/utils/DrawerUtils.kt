package com.openclassrooms.realestatemanager.utils

import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.openclassrooms.realestatemanager.R

fun AppCompatActivity.setupDrawer(
    drawerLayoutId: Int,
    navigationViewId: Int,
    burgerMenuId: Int
) {
    val drawerLayout: DrawerLayout = findViewById(drawerLayoutId)
    val navigationView: NavigationView = findViewById(navigationViewId)
    val burgerMenu: ImageView = findViewById(burgerMenuId)

    burgerMenu.setOnClickListener {
        drawerLayout.openDrawer(Gravity.RIGHT)
    }

    navigationView.setNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.drawer_logout -> {

            }
            R.id.drawer_settings -> {

            }
            R.id.drawer_my_account-> {

            }
        }
        drawerLayout.closeDrawer(Gravity.RIGHT)
        true
    }
}
