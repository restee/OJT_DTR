package com.example.adapter;

import java.util.List;

import com.example.scrumgztrack.Person;
import com.example.scrumgztrack.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;



public class OJTlistAdapter extends BaseAdapter {
	private List<Person> resultList;
	private Context context;
	
	public OJTlistAdapter(Context context, List<Person> resultList) {
		this.resultList = resultList;
		this.context = context;
	}
	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return resultList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.ojtlist_item, parent,false);
		}
		
		//-----------------------//
		
		
		return convertView;
	}

}