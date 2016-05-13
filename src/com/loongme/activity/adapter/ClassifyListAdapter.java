package com.loongme.activity.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.bean.SSBonServer;

public class ClassifyListAdapter extends BaseAdapter {
	private Context context;
	private List<SSBonServer> list;
	private int selectedPosition;// 选中的位置

	public ClassifyListAdapter(Context context, List<SSBonServer> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.classify_items, null);
		TextView classify_text = (TextView) convertView.findViewById(R.id.classify_text);
		classify_text.setText(list.get(position).getName());
		// 设置背景颜色
		if (selectedPosition == position) {
			classify_text.setSelected(true);
			classify_text.setPressed(true);
			convertView.setBackgroundColor(Color.parseColor("#EBEBEC"));
		} else {
			classify_text.setSelected(false);
			classify_text.setPressed(false);
			convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
		return convertView;
	}
}
