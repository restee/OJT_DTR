package com.example.gztrackz;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class TimeStampQueryDialog extends Activity {

	private Button okBTN;
	private Spinner yearSpin,monthSpin,daySpin;
	private ArrayList<String> yearList,monthList,dayList;
	private ArrayAdapter<String> yearSpinnerArrayAdapter,monthSpinnerArrayAdapter,daySpinnerArrayAdapter ;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.query_history_dialog);
		
		context = this;
		okBTN = (Button) findViewById(R.id.querybutton);
		yearSpin = (Spinner) findViewById(R.id.yearSpinner);
		monthSpin = (Spinner) findViewById(R.id.monthSpinner);
		daySpin = (Spinner) findViewById(R.id.daySpinner);
		
		yearList = new ArrayList();
		monthList = new ArrayList();
		dayList = new ArrayList();
		yearList.add("----");
		monthList.add("--------");
		dayList.add("--");
		
		
		yearSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
		yearSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearSpin.setAdapter(yearSpinnerArrayAdapter);
		
		monthSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthList);
		monthSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSpin.setAdapter(monthSpinnerArrayAdapter);
		
		daySpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayList);
		daySpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daySpin.setAdapter(daySpinnerArrayAdapter);
		
		
		
		yearSpin.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				for(int init = yearList.size()-1;init>=0;init--){				
					yearList.remove(init);
				}				
				for(int init=2000;init<=2015;init++){
					yearList.add(Integer.toString(init));
				}
				yearSpinnerArrayAdapter.notifyDataSetChanged();	
				return false;
			}
		});
		
		monthSpin.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(yearList.size()>1){
					for(int init = monthList.size()-1;init>=0;init--){
						monthList.remove(init);
					}
					monthList.add("January");
					monthList.add("February");
					monthList.add("March");
					monthList.add("April");
					monthList.add("May");
					monthList.add("June");
					monthList.add("July");
					monthList.add("August");
					monthList.add("September");
					monthList.add("October");
					monthList.add("November");
					monthList.add("December");
					return false;
				}else{
					Toast.makeText(context,"Please select a year first!",Toast.LENGTH_SHORT).show();		
					return true;
				}								
			}
		});
		
		daySpin.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {												
				if(yearList.size()>1&&monthList.size()>1){		
					for(int init = dayList.size()-1;init>=0;init--){
						dayList.remove(init);
					}
					
					int monthSelected = monthSpin.getSelectedItemPosition()+1;
					int days=0;
					if(monthSelected==1||monthSelected==3||monthSelected==5||monthSelected==7||monthSelected==8||
							monthSelected==10||monthSelected==12){
						days = 31;
					}else if(monthSelected==4||monthSelected==6||monthSelected==9||monthSelected==11){
						days = 30;						
					}else if (monthSelected==2){
						int yearSelected = Integer.parseInt(yearSpin.getSelectedItem().toString());
						if(yearSelected%4==0){
							days=29;
						}else{
							days=28;
						}
					}
					
					for(int init=1;init<=days;init++){
						dayList.add(Integer.toString(init));						
					}
					
					return false;
				}else if (yearList.size()<=1){
					Toast.makeText(context,"Please select a year first!",Toast.LENGTH_SHORT).show();
					return true;
				}else if (monthList.size()<=1){
					Toast.makeText(context,"Please select a month first",Toast.LENGTH_SHORT).show();
					return true;
				}
				return true;
			}
		});
		
		
		okBTN.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent data = new Intent();
				data.putExtra("year",Integer.parseInt(yearSpin.getSelectedItem().toString()));
				data.putExtra("month",monthSpin.getSelectedItemPosition()+1);
				data.putExtra("day",daySpin.getSelectedItemPosition()+1);
				setResult(RESULT_OK, data);
				finish();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_stamp_query_dialog, menu);
		return true;
	}

}
