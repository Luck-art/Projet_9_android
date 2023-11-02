package com.openclassrooms.realestatemanager.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R

class EstateDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.estate_detail_activity)

        val estate_id = intent.getLongExtra("estate_id", -1)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_detail, EstateDetailsFragment.newInstance(
                    estate_id = estate_id,
                )
            )
            .commitAllowingStateLoss()

    }
}