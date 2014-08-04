package list_adapters;

import list_objects.Group;
import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gztrackz.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter{

	private final SparseArray<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;
	
	public ExpandableListAdapter(Activity act, SparseArray<Group> groups) {
	    activity = act;
	    this.groups = groups;
	    inflater = act.getLayoutInflater();
	  }
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
		      boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
	    TextView text = null;
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.listrow_details, null);
	    }
	    text = (TextView) convertView.findViewById(R.id.textView1);
	    text.setText(children);
	    convertView.setOnClickListener(new OnClickListener() {
	      @Override
	      public void onClick(View v) {
	        Toast.makeText(activity, children,
	            Toast.LENGTH_SHORT).show();
	      }
	    });
	    return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
		      View convertView, ViewGroup parent) {
		if (convertView == null) {
		      convertView = inflater.inflate(R.layout.listrow_group, null);
		    }
		    Group group = (Group) getGroup(groupPosition);
		    ((CheckedTextView) convertView).setText(group.string);
		    ((CheckedTextView) convertView).setChecked(isExpanded);
		    return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
