package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.scrumgztrack.Person;
import com.example.scrumgztrack.R;

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
	public View getView(int personAtCurrentPosition, View convertView,
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

		holder.tv_nameTxt.setText(resultList.get(personAtCurrentPosition)
				.getFirstName()
				+ " "
				+ resultList.get(personAtCurrentPosition).getLastName());

		holder.iv_rowSelected.setOnClickListener(new rowClickListener());

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

		@Override
		public void onClick(View selectedView) {

			PopupMenu popup = new PopupMenu(context, selectedView);
			MenuInflater inflater = popup.getMenuInflater();
			inflater.inflate(R.menu.add_remove_tofrom_team, popup.getMenu());
			popup.show();

		}

	}

}
