package com.gmail.rewhelldev.app

import android.graphics.RectF
import timber.log.Timber


class Bullet(screenX: Int) {
    // A RectF to represent the size and location of the bullet
    val mRect: RectF = RectF()

    // How fast is the bullet traveling?
    private var mXVelocity = 0f
    private var mYVelocity = 0f

    // How big is a bullet
    private var mWidth = 0f
    private var mHeight = 0f

    init {
        // Configure the bullet based on
// the screen width in pixels

        // Configure the bullet based on
// the screen width in pixels
        mWidth = (screenX / 100).toFloat()
        mHeight = (screenX / 100).toFloat()

        mYVelocity = (screenX / 5).toFloat()
        mXVelocity = (screenX / 5).toFloat()
    }

    // Move the bullet based on the speed and the frame rate
    fun update(fps: Long) {
// Update the left and top coordinates
// based on the velocity and current frame rate
        mRect.left = mRect.left + mXVelocity / fps
        mRect.top = mRect.top + mYVelocity / fps
        mRect.right = mRect.left + mWidth
        mRect.bottom = mRect.top - mHeight
    }

    // Reverse the vertical direction of travel
    fun reverseYVelocity() {
        mYVelocity = -mYVelocity
        if (DEBUGGING) {
            Timber.i("reverseYVelocity() mYVelocity = $mYVelocity")
        }
    }

    // Reverse the horizontal direction of travel
    fun reverseXVelocity() {
        mXVelocity = -mXVelocity
        if (DEBUGGING)
            Timber.i("reverseXVelocity() mXVelocity = $mXVelocity")
    }


    // Spawn a new bullet
    // Создаем новую пулю
    fun spawn(pX: Int, pY: Int, vX: Int, vY: Int) {
// Spawn the bullet at the location
// passed in as parameters
        mRect.left = pX.toFloat()
        mRect.top = pY.toFloat()
        mRect.right = pX + mWidth
        mRect.bottom = pY + mHeight
        mXVelocity = mXVelocity * vX
        mYVelocity = mYVelocity * vY
    }


    companion object {
        private const val DEBUGGING = true
    }
}

