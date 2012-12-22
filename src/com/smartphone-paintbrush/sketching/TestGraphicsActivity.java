package com.internalerror.sketching;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.internalerror.engine.AccelerometerReader;
import com.internalerror.graphics.*;

public class TestGraphicsActivity extends Activity {

	private GLSurfaceView glView;
	private SensorManager mSensorManager = null;
	private AccelerometerReader mSensorListener;
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mSensorListener = new AccelerometerReader(mSensorManager);
	    glView = new GeneralView(this, mSensorListener);
		setContentView(glView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		glView.onPause();
	}

	protected void onResume() {
		super.onResume();
		glView.onResume();
		mSensorManager.registerListener(mSensorListener, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(mSensorListener, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(mSensorListener);
		super.onStop();
	}
	


}
