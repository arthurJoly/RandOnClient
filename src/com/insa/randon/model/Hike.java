package com.insa.randon.model;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Hike implements Parcelable {
	String name;
	List<LatLng> coordinates;
	float distance;
	float positiveDiffHeight;
	float negativeDiffHeight;
	String date;
	String duration = "0 min";
	String id;	
	float averageSpeed;

	public Hike(){
		this.name="";
		this.coordinates = new ArrayList<LatLng>();
		this.distance=0;	
		this.positiveDiffHeight=0;
		this.negativeDiffHeight=0;
		this.id="";
		DateFormat df = DateFormat.getDateInstance();
		this.date = df.format(new Date());
	}
	
	public Hike(String name, String id, String duration, float distance){
		this.name=name;
		this.coordinates = new ArrayList<LatLng>();
		this.distance=distance;	
		this.duration=duration;
		this.positiveDiffHeight=0;
		this.negativeDiffHeight=0;
		this.id=id;
		
		DateFormat df = DateFormat.getDateInstance();
		this.date = df.format(new Date());
	}
	
	public Hike(String name, List<LatLng> coordinates, float distance, String duration, float positiveDiffHeight, float negativeDiffHeight, String date){ 
		this.name=name;
		this.coordinates = coordinates;
		this.distance=distance;	
		this.positiveDiffHeight=positiveDiffHeight;
		this.negativeDiffHeight=negativeDiffHeight;
		this.id="";
		this.duration=duration;
		this.date=date;
		
		//DateFormat df = DateFormat.getDateInstance();
		//this.date = df.format(new Date());
	}
	
	/**
	 * add coordinates to the hike and compute the current distance,
	 * this is not the total distance of the hike (need to close the loop)
	 * @param newPoint point added to the hike
	 */
	public void extendHike(LatLng newPoint){
		this.coordinates.add(newPoint);
		
		int size = this.coordinates.size();
		if(size >= 2)
		{
			float[] results = new float[1];
			Location.distanceBetween(this.coordinates.get(size-2).latitude, this.coordinates.get(size-2).longitude, this.coordinates.get(size-1).latitude, this.coordinates.get(size-1).longitude, results);
			this.distance+=results[0]/1000;
		}
	}

	public String getName(){
		return this.name;
	}
	
	public String getId(){
		return this.id;
	}
	
	public float getDistance(){
		return this.distance;
	}
	
	public String getDuration(){
		return this.duration;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public List<LatLng> getCoordinates(){
		return this.coordinates;
	}
	
	public void setPositiveDiffHeight(float positiveDiffHeight) {
		this.positiveDiffHeight = positiveDiffHeight;
	}

	public void setNegativeDiffHeight(float negativeDiffHeight) {
		this.negativeDiffHeight = negativeDiffHeight;
	}
	
	public float getPositiveDiffHeight() {
		return positiveDiffHeight;
	}

	public float getNegativeDiffHeight() {
		return negativeDiffHeight;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public float getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(float averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	//-----------------------------------------------------------------
	//Parcelable : in order to pass a hike between activities
	public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(this.distance);
        out.writeFloat(this.positiveDiffHeight);
        out.writeFloat(this.negativeDiffHeight);
        out.writeFloat(this.averageSpeed);
        out.writeString(this.name);
        out.writeString(this.duration);
        out.writeString(this.date);
        out.writeList(this.coordinates);
    }

    // this is used to regenerate your object
    public static final Parcelable.Creator<Hike> CREATOR = new Parcelable.Creator<Hike>() {
        public Hike createFromParcel(Parcel in) {
            return new Hike(in);
        }

        public Hike[] newArray(int size) {
            return new Hike[size];
        }
    };
    
    private Hike(Parcel in) {
    	this.distance = in.readFloat();
    	this.positiveDiffHeight = in.readFloat();
    	this.negativeDiffHeight = in.readFloat();
    	this.averageSpeed = in.readFloat();
    	this.name = in.readString();
    	this.duration = in.readString();
    	this.date = in.readString();    	
    	this.coordinates = new ArrayList<LatLng>();
      	in.readList(this.coordinates, LatLng.class.getClassLoader());
    }
	
	
}
