package com.insa.randon.utilities;

import static com.insa.randon.services.Constants.PARAMETER_LATITUDE;
import static com.insa.randon.services.Constants.PARAMETER_LONGITUDE;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class ParserTool {
	public static List<LatLng> parseCoordinates(JSONArray JSONCoordinates) throws JSONException{
		List<LatLng> coordinates = new ArrayList<LatLng>();
		for(int i=0 ; i<JSONCoordinates.length() ; i++){
			JSONObject oneCoordinate = (JSONObject)JSONCoordinates.get(i);
			coordinates.add(new LatLng(oneCoordinate.getDouble(PARAMETER_LATITUDE),oneCoordinate.getDouble(PARAMETER_LONGITUDE)));
		}
		
		return coordinates;
	}
}
