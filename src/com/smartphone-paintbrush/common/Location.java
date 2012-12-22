package com.internalerror.common;

public class Location 
{	
	//data fields
	private final float x;
	private final float y;
	private final float z;
	
	//constructor populates all fields
	public Location (float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location ( float[] vec ) 
	{
		this.x = vec[0];
		this.y = vec[1];
		this.z = vec[2];
	}
	
	//accessors
	public float getX(){return x;}
	public float getY(){return y;}
	public float getZ(){return z;}
	
	boolean equals(Location loc){
		if (loc.getX() == this.x && loc.getY() == this.y && loc.getZ() == this.z){
			return true;
		}
		return false;
	}
	
	public Location scale(float factor) {
		//TODO: IMPLEMENT
		return new Location(new float[3]);
	}
}
