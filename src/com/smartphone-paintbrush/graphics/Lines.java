package com.internalerror.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.internalerror.common.Location;

public class Lines {
	
	private List<Location> points = new ArrayList<Location>();
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer colorBuffer;
	private float scaleFactor =1.0f;
	
	
	
	Random random;
	public Lines(){
		random = new Random();
		loadPyramid();
		//loadCube();
	}
	
	public synchronized void addPoint(Location point){
		points.add(point);
		init();
	}
	
	public synchronized void init(){
		short[] indices = new short[points.size()];
		float[] vertices = new float[points.size()*3];
		float[] colors = new float[points.size()*4];
		
		for (int i=0; i<points.size(); i++){
			indices[i] = (short) i;
			Location point = points.get(i);
			vertices[i*3] = point.getX()*scaleFactor;
			vertices[(i*3)+1] = point.getY()*scaleFactor;
			vertices[(i*3)+2] = point.getZ()*scaleFactor;
			colors[i*4] = random.nextFloat();
			colors[(i*4)+1] = random.nextFloat();
			colors[(i*4)+2] = random.nextFloat();
			colors[(i*4)+3] = random.nextFloat();
		}
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length*2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
	}
	
	public synchronized void draw(GL10 gl){
		gl.glLineWidth(5f);
		gl.glEnable(GL10.GL_POINT_SMOOTH);
        
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glDrawElements(GL10.GL_LINE_STRIP, points.size(), GL10.GL_UNSIGNED_SHORT, indexBuffer);
	}

	/*Test method that generates a pyramid*/
	private void loadPyramid(){
        addPoint(new Location(-0.5f, -0.5f, 0.5f));
        addPoint(new Location(0.5f, -0.5f, 0.5f));
        addPoint(new Location(0f, 0.5f, 0f));
        addPoint(new Location(-0.5f, -0.5f, 0.5f));
        addPoint(new Location(0f, 0.5f, 0f));
        addPoint(new Location(0f, -0.5f, -0.5f));
        addPoint(new Location(0.5f, -0.5f, 0.5f));
        addPoint(new Location(0f, 0.5f, 0f));
        addPoint(new Location(-0.5f, -0.5f, 0.5f));
        addPoint(new Location(0f, -0.5f, -0.5f));
	}
	
	/*Test method that generates a cube*/
	private void loadCube(){
        addPoint(new Location(-0.5f, -0.5f, -0.5f));
        addPoint(new Location(0.5f, -0.5f, -0.5f));
        addPoint(new Location(0.5f, 0.5f, -0.5f));
        addPoint(new Location(-0.5f, 0.5f, -0.5f));
        addPoint(new Location(-0.5f, -0.5f, -0.5f));
        addPoint(new Location(-0.5f, -0.5f, 0.5f));
        addPoint(new Location(-0.5f, 0.5f, 0.5f));
        addPoint(new Location(-0.5f, 0.5f, -0.5f));
        addPoint(new Location(-0.5f, -0.5f, -0.5f));
        addPoint(new Location(-0.5f, -0.5f, 0.5f));
        addPoint(new Location(0.5f, -0.5f, 0.5f));
        addPoint(new Location(0.5f, 0.5f, 0.5f));
        addPoint(new Location(-0.5f, 0.5f, 0.5f));
        addPoint(new Location(-0.5f, -0.5f, 0.5f));
	}

	public synchronized void clear(){
		points.clear();
	}
	
	public void setScaleFactor(float factor){
		scaleFactor += factor;
	}
	
	public synchronized void scale(float factor) {
		
	}
}


