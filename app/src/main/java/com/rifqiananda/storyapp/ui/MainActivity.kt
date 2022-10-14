package com.rifqiananda.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifqiananda.storyapp.R
import com.rifqiananda.storyapp.adapter.LoadingStateAdapter
import com.rifqiananda.storyapp.adapter.StoriesAdapter
import com.rifqiananda.storyapp.adapter.StoryListAdapter
import com.rifqiananda.storyapp.databinding.ActivityMainBinding
import com.rifqiananda.storyapp.helper.Constant
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.model.Story
import com.rifqiananda.storyapp.ui.view.StoriesViewModel
import com.rifqiananda.storyapp.ui.view.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var sharedPref: PreferencesHelper

    lateinit var toggle: ActionBarDrawerToggle

    private val viewModel: StoriesViewModel by viewModels {
        ViewModelFactory(this)
    }

    private lateinit var dataAdapter: StoryListAdapter

    override fun onStart() {
        super.onStart()
        getData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        toggle = ActionBarDrawerToggle(this, binding.mainActivity, R.string.open, R.string.close)
        binding.mainActivity.addDrawerListener(toggle)
        toggle.syncState()

        binding.apply {

            val name = sharedPref.getString(Constant.PREF_NAME)

            val header: View = navView.getHeaderView(0)
            val tvHeader = header.findViewById<TextView>(R.id.tv_name)
            tvHeader.text = name

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.logout -> logoutAccount()
                }
                true
            }

            btnMenu.setOnClickListener {
                mainActivity.openDrawer(GravityCompat.START)
            }

            btnCreate.setOnClickListener {
                startActivity(Intent(this@MainActivity, CreateStoryActivity::class.java))
            }
        }
        setupListStories()
    }

    private fun getData() {
        viewModel.story.observe(this) {
            dataAdapter.submitData(lifecycle, it)
        }

//        val name = sharedPref.getString(Constant.PREF_NAME)
//        val token = sharedPref.getString(Constant.PREF_TOKEN)
//        val bearer = "Bearer $token"
//
//        viewModel.setListStories(bearer)
//        viewModel.getListStories().observe(this@MainActivity) {
//            if (it != null) {
//                dataAdapter.setData(it)
//                dataNotShowUp(false)
//                Log.e("Name", name!!)
//            } else dataNotShowUp(true)
//        }
    }

    private fun setupListStories() {
        dataAdapter = StoryListAdapter(this)
        dataAdapter.setOnItemClick(object : StoryListAdapter.OnAdapterListener {
            override fun onClick(data: Story, option: ActivityOptionsCompat) {
                Intent(applicationContext, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.USER_PHOTO, data.photoUrl)
                    it.putExtra(DetailStoryActivity.USER_NAME, data.name)
                    it.putExtra(DetailStoryActivity.USER_DESCRIPTION, data.description)
                    it.putExtra(DetailStoryActivity.USER_CREATED, data.createdAt)
                    startActivity(it, option.toBundle())
                }
            }
        })

        binding.rvData.adapter = dataAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                dataAdapter.retry()
            }
        )
        binding.rvData.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvData.setHasFixedSize(true)

        viewModel.story.observe(this) {
            dataAdapter.submitData(lifecycle, it)
        }
    }

    private fun logoutAccount() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Sign Out")
            setMessage("Are you sure to sign out?")
            setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Yes") { dialogInterface, i ->
                dialogInterface.dismiss()
                sharedPref.clear()
                showMessage("You have logged out!")
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }.show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dataNotShowUp(state: Boolean) {
        if (state) {
            binding.dataNotFound.visibility = View.VISIBLE
        } else {
            binding.dataNotFound.visibility = View.GONE
        }
    }

}