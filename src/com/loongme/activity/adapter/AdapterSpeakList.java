package com.loongme.activity.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.bean.SpeakItem;
import com.loongme.activity.ui.ActivityShowMoreInfo;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-3 下午4:11:51 类说明:人机聊天列表的适配器
 */
public class AdapterSpeakList extends BaseAdapter {

	private Context mContext;
	private List<SpeakItem> datas;

	public static final int SpeakerType_Robot = 0;// 机器人小帮
	public static final int SpeakerType_User = 1;// 用户

	public AdapterSpeakList(Context mContext, List<SpeakItem> datas) {
		super();
		this.mContext = mContext;
		this.datas = datas;
	}

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
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (datas.get(position).getSpeakerType() == SpeakerType_Robot) {
			return SpeakerType_Robot;
		} else if (datas.get(position).getSpeakerType() == SpeakerType_User) {
			return SpeakerType_User;
		} else {
			return -1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RobotViewHolder robotViewHolder = null;
		UserViewHolder userViewHolder = null;
		if (convertView == null) {
			switch (getItemViewType(position)) {
			case SpeakerType_Robot:
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_speaklist_robot, null);
				robotViewHolder = new RobotViewHolder();
				robotViewHolder.tx_robot_speaktime = (TextView) convertView.findViewById(R.id.tx_robot_speaktime);
				robotViewHolder.tx_robot_speakcontent = (TextView) convertView.findViewById(R.id.tx_robot_speakcontent);
				robotViewHolder.image_robot_photo = (ImageView) convertView.findViewById(R.id.img_robot_photo);
				convertView.setTag(robotViewHolder);
				break;
			case SpeakerType_User:
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_speaklist_user, null);
				userViewHolder = new UserViewHolder();
				userViewHolder.tx_user_speaktime = (TextView) convertView.findViewById(R.id.tx_user_speaktime);
				userViewHolder.tx_user_speakcontent = (TextView) convertView.findViewById(R.id.tx_user_speakcontent);
				userViewHolder.image_userPhoto = (ImageView) convertView.findViewById(R.id.img_userPhoto);
				convertView.setTag(userViewHolder);
				break;
			default:
				break;
			}
		} else {
			switch (getItemViewType(position)) {
			case SpeakerType_Robot:
				robotViewHolder = (RobotViewHolder) convertView.getTag();
				break;
			case SpeakerType_User:
				userViewHolder = (UserViewHolder) convertView.getTag();
				break;
			default:
				break;
			}
		}
		// 初始化数据
		SpeakItem speakItem = datas.get(position);
		switch (getItemViewType(position)) {
		// 小帮
		case SpeakerType_Robot:
			if (StringUtils.isEmpty(speakItem.getSpeakTime())) {
				robotViewHolder.tx_robot_speaktime.setVisibility(View.GONE);
			} else {
				robotViewHolder.tx_robot_speaktime.setVisibility(View.VISIBLE);
				robotViewHolder.tx_robot_speaktime.setText(speakItem.getSpeakTime());
			}
			// 聊天内容
			robotViewHolder.tx_robot_speakcontent.setText(Html.fromHtml(speakItem.getSpeakerContent()));
			robotViewHolder.tx_robot_speakcontent.setMovementMethod(LinkMovementMethod.getInstance());
			addLink(robotViewHolder.tx_robot_speakcontent);
			robotViewHolder.image_robot_photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					UIUtil.showMsg(mContext, "进入小帮页面");

				}
			});
			break;
		// 用户
		case SpeakerType_User:
			userViewHolder.tx_user_speaktime.setText(speakItem.getSpeakTime());
			userViewHolder.tx_user_speakcontent.setText(speakItem.getSpeakerContent());
			if (!StringUtils.isEmpty(speakItem.getSpeakerPhoto())) {
				ImageLoader.getInstance().displayImage(speakItem.getSpeakerPhoto(), userViewHolder.image_userPhoto);
			} else {
				userViewHolder.image_userPhoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dtx));
			}
			break;
		default:
			break;
		}

		return convertView;
	}

	private class RobotViewHolder {
		public TextView tx_robot_speaktime;
		public TextView tx_robot_speakcontent;
		public ImageView image_robot_photo;
	}

	private class UserViewHolder {
		public TextView tx_user_speaktime;
		public TextView tx_user_speakcontent;
		public ImageView image_userPhoto;
	}

	private void addLink(TextView tv) {
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans
			for (URLSpan url : urls) {
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			tv.setText(style);
		}
	}

	private class MyURLSpan extends ClickableSpan {

		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			if (mUrl.contains("tel:")) {
				String[] first = mUrl.split("tel:");
				String fis = first[1];
				// Toast.makeText(mContext, "hello!" + fis,
				// Toast.LENGTH_LONG).show();
				// 用intent启动拨打电话
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + fis.trim()));
				mContext.startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(mContext, ActivityShowMoreInfo.class);
				intent.putExtra("url", mUrl);
				mContext.startActivity(intent);
			}
		}
	}

}
