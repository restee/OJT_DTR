package com.example.adapter;

import java.util.List;

import com.example.scrumgztrack.ManageTeam;
import com.example.scrumgztrack.R;
import com.example.tabs.OJTlistFragment;
import com.example.tabs.TeamListFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class TeamListAdapter extends BaseAdapter{
	private List<String> teamList;
	private Context context;
	
	public TeamListAdapter(Context context, List<String> teamList){
		this.teamList = teamList;
		this.context = context;
	}
	@Override
	public int getCount() {
		return teamList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return teamList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public View getView(int position, View convertView,
			ViewGroup parent) {
	
	

		ViewHolder holder;

		if (convertView == null) {

			convertView = ((LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.ojtlist_item, parent, false);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_nameTxt.setText(teamList.get(position));

		holder.iv_rowSelected.setOnClickListener(new rowClickListener(position));

		
		
		return convertView;

	}
	private class ViewHolder {

		ImageView iv_rowSelected;
		TextView tv_nameTxt;

		public ViewHolder(View convertView) {

			iv_rowSelected = (ImageView) convertView
					.findViewById(R.id.iv_rowSelected);
			tv_nameTxt = (TextView) convertView.findViewById(R.id.tv_ojtName);

		}
	}

	private class rowClickListener implements View.OnClickListener {
		int position;
		public rowClickListener(int position){
			this.position = position;
		}
		@Override
		public void onClick(View selectedView) {
			PopupMenu popup = new PopupMenu(context, selectedView);
			MenuInflater inflater = popup.getMenuInflater();		
			inflater.inflate(R.menu.manage_team, popup.getMenu());
			
			popup.show();
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {			
				public boolean onMenuItemClick(MenuItem item) {					
					if(item.toString().compareToIgnoreCase("Remove Team")==0){
						Toast.makeText(context, "Remove Team " + teamList.get(position),Toast.LENGTH_SHORT).show();
					}else if(item.toString().compareToIgnoreCase("Go to Team")==0){
						Toast.makeText(context, "Go to Team" + teamList.get(position),Toast.LENGTH_SHORT).show();
						Intent i = new Intent(context,ManageTeam.class);
						i.putExtra("teamName",teamList.get(position));
						context.startActivity(i);
					}
					return false;
				}
			});
		}
	}
}
