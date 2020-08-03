package com.gmail.rewhelldev.app

import android.app.Activity
import android.graphics.Point
import android.os.Bundle


class BulletHellActivity : Activity() {
    // An instance of the main class of this project
    private lateinit var mBHGame: BulletHellGame
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the screen resolution
        // Get the screen resolution
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        // Call the constructor(initialize)
// the BulletHellGame instance
        // Call the constructor(initialize)
// the BulletHellGame instance
        mBHGame = BulletHellGame(this, size.x, size.y)
        setContentView(mBHGame)
    }

    // Start the main game thread
    // when the game is launched
    override fun onResume() {
        super.onResume()
        mBHGame.resume()
    }

    // Stop the thread when the player quits
    override fun onPause() {
        super.onPause()
        mBHGame.pause()
    }
}