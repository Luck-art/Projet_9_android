package com.openclassrooms.realestatemanager.estate_manager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.LogInActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.details.EstateDetailsActivity
import com.openclassrooms.realestatemanager.details.EstateDetailsFragment
import com.openclassrooms.realestatemanager.estate_manager.logic.SearchFilter


class RealEstateManagerActivity : AppCompatActivity(), RealEstateManagerFragment.Listener {

    lateinit var burgerMenu: ImageView
    lateinit var drawerLayout: DrawerLayout

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_estate -> {
                getListFragment()?.toggleEditState()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getListFragment() : RealEstateManagerFragment? {
        return supportFragmentManager.findFragmentById(R.id.fragment_container_list) as? RealEstateManagerFragment
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.real_estate_activity)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_list, RealEstateManagerFragment())
            .commitAllowingStateLoss()

        burgerMenu = findViewById(R.id.burger_menu)
        drawerLayout = findViewById(R.id.drawer_layout)

        supportActionBar?.hide()


        val searchIcon: ImageView = findViewById(R.id.search_estate)
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val addButton: ImageView = findViewById(R.id.add_estate)
        val editButton: ImageView = findViewById(R.id.edit_estate)

        addButton.setOnClickListener {
            getListFragment()?.onAddButtonClicked()
        }

        editButton.setOnClickListener {
            getListFragment()?.showSelectEstateDialog()
        }

        val realEstateDao = RealEstateManagerDatabase.getDatabase(this).realEstateDao()


        val searchFilter by lazy {
            SearchFilter(
                context = this,
                realEstateDao = realEstateDao,
                onUpdateUI = { realEstates ->
                    // Mettez à jour votre interface utilisateur ici avec les biens filtrés
                }
            )
        }

        searchIcon.setOnClickListener {
            searchFilter.showFilterDialog()
        }




        burgerMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }


        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_logout -> {
                    logout()
                    true
                }

                else -> false
            }
        }
    }


    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut()

        LoginManager.getInstance().logOut()

        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)

        finish()
    }


    override fun onItemClicked(realEstate: RealEstate) {
        val isTablet = findViewById<View>(R.id.fragment_container_detail) != null
        if(isTablet) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_detail, EstateDetailsFragment.newInstance(
                    estate_id = realEstate.id,
                ))
                .commitAllowingStateLoss()

        } else {
            val intent = Intent(this, EstateDetailsActivity::class.java)
            intent.putExtra("estate_id", realEstate.id)
            startActivity(intent)
        }
    }

}
