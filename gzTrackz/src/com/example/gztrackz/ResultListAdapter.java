package com.example.gztrackz;

import java.util.List;
import java.sql.Timestamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultListAdapter extends BaseAdapter {
	private List<TimeLog> resultList;
	private Context context;
	
	public ResultListAdapter(Context context, List<TimeLog> resultList) {
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
			convertView = inflater.inflate(R.layout.history_timein_timeout_item, parent,false);
		}
		
		TextView date = (TextView) convertView.findViewById(R.id.historydateitem);
		TextView timein = (TextView) convertView.findViewById(R.id.historytimeinitem);
		TextView timeout = (TextView) convertView.findViewById(R.id.historytimeoutitem);
		
		
		date.setText(resultList.get(position).getTimeIn().substring(0,11));
		timein.setText(resultList.get(position).getTimeIn().substring(11,resultList.get(position).getTimeIn().length()));
		if(resultList.get(position).getTimeOut().length()>4){
			timeout.setText(resultList.get(position).getTimeOut().substring(11,resultList.get(position).getTimeOut().length()));
		}else{
			timeout.setText("--:--:--");
		}
		
		
		return convertView;
	}

}
