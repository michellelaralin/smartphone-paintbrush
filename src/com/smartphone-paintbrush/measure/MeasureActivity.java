package com.internalerror.measure;


import com.internalerror.engine.*;

import java.util.*;
import java.lang.Math;

import com.internalerror.R;
import android.view.MotionEvent;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.internalerror.engine.AccelerometerReader;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MeasureActivity extends Activity implements OnTouchListener 
{
	private TextView textView;
	private RadioGroup modeGroup, unitGroup, dimensionGroup;
	private RadioButton pointToPointButton, pathButton, metricButton, imperialButton, distanceButton, areaButton, volumeButton;
	private boolean started = false;
	private double distance = 0;
	private float[] firstCoord = new float[3];
	private float[] secondCoord = new float[3];
	final Handler handler = new Handler();
	private Button button;
	
	SensorManager mSensorManager = null;
	AccelerometerReader accReader = null;
	

	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.test);
	    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    accReader = new AccelerometerReader(mSensorManager);
	    modeGroup = (RadioGroup)this.findViewById(R.id.modeGroup);
	    	pointToPointButton = (RadioButton) this.findViewById(R.id.pointToPointButton);
	    	pathButton = (RadioButton) this.findViewById(R.id.pathButton);
	    unitGroup = (RadioGroup)this.findViewById(R.id.unitGroup);
	    	metricButton = (RadioButton) this.findViewById(R.id.metricButton);
	    	imperialButton = (RadioButton) this.findViewById(R.id.imperialButton);
	    dimensionGroup = (RadioGroup)findViewById(R.id.dimensionGroup);
	    	distanceButton = (RadioButton) findViewById(R.id.distanceButton);
	    	areaButton =(RadioButton) findViewById(R.id.areaButton);
	    	volumeButton = (RadioButton) findViewById(R.id.volumeButton);
	    textView = (TextView) findViewById(R.id.textView);
	    button = (Button) findViewById(R.id.button);
	    button.setOnTouchListener(this);
	    modeGroup.check(pointToPointButton.getId());
	    unitGroup.check(metricButton.getId());
	    dimensionGroup.check(distanceButton.getId());
	}
	
	public boolean onTouch(View view, MotionEvent motionEvent) 
	{
		
		if(motionEvent.getAction()== MotionEvent.ACTION_DOWN)
		{
			if(pathButton.isChecked())
			{
				distance = 0;
			}
			started = true;
			textView.setText("started...");
			firstCoord = accReader.getPosition();
			final int delay = 1000;
			handler.postDelayed(new Runnable(){
				public void run()
				{
					if(started==true)
					{
					secondCoord = accReader.getPosition();
					distance += calculateDistance();
					firstCoord = secondCoord;
					handler.postDelayed(this, delay);
					textView.setText("Distance: " + String.format("%2.2f", distance));
					}
				}
			}, delay);
		}
		
		else
		{
			started = false;
			if(pointToPointButton.isChecked())
			{
				textView.setText("Total Distance Till now =  " + String.format("%2.2f", distance ));
			}
		}
		return true;
	}
	
	private double calculateDistance()
	{
		double distance;
		distance = Math.sqrt(Math.abs(Math.pow(firstCoord[0]-secondCoord[0],2)) + Math.abs(Math.pow(firstCoord[1]-secondCoord[1],2)) + Math.abs(Math.pow(firstCoord[2]-secondCoord[2],2)));
		return distance;
	}
	
	public double[] getCoordinates()
	{
		double[] coord = new double[3];
		Random generator = new Random(new Date().getTime());
		coord[0] = generator.nextDouble()*10;
		coord[1] = generator.nextDouble()*15;
		coord[2] = generator.nextDouble()*85;
		return coord;
	}
	
	/**
	 * Registers listeners for accelerometer and magnetic sensor
	 */

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(accReader, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(accReader);
		super.onStop();
	}

}
