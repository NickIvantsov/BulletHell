package com.gmail.rewhelldev.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import timber.log.Timber
import java.util.*


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


    // Up to 10000 bullets
    private val mBullets = arrayOfNulls<Bullet>(10_000)
    private var mNumBullets = 0
    private val mSpawnRate = 1
    private val mRandomX: Random = Random()
    private val mRandomY: Random = Random()

    private var mBob: Bob? = null
    var mHit = false
    var mNumHits = 0
    var mShield = 10

    // Let's time the game
    private var mStartGameTime: Long = 0
    private var mBestGameTime: Long = 0
    private var mTotalGameTime: Long = 0

    init {
        for (i in mBullets.indices) {
            mBullets[i] = Bullet(mScreenX)
        }

        mBob = Bob(context, mScreenX.toFloat(), mScreenY.toFloat())
        startGame()
    }

    // Called to start a new game
    fun startGame() {
        mNumHits = 0
        mNumBullets = 0
        mHit = false
// Did the player survive longer than previously
        if (mTotalGameTime > mBestGameTime) {
            mBestGameTime = mTotalGameTime
        }
    }

    // Spawns ANOTHER bullet
    private fun spawnBullet() {
        // Add one to the number of bullets
        mNumBullets++
// Where to spawn the next bullet
// And in which direction should it travel
        var spawnX: Int?
        var spawnY: Int?
        var velocityX: Int?
        var velocityY: Int?
// This code will change in chapter 13
        // Don't spawn to close to Bob
        if (mBob!!.mRect.centerX()
            < mScreenX / 2
        ) {
// Bob is on the left
// Spawn bullet on the right
            spawnX = mRandomX.nextInt(mScreenX / 2) + mScreenX / 2
// Head right
            velocityX = 1
        } else {
// Bob is on the right
// Spawn bullet on the left
            spawnX = mRandomX.nextInt(mScreenX / 2)
// Head left
            velocityX = -1
        }
        // Don't spawn to close to Bob
        if (mBob!!.mRect.centerY()
            < mScreenY / 2
        ) {

            // Bob is on the top
// Spawn bullet on the bottom
            spawnY = mRandomY.nextInt(mScreenY / 2) + mScreenY / 2
// Head down
            velocityY = 1
        } else {
// Bob is on the bottom
// Spawn bullet on the top
            spawnY = mRandomY.nextInt(mScreenY / 2)
// head up
            velocityY = -1
        }
/*// Pick a random point on the screen
// to spawn a bullet
        spawnX = mRandomX.nextInt(mScreenX)
        spawnY = mRandomY.nextInt(mScreenY)
// The horizontal direction of travel
        velocityX = 1
// Randomly make velocityX negative
        if (mRandomX.nextInt(2) == 0) {
            velocityX = -1
        }
        velocityY = 1
// Randomly make velocityY negative
        if (mRandomY.nextInt(2) == 0) {
            velocityY = -1
        }*/
// Spawn the bullet
        mBullets[mNumBullets - 1]?.spawn(spawnX, spawnY, velocityX, velocityY)

    }

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
    private fun update() {
        for (i in 0 until mNumBullets) {
            mBullets[i]?.update(mFPS)
        }
    }

    private fun detectCollisions() {
        // Has a bullet collided with a wall?
// Loop through each active bullet in turn
        for (i in 0 until mNumBullets) {
            if (mBullets[i]?.mRect!!.bottom > mScreenY) {
                mBullets[i]!!.reverseYVelocity()
            } else if (mBullets[i]!!.mRect.top < 0) {
                mBullets[i]!!.reverseYVelocity()
            } else if (mBullets[i]!!.mRect.left < 0) {
                mBullets[i]!!.reverseXVelocity()
            } else if (mBullets[i]!!.mRect.right > mScreenX) {
                mBullets[i]!!.reverseXVelocity()
            }
        }
        for (i in 0 until mNumBullets) {
            if (RectF.intersects(mBullets[i]!!.mRect, mBob!!.mRect)) {
// This flags that a hit occurred
// so that the draw
// method "knows" as well
                mHit = true
// Rebound the bullet that collided
                mBullets[i]!!.reverseXVelocity()
                mBullets[i]!!.reverseYVelocity()
// keep track of the number of hits
                mNumHits++
                if (mNumHits == mShield) {
                    mPaused = true
                    mTotalGameTime = System.currentTimeMillis()-mStartGameTime
                    startGame()
                }
            }
        }
    }

    private fun draw() {
        if (mOurHolder.surface.isValid) {
            mCanvas = mOurHolder.lockCanvas()
            mCanvas?.drawColor(Color.argb(255, 243, 111, 36))
            mPaint.color = Color.argb(255, 255, 255, 255)
// All the drawing code will go here

// All the drawing code will go here

            // All the drawing code will go here
            for (i in 0 until mNumBullets) {
                mCanvas?.drawRect(mBullets[i]!!.mRect, mPaint)
            }
            mCanvas?.drawBitmap(mBob!!.mBitmap!!, mBob!!.mRect.left, mBob!!.mRect.top, mPaint)
            mPaint.textSize = mFontSize.toFloat()
            mCanvas?.drawText(
                "Bullets: " + mNumBullets +
                        " Shield: " + (mShield - mNumHits) +
                        " Best Time: " + mBestGameTime / MILLIS_IN_SECOND, mFontMargin.toFloat(), mFontSize.toFloat(), mPaint
            )

            // Don't draw the current time when paused
            if (!mPaused) {
                mCanvas?.drawText("Seconds Survived: " + ((System.currentTimeMillis() - mStartGameTime) / MILLIS_IN_SECOND), mFontMargin.toFloat(), (mScreenY /1.05).toFloat(), mPaint)

            }
            if (mDebugging) {
                printDebuggingText()
            }
            mOurHolder.unlockCanvasAndPost(mCanvas)
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
        mCanvas?.drawText("FPS: $mFPS", 10f, debugStart + debugSize.toFloat(), mPaint)
        mCanvas?.drawText(
            "Bob left: " + mBob?.mRect?.left,
            10F,
            (debugStart + debugSize * 2).toFloat(),
            mPaint
        )
        mCanvas?.drawText(
            "Bob top: " + mBob?.mRect?.top,
            10F,
            (debugStart + debugSize * 3).toFloat(),
            mPaint
        )
        mCanvas?.drawText(
            "Bob right: " + mBob?.mRect?.right,
            10F,
            (debugStart + debugSize * 4).toFloat(),
            mPaint
        )
        mCanvas?.drawText(
            "Bob bottom: " + mBob?.mRect?.bottom,
            10F,
            (debugStart + debugSize * 5).toFloat(),
            mPaint
        )
        mCanvas?.drawText(
            "Bob centerX: " + mBob?.mRect?.centerX(),
            10F,
            (debugStart + debugSize * 6).toFloat(),
            mPaint
        )
        mCanvas?.drawText(
            "Bob centerY: " + mBob?.mRect?.centerY(),
            10F,
            (debugStart + debugSize * 7).toFloat(),
            mPaint
        )
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        when (motionEvent!!.action and
                MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                if (mPaused) {
                    mStartGameTime = System.currentTimeMillis()
                    mPaused = false
                }
                if (mBob!!.teleport(
                        motionEvent.x,
                        motionEvent.y
                    )
                ) {
                    /*mSP.play(mTeleportID, 1, 1, 0, 0, 1)*/
                }
            }
            MotionEvent.ACTION_UP -> {
                mBob!!.setTelePortAvailable()
                spawnBullet()
            }
        }
        return true
    }

}