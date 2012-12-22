
package com.internalerror.graphics;

import java.util.Random;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.internalerror.common.Location;
import com.internalerror.common.Matrix;
import com.internalerror.engine.AccelerometerReader;

public class GeneralView extends GLSurfaceView {
	
	private GeneralRenderer mRenderer;
	private AccelerometerReader mSensorListener;
    private float _x = 0;
    private float _y = 0;
	
	public static final float DEFAULT_ROTATION_DEGREE = 10.0f;
	public static final float DEFAULT_SCALE_INCRMENT = 0.2f;
	
	public GeneralView (Context context, AccelerometerReader listen){
		super(context);
		mRenderer = new GeneralRenderer();
		mSensorListener = listen;
		setRenderer(mRenderer);
		setFocusable(true);
	}

    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _x = event.getX();
            _y = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final float xdiff = (_x - event.getX());
            final float ydiff = (_y - event.getY());
            queueEvent(new Runnable() {
                public void run() {
                	mRenderer.setXAngle(mRenderer.getXAngle() - ydiff);
                	mRenderer.setYAngle(mRenderer.getYAngle() - xdiff);
                }
            });
            _x = event.getX();
            _y = event.getY();
        }
        return true;
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
			case KeyEvent.KEYCODE_DPAD_LEFT:
				//clear screen
				mRenderer.clear();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				//add point
				mRenderer.addPoint(new Location( mSensorListener.getPosition() ));
				//Random random = new Random();
				//mRenderer.addPoint(new Location(random.nextFloat(), random.nextFloat(), random.nextFloat()));
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				//zoom in
				mRenderer.setScaleFactor(DEFAULT_SCALE_INCRMENT);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				//zoom out
				mRenderer.setScaleFactor(-DEFAULT_SCALE_INCRMENT);
				break;
			default:
				break;
		}
		return true;
	}
	
	
}
