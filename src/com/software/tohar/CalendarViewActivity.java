package com.software.tohar;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import net.sourceforge.zmanim.ZmanimCalendar;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
//import net.sourceforge.zmanim.hebrewcalendar.JewishDate;
import net.sourceforge.zmanim.hebrewcalendar.JewishDate;
import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
//import net.sourceforge.zmanim.hebrewcalendar.JewishDate;
import net.sourceforge.zmanim.util.GeoLocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarViewActivity extends Activity implements OnClickListener
	{
		private static final String tag = "SimpleCalendarViewActivity";

		private ImageView calendarToJournalButton;
		private Button selectedDayMonthYearButton;
		private Button currentMonth;
		private ImageView prevMonth;
		private ImageView nextMonth;
		private GridView calendarView;
		private GridCellAdapter adapter;
		private Calendar _calendar;
		private int month, year;
		private final DateFormat dateFormatter = new DateFormat();
		private static final String dateTemplate = "MMMM yyyy";
		public static final String PREFS_NAME = "toldotru";
		public ZmanimCalendar zc;
		public String timeFormat;

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.simple_calendar_view);

				_calendar = Calendar.getInstance(Locale.getDefault());
				month = _calendar.get(Calendar.MONTH) + 1;
				year = _calendar.get(Calendar.YEAR);
				Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

				//selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
				//selectedDayMonthYearButton.setText("Selected: ");

				prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
				prevMonth.setOnClickListener(this);

				currentMonth = (Button) this.findViewById(R.id.currentMonth);
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

				nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
				nextMonth.setOnClickListener(this);

				calendarView = (GridView) this.findViewById(R.id.calendar);

				// Initialised
				adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				adapter.notifyDataSetChanged();
				calendarView.setAdapter(adapter);
				
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//			    String lat = settings.getString("lat", "59.43695");
//			    String lng = settings.getString("lng", "24.75352");
//			    String location = settings.getString("location", "Tallinn, Estonia");
				
				String lat = settings.getString("lat", "31.778");
			    String lng = settings.getString("lng", " 35.2354");
			    String DeflocationName = "Jerusalem";
			    String location = settings.getString("location", DeflocationName);
			    
			    //TimeZone timeZone = TimeZone.getTimeZone("Asia/Jerusalem");
			    
				timeFormat = settings.getString("timeFormat", "24h");
			 
			    //date today
			    //int cyear = c.get(Calendar.YEAR);
			    //int cmonth = c.get(Calendar.MONTH);
			    //int cday = c.get(Calendar.DAY_OF_MONTH);
			    
			    //zmanim
				String locationName = location;
				double latitude = Double.parseDouble(lat); 
				double longitude = Double.parseDouble(lng); 
				double elevation = 0; //optional elevation
				TimeZone timeZone = TimeZone.getDefault();
				GeoLocation geoLocation = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
				
				zc = new ZmanimCalendar(geoLocation);
				
			}
		
		
		/**
		 * 
		 * @param month
		 * @param year
		 */
		private void setGridCellAdapterToDate(int month, int year)
			{
				adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
				adapter.notifyDataSetChanged();
				calendarView.setAdapter(adapter);
			}

		@Override
		public void onClick(View v)
			{
				if (v == prevMonth)
					{
						if (month <= 1)
							{
								month = 12;
								year--;
							}
						else
							{
								month--;
							}
						Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}
				if (v == nextMonth)
					{
						if (month > 11)
							{
								month = 1;
								year++;
							}
						else
							{
								month++;
							}
						Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}

			}

		@Override
		public void onDestroy()
			{
				Log.d(tag, "Destroying View ...");
				super.onDestroy();
			}

		// ///////////////////////////////////////////////////////////////////////////////////////
		// Inner Class
		public class GridCellAdapter extends BaseAdapter implements OnClickListener
			{
				private static final String tag = "GridCellAdapter";
				private final Context _context;

				private final List<String> list;
				private static final int DAY_OFFSET = 1;
				//private final String[] weekdays = new String[]{"Вос", "Пон", "Втр", "Срд", "Чет", "Пят", "Суб"};
				private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
				//private final String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
				private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
				private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
				private final int month, year;
				private int daysInMonth, prevMonthDays;
				private int currentDayOfMonth;
				private int currentWeekDay;
				private Button gridcell;
				private TextView num_events_per_day;
				private final HashMap eventsPerMonthMap;
				private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

				// Days in Current Month
				public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
					{
						super();
						this._context = context;
						this.list = new ArrayList<String>();
						this.month = month;
						this.year = year;

						Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
						Calendar calendar = Calendar.getInstance();
						
						setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
						setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
						Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
						Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
						Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

						// Print Month
						printMonth(month, year);

						// Find Number of Events
						eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
					}
				private String getMonthAsString(int i)
					{
						return months[i];
					}

				private String getWeekDayAsString(int i)
					{
						return weekdays[i];
					}

				private int getNumberOfDaysOfMonth(int i)
					{
						return daysOfMonth[i];
					}

				public String getItem(int position)
					{
						return list.get(position);
					}

				@Override
				public int getCount()
					{
						return list.size();
					}

				/**
				 * Prints Month
				 * 
				 * @param mm
				 * @param yy
				 */
				private void printMonth(int mm, int yy)
					{
						Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
						// The number of days to leave blank at
						// the start of this month.
						int trailingSpaces = 0;
						int leadSpaces = 0;
						int daysInPrevMonth = 0;
						int prevMonth = 0;
						int prevYear = 0;
						int nextMonth = 0;
						int nextYear = 0;

						int currentMonth = mm - 1;
						String currentMonthName = getMonthAsString(currentMonth);
						daysInMonth = getNumberOfDaysOfMonth(currentMonth);

						Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

						// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
						GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
						Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

						if (currentMonth == 11)
							{
								prevMonth = currentMonth - 1;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								nextMonth = 0;
								prevYear = yy;
								nextYear = yy + 1;
								Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
							}
						else if (currentMonth == 0)
							{
								prevMonth = 11;
								prevYear = yy - 1;
								nextYear = yy;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								nextMonth = 1;
								Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
							}
						else
							{
								prevMonth = currentMonth - 1;
								nextMonth = currentMonth + 1;
								nextYear = yy;
								prevYear = yy;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
							}

						// Compute how much to leave before before the first day of the
						// month.
						// getDay() returns 0 for Sunday.
						int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
						trailingSpaces = currentWeekDay;

						Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
						Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
						Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

						if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 2)
							{
								++daysInMonth;
							}
						if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 3)
						{
							++daysInPrevMonth;
						}
						// Trailing Month days
						for (int i = 0; i < trailingSpaces; i++)
							{
								Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
								list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
							}

						// Current Month Days
						String color ="";
						for (int i = 1; i <= daysInMonth; i++)
							{
								Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
								color = "WHITE";
								if (i == getCurrentDayOfMonth())
									{
									color = "BLUE";
									}
								/*if (isSaturday((i-1)+"-"+mm+"-"+yy))
								{
									color = "RED";
								}*/
								if (isJewishHolluday(i+"-"+mm+"-"+yy))
								{
									color = "RED";
								}
								list.add(String.valueOf(i) + "-"+ color + "-" + getMonthAsString(currentMonth) + "-" + yy);
							}

						// Leading Month days
						for (int i = 0; i < list.size() % 7; i++)
							{
								Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
								list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
							}
					}

				/**
				 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
				 * ALL entries from a SQLite database for that month. Iterate over the
				 * List of All entries, and get the dateCreated, which is converted into
				 * day.
				 * 
				 * @param year
				 * @param month
				 * @return
				 */
				private HashMap findNumberOfEventsPerMonth(int year, int month)
					{
						HashMap<String, Integer> map = new HashMap<String, Integer>();
						// DateFormat dateFormatter2 = new DateFormat();
						//						
						// String day = dateFormatter2.format("dd", dateCreated).toString();
						//
						// if (map.containsKey(day))
						// {
						// Integer val = (Integer) map.get(day) + 1;
						// map.put(day, val);
						// }
						// else
						// {
						// map.put(day, 1);
						// }
						return map;
					}

				@Override
				public long getItemId(int position)
					{
						return position;
					}

				@Override
				public View getView(int position, View convertView, ViewGroup parent)
					{
						View row = convertView;
						if (row == null)
							{
								LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
							}

						// Get a reference to the Day gridcell
						gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
						gridcell.setOnClickListener(this);

						// ACCOUNT FOR SPACING

						Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
						String[] day_color = list.get(position).split("-");
						String theday = day_color[0];
						String themonth = day_color[2];
						String theyear = day_color[3];
						
						/*if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
							{
								if (eventsPerMonthMap.containsKey(theday))
									{
										num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
										Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
										num_events_per_day.setText(numEvents.toString());
									}
							}*/
						int monthInt = 0;
						for (int i = 0; i < months.length; i++) {
							if (months[i].contains(themonth)) {
								monthInt = i+1;
								break;
							}
						}
						/*if (isJewishHolluday(theday+"-"+monthInt+"-"+theyear))
						{
							num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
							num_events_per_day.setText(JewishHollidayName(theday+"-"+monthInt+"-"+theyear));
						}*/
						TextView normal_day = (TextView) row.findViewById(R.id.normal_day);
						normal_day.setText(theday);
						// Set the Day GridCell
						JewishDate hDate = new JewishDate();
						//hDate.setDate(monthInt,Integer.parseInt(theday),Integer.parseInt(theyear));
						//setGregorianDate(int year, int month, int dayOfMonth)
						hDate.setGregorianDate(Integer.parseInt(theyear), monthInt-1,Integer.parseInt(theday));
						HebrewDateFormatter hdf = new HebrewDateFormatter();
						hdf.setHebrewFormat(true);
						gridcell.setText(hDate.getJewishDayOfMonth()+"\n"+ hdf.formatMonth(hDate));
						gridcell.setTag(theday + "-" + themonth + "-" + theyear);
						Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

						if (day_color[1].equals("GREY"))
							{
								gridcell.setTextColor(Color.LTGRAY);
							}
						if (day_color[1].equals("WHITE"))
							{
								gridcell.setTextColor(Color.WHITE);
							}
						if (day_color[1].equals("BLUE"))
							{
								gridcell.setTextColor(getResources().getColor(R.color.static_text_color));
							}
						if (day_color[1].equals("RED"))
						{
							gridcell.setTextColor(Color.RED);
						}
						return row;
					}
				@Override
				public void onClick(View view)
					{
						String date_month_year = (String) view.getTag();
						//selectedDayMonthYearButton.setText("Selected: " + date_month_year);
						Date parsedDate = null;
						try
							{
								parsedDate = dateFormatter.parse(date_month_year);
								Log.d(tag, "Parsed Date: " + parsedDate.toString());

							}
						catch (ParseException e)
							{
								e.printStackTrace();
							}
						
						String[] date = date_month_year.split("-");
						int monthInt = 0;
						for (int i = 0; i < months.length; i++) {
							if (months[i].contains(date[1])) {
								monthInt = i+1;
								break;
							}
						}
						
						
						int cyear = Integer.parseInt(date[2]);
						int cmonth = monthInt;//Integer.parseInt(date[1]);
						int cday = Integer.parseInt(date[0]);
						DisplayData dd = new DisplayData().cday(cday).cmonth(cmonth-1).cyear(cyear);
						String ddOutput = dd.getOuput(CalendarViewActivity.this);
						String body =ddOutput+"\n";
						
						Calendar gCal = new GregorianCalendar (cyear,cmonth,cday);
						zc.setCalendar(gCal);
						SimpleDateFormat format_time = null;
						if (timeFormat.equalsIgnoreCase("12h")) format_time = new SimpleDateFormat("h:mm a");
						else format_time = new SimpleDateFormat("k:mm");
						
						Calendar Cal = new GregorianCalendar (cyear,cmonth-1,cday);
						JewishCalendar jDate = new JewishCalendar(Cal);
					    //jDate.setDate(Cal);
						HebrewDateFormatter hdf = new HebrewDateFormatter();
						hdf.setHebrewFormat(true);
						body = body+"\n"+ hdf.formatYomTov(jDate)+"\n";
						String alot = "";
						if (zc.getAlosHashachar() != null)
							alot = format_time.format(zc.getAlosHashachar());
						String tallit = "";
						if (zc.getSunriseOffsetByDegrees(101)!=null)
							tallit = format_time.format(zc.getSunriseOffsetByDegrees(101));
						String shmaGra = "";
						if (zc.getSofZmanShmaGRA() != null)
							shmaGra = format_time.format(zc.getSofZmanShmaGRA());
						String shkiah = "";
						if (zc.getSunset() != null)
							shkiah = format_time.format(zc.getSunset());
						body = body + 
							"Alos HaShachar:"+alot+"\n"+
						"Earliest Tallis:"+tallit+"\n"+
						"Netz:"+format_time.format(zc.getSunrise())+"\n"+
						"Latest Sh'ma:"+shmaGra+"\n"+
						"Chatzos:"+format_time.format(zc.getChatzos())+"\n"+
						"Mincha Gedola:"+format_time.format(zc.getMinchaGedola())+"\n"+
						"Mincha Ktana:"+format_time.format(zc.getMinchaKetana())+"\n"+
						"Plag Hamincha:"+format_time.format(zc.getPlagHamincha())+"\n"+
						"Shkiah:"+shkiah;
							
						Dialog adb = new Dialog(CalendarViewActivity.this);
			    		adb.setContentView(R.layout.list_item);
			    		adb.setTitle("Details");
			    		adb.setCanceledOnTouchOutside(true);
			    		TextView text = (TextView) adb.findViewById(R.id.item_text);
			    		text.setText(body);
			    		adb.show();
					}
						
				public int getCurrentDayOfMonth()
					{
						return currentDayOfMonth;
					}
				public Boolean isSaturday(String dateString)
				{
					String [] dateAr = dateString.split("-");
					int year = Integer.parseInt(dateAr[2]);
					int month = Integer.parseInt(dateAr[1]);
					int day = Integer.parseInt(dateAr[0]);
					GregorianCalendar cal = new GregorianCalendar ();
					cal.set(year, month, day);
					if (cal.get(Calendar.DAY_OF_WEEK) == 7)
						return true;
					return false;
				}
				public Boolean isJewishHolluday(String dateString)
				{
					String [] dateAr = dateString.split("-");
					int year = Integer.parseInt(dateAr[2]);
					int month = Integer.parseInt(dateAr[1]);
					int day = Integer.parseInt(dateAr[0]);
					GregorianCalendar cal = new GregorianCalendar ();
					cal.set(year, month-1, day);
					JewishCalendar jDate = new JewishCalendar(cal);
				    //jDate.setDate(cal);
				    return jDate.isYomTov();
				    /*
					if (!jDate.getHoliday().contentEquals(""))
						return true;
					return false;*/
				}
				public String JewishHollidayName(String dateString)
				{
					String [] dateAr = dateString.split("-");
					int year = Integer.parseInt(dateAr[2]);
					int month = Integer.parseInt(dateAr[1]);
					int day = Integer.parseInt(dateAr[0]);
					GregorianCalendar cal = new GregorianCalendar ();
					cal.set(year, month-1, day);
					/*JewishDate jDate = new JewishDate();
				    jDate.setDate(cal);
					return jDate.getHoliday();*/
					JewishCalendar jDate = new JewishCalendar(cal);
				    HebrewDateFormatter hdf = new HebrewDateFormatter();
					hdf.setHebrewFormat(true);
					return  hdf.formatYomTov(jDate);
				}
				

				private void setCurrentDayOfMonth(int currentDayOfMonth)
					{
						this.currentDayOfMonth = currentDayOfMonth;
					}
				public void setCurrentWeekDay(int currentWeekDay)
					{
						this.currentWeekDay = currentWeekDay;
					}
				public int getCurrentWeekDay()
					{
						return currentWeekDay;
					}
			}
	}

