package com.hit.changyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class CityReceiver extends BroadcastReceiver {
	
	public CityReceiver() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String city;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
		this.city = arg1.getExtras().getString("city"); 
		if (MapFactory.mapActivity!=null) {
			MapFactory.mapActivity.SearchCity(city);
			MapFactory.mapActivity.app.cityString=city;
		}
		if (MapFactory.mySearchListActivity!=null) {
//			MapFactory.mySearchListActivity.getCity(city);
			MapFactory.mySearchListActivity.SearchCity(city);
			MapFactory.mySearchListActivity.app.cityString = city;
			
		}
		if (MapFactory.mArNavigatorActivity!=null) {
			MapFactory.mArNavigatorActivity.app.cityString = city;
		}
		
	}

}
