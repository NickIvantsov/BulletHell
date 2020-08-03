package com.gmail.rewhelldev.app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import com.gmail.rewhelldev.R


class Bob(context: Context, screenX: Float, screenY: Float) {
    var mRect: RectF = RectF()
        private set
    private var mBobHeight = 0f
    private var mBobWidth = 0f
    private var mTeleporting = false
    var mBitmap: Bitmap? = null
        private set

    init {
        mBobHeight = screenY / 10
        mBobWidth = mBobHeight / 2
        mRect = RectF(
            screenX / 2,
            screenY / 2,
            screenX / 2 + mBobWidth,
            screenY / 2 + mBobHeight
        )
// Prepare the bitmap
// Load Bob from his .png file
// Bob practices responsible encapsulation
// looking after his own resources
// Prepare the bitmap
// Load Bob from his .png file
// Bob practices responsible encapsulation
// looking after his own resources
        mBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bob1)
    }

    fun teleport(newX: Float, newY: Float): Boolean {
// Did Bob manage to teleport?
        var success = false
// Move Bob to the new position
// If not already teleporting
        if (!mTeleporting) {// Make him roughly central to the touch
            mRect.left = newX - mBobWidth / 2
            mRect.top = newY - mBobHeight / 2
            mRect.bottom = mRect.top + mBobHeight
            mRect.right = mRect.left + mBobWidth
            mTeleporting = true
// Notify BulletHellGame that teleport
// attempt was successful
            success = true
        }
        return success
    }

    fun setTelePortAvailable() {
        mTeleporting = false
    }
}