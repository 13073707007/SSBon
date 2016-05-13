package com.loongme.activity.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseFragment;
import com.loongme.activity.base.MenuItemClickCallBack;
import com.loongme.activity.bean.MoreMenuItem;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-4 下午12:03:17 类说明:
 */
public class FragmentMoreMenuVP extends BaseFragment {

	private GridView gridView;
	private List<MoreMenuItem> datas;
	private View contentView;
	private MenuItemClickCallBack menuItemClickCallBack;

	public FragmentMoreMenuVP(List<MoreMenuItem> datas, MenuItemClickCallBack menuItemClickCallBack) {
		super();
		this.datas = datas;
		this.menuItemClickCallBack = menuItemClickCallBack;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_moremenu, null);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gridView = (GridView) contentView.findViewById(R.id.grid_moremenu);
		gridView.setAdapter(new GridAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (menuItemClickCallBack != null) {
					menuItemClickCallBack.onMoreMenuItemClick(datas.get(position));
				}

			}
		});
		return contentView;
	}

	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_grid_moremenu, null);
				viewHolder = new ViewHolder();
				viewHolder.image_menuitem = (ImageView) convertView.findViewById(R.id.img_menuitem);
				viewHolder.tx_menuitem = (TextView) convertView.findViewById(R.id.tx_menuitem);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MoreMenuItem item = datas.get(position);
			viewHolder.image_menuitem.setImageDrawable(getActivity().getResources()
					.getDrawable(item.getMenuIconResId()));
			viewHolder.tx_menuitem.setText(item.getMenuName());
			return convertView;
		}

	}

	private class ViewHolder {
		ImageView image_menuitem;
		TextView tx_menuitem;

	}

}
