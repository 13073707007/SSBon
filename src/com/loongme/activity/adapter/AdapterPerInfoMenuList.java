package com.loongme.activity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.bean.PerInfoMenuItem;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-7 下午5:25:22 类说明:
 */
public class AdapterPerInfoMenuList extends BaseAdapter {

	private List<PerInfoMenuItem> items;
	private Context mContext;

	public AdapterPerInfoMenuList(List<PerInfoMenuItem> items, Context mContext) {
		super();
		this.items = items;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_perinfo_menu, null);
			viewHolder = new ViewHolder();
			viewHolder.menu_icon = (ImageView) convertView.findViewById(R.id.img_menu_icon);
			viewHolder.tx_menuName = (TextView) convertView.findViewById(R.id.tx_menu_name);
			viewHolder.menuitem_seperator = (View) convertView.findViewById(R.id.view_menuitem_seperator);
			viewHolder.menuitem_line = (View) convertView.findViewById(R.id.view_menuitem_line);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PerInfoMenuItem menuItem = items.get(position);
		viewHolder.menu_icon.setImageDrawable(mContext.getResources().getDrawable(menuItem.getIconResId()));
		viewHolder.tx_menuName.setText(menuItem.getName());

		if (menuItem.isBonke()) {
			if (position == 0 || position == 1 || position == 3) {
				viewHolder.menuitem_seperator.setVisibility(View.VISIBLE);
			} else {
				viewHolder.menuitem_seperator.setVisibility(View.GONE);
			}
			if (position == 1 || position == 3) {
				viewHolder.menuitem_line.setVisibility(View.VISIBLE);
			} else {
				viewHolder.menuitem_line.setVisibility(View.GONE);
			}
		} else {
			if (position == 0) {
				viewHolder.menuitem_seperator.setVisibility(View.VISIBLE);
			} else {
				viewHolder.menuitem_seperator.setVisibility(View.GONE);
			}
			viewHolder.menuitem_line.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView menu_icon;
		public TextView tx_menuName;
		public View menuitem_seperator;
		public View menuitem_line;
	}

}
