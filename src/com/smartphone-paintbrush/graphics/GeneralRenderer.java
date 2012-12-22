package com.internalerror.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.internalerror.common.Location;

public class GeneralRenderer implements Renderer {

	private float bgRed = 0.0f;
	private float bgGreen = 0.0f;
	private float bgBlue = 0.0f;
	private float bgAlpha = 0.0f;

	private float drawRed = 1.0f;
	private float drawGreen = 1.0f;
	private float drawBlue = 1.0f;
	private float drawAlpha = 1.0f;
	
	private Lines mLines;
	
    private float rotationAngleX = GeneralView.DEFAULT_ROTATION_DEGREE;
    private float rotationAngleY = GeneralView.DEFAULT_ROTATION_DEGREE;
	private float rotationAngleZ;
	
	/* Called to draw the current frame*/
	public void onDrawFrame(GL10 gl) {
		// reset the matrix - good to fix the rotation to a static angle
		gl.glLoadIdentity();
		
		//sets the color of the background
		gl.glClearColor(bgRed, bgGreen, bgBlue, bgAlpha);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT |GL10.GL_DEPTH_BUFFER_BIT);
		
		//set rotation
		gl.glRotatef(rotationAngleX, 1f, 0f, 0f);
		gl.glRotatef(rotationAngleY, 0f, 1f, 0f);
		gl.glRotatef(rotationAngleZ, 0f, 1f, 1f);
		
		//sets the color of things we want to draw
		gl.glColor4f(drawRed, drawGreen, drawBlue, drawAlpha);
		
		mLines.draw(gl);
	}

	
	/* Called when surface changes size, eg. switching between landscape/portrait. */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current viewport to the new size
		gl.glViewport(0,0,width,height);		
	}
	
	/*Called when the surface is first created or recreated.*/
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//Enable smooth shading
		gl.glShadeModel(GL10.GL_SMOOTH);
		//Depth buffer setup
		gl.glClearDepthf(1.0f);
		//Turn on depth testing
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		mLines = new Lines();
	}
	
	public void addPoint(Location point){
		mLines.addPoint(point);
		Log.d("GeneralRenderer", "Adding point: " + point.getX() + "\t" + point.getY() + "\t" + point.getZ());
	}
	
	public void setBackgroundColor(float r, float g, float b, float a){
		bgRed = r;
		bgGreen = g;
		bgBlue = b;
		bgAlpha = a;
	}
	
	public void setDrawColor(float r, float g, float b, float a){
		drawRed = r;
		drawGreen = g;
		drawBlue = b;
		drawAlpha = a;
	}
	
    public void setXAngle(float angle) {
    	rotationAngleX = angle;
    }
    
    public float getXAngle() {
        return rotationAngleX;
    }
    
    public void setYAngle(float angle) {
    	rotationAngleY = angle;
    }
    
    public float getYAngle() {
        return rotationAngleY;
    }
    
    public void setZAngle(float angle) {
    	rotationAngleZ = angle;
    }
    
    public float getZAngle() {
        return rotationAngleZ;
    }
    
    public void clear() {
    	mLines.clear();
    	rotationAngleX = 0;
    	rotationAngleY = 0;
    	rotationAngleZ = 0;
    }
    
    public void setScaleFactor(float factor) {
    	mLines.setScaleFactor(factor);
    	mLines.init();
    }
}
