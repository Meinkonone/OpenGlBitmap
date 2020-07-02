package com.konone.openglbitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var mGlSurfaceView: GLSurfaceView? = null
    var mShowBitmap: Bitmap? = null
    var mRenderThread: HandlerThread = HandlerThread("gl render thread")
    var mRenderCount: Int = 0

    companion object {
        const val RENDER_MESSAGE: Int = 10010
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mGlSurfaceView = findViewById(R.id.glSurfaceView)
        //decodeBitmap
        mShowBitmap = BitmapFactory.decodeResource(resources, R.drawable.cat)
        mGlSurfaceView?.setEGLContextClientVersion(2)
        mGlSurfaceView?.setRenderer(BitmapRender(this, mShowBitmap!!))
        mGlSurfaceView?.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        mRenderThread.start()
        //bitmap 模拟运镜
        var mHandler = object : Handler(mRenderThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == RENDER_MESSAGE) {
                    startRender()
                    mRenderCount++
                    if (mRenderCount < 10) {
                        sendEmptyMessageDelayed(RENDER_MESSAGE, 30)
                    }
                }
            }
        }
        mHandler.sendEmptyMessage(RENDER_MESSAGE)
    }

    private fun startRender() {
        mGlSurfaceView?.requestRender()
    }
}
