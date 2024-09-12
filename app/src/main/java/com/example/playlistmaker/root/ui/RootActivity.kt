package com.example.playlistmaker.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.media.ui.MediaFragment
import com.example.playlistmaker.search.ui.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, destination,_ ->
            when (destination.id) {
                R.id.playlistFragment -> {binding.bottomNavigationView.isVisible = false}
                R.id.playerFragment -> {binding.bottomNavigationView.isVisible = false}
                R.id.playlistItemFragment -> {binding.bottomNavigationView.isVisible = false}
                R.id.editPlaylistFragment -> {binding.bottomNavigationView.isVisible = false}
                else -> {binding.bottomNavigationView.isVisible = true}
            }
        }
    }

}