package com.insa.randon.model;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Hike implements Parcelable {
	String name;
	List<LatLng> coordinates;
	float distance;
	float diffHeight;
	
	public Hike(){
		this.name="";
		this.coordinates = new ArrayList<LatLng>();
		this.distance=0;	
		this.diffHeight=0;
	}
	
	/**
	 * add coordinates to the hike and compute the current distance,
	 * this is not the total distance of the hike (need to close the loop)
	 * @param newPoint point added to the hike
	 */
	public void extendHike(LatLng newPoint){
		this.coordinates.add(newPoint);
		
		int size = this.coordinates.size();
		if(size>2)
		{
			float[] results = new float[1];
			Location.distanceBetween(this.coordinates.get(size-2).latitude, this.coordinates.get(size-2).longitude, this.coordinates.get(size-1).latitude, this.coordinates.get(size-1).longitude, results);
			this.distance+=results[0];
		}
	}
	
	public float getDistance(){
		return this.distance;
	}
	
	public List<LatLng> getCoordinates(){
		return this.coordinates;
	}
	
	//-----------------------------------------------------------------
	//Parcelable : in order to pass a hike between activities
	public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
    	//TODO : write the coordinates
        out.writeFloat(this.distance);
        out.writeFloat(this.diffHeight);
        out.writeString(this.name);
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
    	this.diffHeight = in.readFloat();
    	this.name = in.readString();
    	this.coordinates = new ArrayList<LatLng>();
      	in.readList(this.coordinates, LatLng.class.getClassLoader());
    }
	
	
}
