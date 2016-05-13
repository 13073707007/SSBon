package com.loongme.activity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.bean.SSBonServerSub;

public class ClassifySubAdapter extends BaseAdapter {
	private Context context;
	private List<SSBonServerSub> subList;

	public ClassifySubAdapter(Context context, List<SSBonServerSub> subList) {
		super();
		this.context = context;
		this.subList = subList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return subList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.classify_subitem, null);
		TextView classify_subitem = (TextView) convertView.findViewById(R.id.classify_subtext);
		classify_subitem.setText(subList.get(position).getName());
		return convertView;
	}

}
