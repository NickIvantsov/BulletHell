package com.gmail.rewhelldev.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import timber.log.Timber


class BulletHellGame(context: Context, x: Int, y: Int) : SurfaceView(context), Runnable {
    // Are we currently debugging
    var mDebugging = true

    // Objects for the game loop/thread
    private var mGameThread: Thread? = null

    @Volatile
    private var mPlaying = false
    private var mPaused = true

    // Objects for drawing
    private val mOurHolder: SurfaceHolder = holder
    private var mCanvas: Canvas? = null
    private val mPaint: Paint = Paint()

    // Keep track of the frame rate
    private var mFPS: Long = 0

    // The number of milliseconds in a second
    private val MILLIS_IN_SECOND = 1000

    // Holds the resolution of the screen
    private val mScreenX = x
    private val mScreenY = y

    // How big will the text be?
    // Font is 5% of screen width
    private val mFontSize = mScreenX / 20

    // Margin is 2% of screen width
    private val mFontMargin = mScreenX / 50

    init {
        startGame()
    }

    // Called to start a new game
    fun startGame() {}

    // Spawns ANOTHER bullet
    private fun spawnBullet() {}
    override fun run() {
        while (mPlaying) {
            val frameStartTime = System.currentTimeMillis()
            if (!mPaused) {
                update()
                // Now all the bullets have been moved
// we can detect any collisions
                detectCollisions()
            }
            draw()
            val timeThisFrame = (System.currentTimeMillis()
                    - frameStartTime)
            if (timeThisFrame >= 1) {
                mFPS = MILLIS_IN_SECOND / timeThisFrame
            }
        }
    }

    // Update all the game objects
    private fun update() {}

    private fun detectCollisions() {}
    private fun draw() {
        if (mOurHolder.surface.isValid) {
            mCanvas = mOurHolder.lockCanvas()
            mCanvas?.drawColor(Color.argb(255, 243, 111, 36))
            mPaint.color = Color.argb(255, 255, 255, 255)
// All the drawing code will go here
            if (mDebugging) {
                printDebuggingText()
            }
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    fun pause() {
        mPlaying = false
        try {
            mGameThread?.join()
        } catch (e: InterruptedException) {
            Timber.e("Error: %s", "joining thread")
        }
    }

    fun resume() {
        mPlaying = true
        mGameThread = Thread(this)
        mGameThread?.start()
    }

    private fun printDebuggingText() {
        val debugSize = 35
        val debugStart = 150
        mPaint.textSize = debugSize.toFloat()
        mCanvas!!.drawText("FPS: $mFPS", 10f, debugStart + debugSize.toFloat(), mPaint)
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        return true
    }

}