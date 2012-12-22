package com.internalerror.engine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.internalerror.common.Matrix;

public class AccelerometerReader extends Activity implements SensorEventListener {
	private static final String TAG = "AccelerometerReader";
	private static final float ACCEL_DEADBAND = 0.1f;
	private static final float ACCEL_MIN_DELTA = 0.2f;
	private static final float VEL_DEADBAND = 5e-4f;
	private static final float VEL_SCALE = 0.9f;
	private static final int LOG_INTERVAL = 100;
	
	private static final int SAMPLE_SIZE = 16;
	private SensorManager mSensorManager;
	private LinkedList<float[]> mAccelSamples;
	private LinkedList<float[]> mMagnetSamples;
	
	private float[] mAccelVec;
	private float[] mMagnetVec;
	private float[] mRotationArr;
	private float[][] mRotationMat;
	private float[] mOffsetVec;
	private float[] mTemp;
	
	private float[] mVelVec;
	private float[] mPosVec;
	
	private int logCount = 0;
	
	private long prevTime = 0;
	
	public AccelerometerReader(SensorManager sensorManager) {
		mSensorManager = sensorManager;
		mSensorManager.registerListener(this,
										mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
										SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this,
										mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
										SensorManager.SENSOR_DELAY_FASTEST);
		mAccelSamples = new LinkedList<float[]>();
		mMagnetSamples = new LinkedList<float[]>();
		
		mAccelVec = new float[3];
		mVelVec = new float[3];
		mPosVec = new float[3];
		mMagnetVec = new float[3];
		mRotationArr = new float[9];
		mRotationMat = new float[3][3];
		mOffsetVec = new float[3];
//		mOffsetVec[2] = SensorManager.GRAVITY_EARTH;
		mTemp = new float[3];
	}
	
	/**
	 * Returns position as array of floats.
	 * @return position as array of floats.
	 */
	public synchronized float[] getPosition() {
		return mPosVec;
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// do nothing
	}

	public synchronized void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] sample;
			if (mAccelSamples.size() == SAMPLE_SIZE) {
				sample = mAccelSamples.removeFirst();
			}
			else if (mAccelSamples.size() == SAMPLE_SIZE - 1) {
				sample = new float[3];
				
				ListIterator<float[]> it;
				it = mAccelSamples.listIterator();
				while (it.hasNext()) {
					sample = it.next();
					mOffsetVec[0] += sample[0];
					mOffsetVec[1] += sample[1];
					mOffsetVec[2] += sample[2];
				}
				mOffsetVec[0] /= SAMPLE_SIZE - 1;
				mOffsetVec[1] /= SAMPLE_SIZE - 1;
				mOffsetVec[2] /= SAMPLE_SIZE - 1;
			}
			else {
				sample = new float[3];
			}
			
			System.arraycopy(event.values, 0, sample, 0, 3);
			mAccelSamples.addLast(sample);
			
			
			if(mAccelSamples.size() == SAMPLE_SIZE) {
				deltaFilter(mAccelSamples.getLast(), mAccelSamples.get(SAMPLE_SIZE-2));
				updateVecs();
				long currTime = event.timestamp;
				long timePeriod;
				if(prevTime == 0) timePeriod = 1;
				else timePeriod = currTime - prevTime;
				prevTime = currTime;
				
				Matrix.subFrom(mAccelVec, mOffsetVec);
				
				deadband(mAccelVec, ACCEL_DEADBAND);
				
				Matrix.multBy(mAccelVec, timePeriod / 1e9f);
				Matrix.addTo(mVelVec, mAccelVec);
				Matrix.multBy(mAccelVec, 1e9f / timePeriod);
				
				Matrix.multBy(mVelVec, timePeriod / 1e9f);
				Matrix.addTo(mPosVec, mVelVec);
				Matrix.multBy(mVelVec, 1e9f / timePeriod);
				Matrix.multBy(mVelVec, VEL_SCALE);
				
				deadband(mVelVec, VEL_DEADBAND);
				
				//Log.d(TAG, Matrix.vecToString(mAccelVec) + Matrix.vecToString(mVelVec) + Matrix.vecToString(mPosVec));
				Log.d(TAG, Matrix.vecToString(mPosVec));
				if(++logCount == LOG_INTERVAL) {
//					Log.d(TAG, "accelVec: " + Matrix.vecToString(mAccelVec));
//					Log.d(TAG, "velVec: " + Matrix.vecToString(mVelVec));
//					Log.d(TAG, "posVec: " + Matrix.vecToString(mPosVec));
//					Log.d(TAG, "timePeriod: " + timePeriod);
					logCount = 0;
				}
			}
			
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			float[] sample;
			if (mMagnetSamples.size() == SAMPLE_SIZE) {
				sample = mMagnetSamples.removeFirst();
			} else {
				sample = new float[3];
			}
			System.arraycopy(event.values, 0, sample, 0, 3);
			mMagnetSamples.addLast(sample);
		}
	}
	
	private synchronized void updateVecs() {
		float[] sample;
		ListIterator<float[]> it;
		
		Arrays.fill(mAccelVec, 0);
		it = mAccelSamples.listIterator();
		while (it.hasNext()) {
			sample = it.next();
			mAccelVec[0] += sample[0];
			mAccelVec[1] += sample[1];
			mAccelVec[2] += sample[2];
		}
		mAccelVec[0] /= SAMPLE_SIZE;
		mAccelVec[1] /= SAMPLE_SIZE;
		mAccelVec[2] /= SAMPLE_SIZE;
		
		Arrays.fill(mMagnetVec, 0);
		it = mMagnetSamples.listIterator();
		while (it.hasNext()) {
			sample = it.next();
			mMagnetVec[0] += sample[0];
			mMagnetVec[1] += sample[1];
			mMagnetVec[2] += sample[2];
		}
		mMagnetVec[0] /= SAMPLE_SIZE;
		mMagnetVec[1] /= SAMPLE_SIZE;
		mMagnetVec[2] /= SAMPLE_SIZE;
	}
	
	private void deadband(float[] vec, float db) {
		for (int i = 0; i < vec.length; i++) {
			if (Math.abs(vec[i]) < db) vec[i] = 0;
		}
	}
	
	private void deltaFilter(float[] newData, float[] oldData) {
		if (newData.length != oldData.length) throw new IllegalArgumentException("deltaFilter: Vector length mismatch");
		for ( int i = 0; i < newData.length; i++ ) {
			if( Math.abs(newData[i] - oldData[i]) <= ACCEL_MIN_DELTA ) newData[i] = oldData[i];
		}
	}
	
	public synchronized void clear() {
		mAccelSamples = new LinkedList<float[]>();
		mMagnetSamples = new LinkedList<float[]>();
		
		mAccelVec = new float[3];
		mVelVec = new float[3];
		mPosVec = new float[3];
		mMagnetVec = new float[3];
		mRotationArr = new float[9];
		mRotationMat = new float[3][3];
		mOffsetVec = new float[3];
//		mOffsetVec[2] = SensorManager.GRAVITY_EARTH;
		mTemp = new float[3];
	}
}