package com.software.tohar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


import net.sourceforge.zmanim.ZmanimCalendar;
import net.sourceforge.zmanim.hebrewcalendar.JewishDate;
import net.sourceforge.zmanim.util.GeoLocation;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

public class DisplayData {
	public static final String PREFS_NAME = "toldotru";
	private int _cyear = 1900;
	private int _cmonth = 1;
	private int _cday = 1;
	
	public DisplayData() { }
	 public DisplayData cyear(int _cyear)
	    {
	        this._cyear = _cyear;
	        return this;
	    }
	 public DisplayData cmonth(int _cmonth)
	    {
	        this._cmonth = _cmonth;
	        return this;
	    }
	 public DisplayData cday(int _cday)
	    {
	        this._cday = _cday;
	        return this;
	    }
	 public Boolean isHolliday(Context context)
		{
		 
		 return true;
		}
	public String getOuput(Context context)
	{
		//getting current date
     	Calendar c = Calendar.getInstance();
     	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
     	
	     int cyear = c.get(Calendar.YEAR);
	     int cmonth = c.get(Calendar.MONTH);
		 int cday = c.get(Calendar.DAY_OF_MONTH);
		if (_cyear!=1900)
			cyear = _cyear;
		if (_cmonth!=1)
			cmonth = _cmonth;
		if (_cday!=1)
			cday = _cday;
       
     	//Generate date output
 	SimpleDateFormat format = new SimpleDateFormat("dd MMMM");
 	Date gDate = new Date(cyear,cmonth,cday);
 	//gCal.setTime(gDate);
 	String date_selected = format.format(gDate);
 	date_selected = date_selected +", "+cyear;//gCal.get(Calendar.YEAR);
 	Calendar gCal = new GregorianCalendar (cyear,cmonth,cday);
 	
 	 	 // Restore preferences
         
	       String location = settings.getString("location", "Tallinn, Estonia");
	       double lat = Double.parseDouble(settings.getString("lat", "59.43695"));
		   double lng = Double.parseDouble(settings.getString("lng", "24.75352"));
		   String timeZone1 = settings.getString("timeZone", "Europe/Tallinn");
      
  	//Verify if time is after sunset or not
  	//sunset time on this day
    //zmanim
   	
   	double elevation = 0; //optional elevation
   	TimeZone timeZone = TimeZone.getTimeZone(timeZone1);
   	GeoLocation locationG = new GeoLocation(location, lat, lng, elevation, timeZone);
   	
   	ZmanimCalendar zc = new ZmanimCalendar(locationG);
  	Date sunset = zc.getSeaLevelSunset();
  	zc.setCalendar(gCal);
  	int sunsetHour = sunset.getHours();
  	int sunsetMin = sunset.getMinutes();
  	//current time
  	Date timeNow = new Date();
  	int nowHour = timeNow.getHours();
  	int nowMin = timeNow.getMinutes();
  	
  	if (sunsetHour < nowHour)
  	{
  		gCal.add(Calendar.DAY_OF_MONTH, 1); 
  		
  	}
  	else if(sunsetHour == nowHour)
  	{
  		if (sunsetMin < nowMin) 
  		{
  			gCal.add(Calendar.DAY_OF_MONTH, 1);
  			
  		}
  	}
  	
 	
     JewishDate jDate = new JewishDate(gCal);
     //jDate.setJewishDate( gCal.get(Calendar.YEAR),gCal.get(Calendar.MONTH)+1, gCal.get(Calendar.DAY_OF_MONTH));
     
     int jDay = jDate.getJewishDayOfMonth();
     //String jMonth = jDate.getHebrewMonthAsString();
     //String curjDate = jDay+" "+jMonth+ " "+jDate.getHebrewYear();
     String curjDate=jDate.toString();
     
	//write to textboxformat.format(gDate)
	return curjDate+"\n"+date_selected;
}
	
}