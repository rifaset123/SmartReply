package com.dicoding.smartreply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val chatFragment = ChatFragment()
        val fragment = fragmentManager.findFragmentByTag(ChatFragment::class.java.simpleName)

        if (fragment !is ChatFragment) {
            Log.d("MyChatFragment", "Fragment Name :" + ChatFragment::class.java.simpleName)
            fragmentManager
                .beginTransaction()
                .add(R.id.container, chatFragment, ChatFragment::class.java.simpleName)
                .commit()
        }

    }
}