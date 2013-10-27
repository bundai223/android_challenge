package com.example.challenge;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by daiji on 2013/10/23.
 */
public final class GLRenderer implements GLSurfaceView.Renderer {


    private final Context mContext;

    // 
    public GLRenderer(Context context) {
        mContext = context;
    }

    //  
    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {

    }

    //
    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    // 
    @Override
    public void onDrawFrame(final GL10 gl) {

        GLES20.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
