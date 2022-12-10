package com.dimasfs.storyappsubmission2.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimasfs.storyappsubmission2.R
import com.dimasfs.storyappsubmission2.databinding.ActivityMainBinding
import com.dimasfs.storyappsubmission2.extensions.animateVisibility
import com.dimasfs.storyappsubmission2.paging.LoadingStateAdapter
import com.dimasfs.storyappsubmission2.paging.PagingAdapter
import com.dimasfs.storyappsubmission2.ui.ViewModelFactory
import com.dimasfs.storyappsubmission2.ui.create.CreateActivity
import com.dimasfs.storyappsubmission2.ui.login.LoginActivity
import com.dimasfs.storyappsubmission2.ui.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var factory : ViewModelFactory
    private lateinit var adapter: PagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        adapter = PagingAdapter()

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.setHasFixedSize(true)

        getStories()
        clickCreate()
        binding.refresh.setOnRefreshListener {
            getStories()
            adapter.addLoadStateListener { loadStates ->
                binding.rvStories.animateVisibility(true)
                binding?.refresh?.isRefreshing = loadStates.source.refresh is LoadState.Loading

            }
        }

    }

    private fun getStories() {
        mainViewModel.getUser().observe(this){
            if (it.isLogin) {
                binding.rvStories.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter{
                        adapter.retry()
                    }
                )
                mainViewModel.getStories().observe(this){
                    adapter.submitData(lifecycle, it)
                }
            }
            else {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.fiture_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                mainViewModel.logout()
                Toast.makeText(this, getString(R.string.you_has_been_logout), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clickCreate() {
        binding.actionCreate.setOnClickListener {
            Intent(this@MainActivity, CreateActivity::class.java).also { intent ->
                Toast.makeText(this, getString(R.string.you_can), Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}