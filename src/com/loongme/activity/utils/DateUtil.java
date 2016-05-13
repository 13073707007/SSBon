package com.loongme.activity.utils;

/**============================================================
 * 版权： 久其软件 版权所有 (c) 
 * 包： com.jiuqi.muchmore.clothing.common.tools
 * 修改记录：
 * 日期                作者           内容
 * =============================================================
 * 2012-6-6       Administrator        
 * ============================================================*/

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 时间格式化类
 * </p>
 * 
 * <p>
 * Copyright: 版权所有 (c)<br>
 * Company: 久其
 * </p>
 * 
 * @author Administrator
 * @version 2012-6-6
 */

public class DateUtil {
	/**
	 * 星期几的汉字
	 */
	public static final String WEEK_OF_DAY_NAMES[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	/**
	 * yyyy-MM-dd
	 */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	/**
	 * yy-MM-dd
	 */
	public static final String YY_MM_DD = "yy-MM-dd";
	/**
	 * MM/dd HH:mm:ss
	 */
	public static final String MM_DD_HH_MM_SS = "MM/dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMddHHmmss作为文件的名称
	 */
	public static final String YYYY_MM_DD_HH_MM_SS_SHORT = "yyyyMMddHHmmss";

	/**
	 * MM/dd HH:mm
	 */
	public static final String MM_DD_HH_MM = "MM/dd HH:mm";

	/**
	 * MM-dd
	 */
	public static final String MM_DD = "MM-dd";

	/**
	 * MM月 dd日 HH:mm
	 */
	public static final String MM_DD_HH_MM1 = "MM月dd日 HH:mm";

	/**
	 * 得到当前时间的字符串，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		return df.format(d);
	}

	/**
	 * 得到当前时间的字符串，格式为HH:mm
	 * 
	 * @return
	 */
	public static String getCurrentSpeakTime() {
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(d);
	}

	/**
	 * 根据long值，转化为yyyy-MM-dd hh:mm的格式
	 * 
	 * @param timelong
	 * @return String
	 */
	public static String getTimeStamp(long timelong) {

		Date d = new Date(checkPhpOrJavaTimeStamp(timelong));
		SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
		return df.format(d);
	}

	public static long checkPhpOrJavaTimeStamp(long timelong) {
		StringBuffer sb = new StringBuffer();
		String tmp = String.valueOf(timelong);
		sb.append(tmp);
		// 如果是php返回的10位时间戳，则补000
		if (tmp.length() == 10) {
			sb.append("000");
		}
		return Long.parseLong(sb.toString());
	}

	/**
	 * timepatten就是SimpleDateFormat用到的patten，比如："yyyy-MM-dd HH:mm"
	 * 
	 * @param timelong
	 * @param timepatten
	 * @return String
	 */
	public static String getTimeStamp(long timelong, String timepatten) {
		Date d = new Date(checkPhpOrJavaTimeStamp(timelong));
		SimpleDateFormat df = new SimpleDateFormat(timepatten);
		return df.format(d);
	}

	/**
	 * timepatten就是SimpleDateFormat用到的patten，比如："yyyy-MM-dd HH:mm"
	 * 
	 * @param timelong
	 * @param timepatten
	 * @return String
	 */
	public static String getTimeStamp(Date date, String timepatten) {
		SimpleDateFormat df = new SimpleDateFormat(timepatten);
		return df.format(date);
	}

	/**
	 * 根据年月日的值取得时间片
	 * 
	 * @param mYear
	 * @param mMonth
	 *            自动减去1900
	 * @param mDay
	 * @return long
	 */
	public static long getTimeStampFromYearMonthDay(int mYear, int mMonth, int mDay) {
		Date dt = new Date();
		dt.setYear(mYear - 1900);
		dt.setMonth(mMonth);
		dt.setDate(mDay);
		return dt.getTime();
	}

	/**
	 * 解析字符串，返回Date对象，字符串格式默认为yyyy-MM-dd；如果解析出现异常，返回null。
	 * 
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDate(String strDate) {
		return parseDate(strDate, DateUtil.YYYY_MM_DD);
	}

	/**
	 * 解析字符串，返回Date对象，可以传入字符串格式进行解析，建议使用DateUtil内置的字符串格式。如果解析出现异常，返回null。
	 * 
	 * @param strDate
	 * @return Date
	 */
	public static Date parseDate(String strDate, String dateFormat) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 根据年月日的值取得时间片
	 * 
	 * @param mYear
	 * @param mMonth
	 *            自动减去1900
	 * @param mDay
	 * @return long
	 */
	public static long getTimeStampFromYearMonthDay(int mYear, int mMonth, int mDay, int mHour, int mMin) {
		Date dt = new Date();
		dt.setYear(mYear);
		dt.setMonth(mMonth);
		dt.setDate(mDay);
		dt.setHours(mHour);
		dt.setMinutes(mMin);
		return dt.getTime();
	}

	/**
	 * @param dayOfMonth
	 * @param month
	 * @param year
	 * 
	 */
	public static boolean compareDateIsLessThanNow(int year, int month, int dayOfMonth) {
		Date dt = new Date();
		int nowYear = dt.getYear();
		int nowMonth = dt.getMonth();
		int nowDayOfMonth = dt.getDate();
		if (year < nowYear) {
			return true;
		}
		if (month < nowMonth) {
			return true;
		}
		if (dayOfMonth < nowDayOfMonth) {
			return true;
		}
		return false;
	}

	public static boolean isEndDateBeforeStartDate(String startDate, String endDate) {
		int i = startDate.compareToIgnoreCase(endDate);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 把HH24:MI转换成为分钟数
	 * 
	 * @param hourAndMinutes
	 *            <pre>
	 * getMinutes(&quot;10:30&quot;)// -&gt; 630
	 * getMinutes(&quot;13:44&quot;)// -&gt; 824
	 * getMinutes(&quot;07:00&quot;)// -&gt; 420
	 * getMinutes(&quot;00:00&quot;)// -&gt; 0
	 * getMinutes(&quot;24:00&quot;)// -&gt; 1440
	 * </pre>
	 */
	public static int convertMinutesFromHourAndMinute(String hourAndMinutes) {
		int minutes = 0;
		String[] strings = hourAndMinutes.split(":");
		minutes = Integer.parseInt(strings[0]) * 60 + Integer.parseInt(strings[1]);
		return minutes;
	}

	/**
	 * 返回unix时间戳 (1970年至今的秒数)
	 * 
	 * @return
	 */
	public static long getUnixStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 得到昨天的日期
	 * 
	 * @return
	 */
	public static String getYestoryDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String yestoday = sdf.format(calendar.getTime());
		return yestoday;
	}

	/**
	 * 得到今天的日期
	 * 
	 * @return
	 */
	public static String getTodayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		return date;
	}

	/**
	 * 时间戳转化为时间格式
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String timeStampToStr(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(timeStamp * 1000);
		return date;
	}

	/**
	 * 得到日期 yyyy-MM-dd
	 * 
	 * @param timeStamp
	 *            时间戳
	 * @return
	 */
	public static String formatDate(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(timeStamp * 1000);
		return date;
	}

	/**
	 * 得到时间 HH:mm:ss
	 * 
	 * @param timeStamp
	 *            时间戳
	 * @return
	 */
	public static String getTime(long timeStamp) {
		String time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(timeStamp * 1000);
		String[] split = date.split("\\s");
		if (split.length > 1) {
			time = split[1];
		}
		return time;
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String convertTimeToFormat(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = curTime - timeStamp;

		if (time < 60 && time >= 0) {
			return "刚刚";
		} else if (time >= 60 && time < 3600) {
			return time / 60 + "分钟前";
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + "小时前";
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			return time / 3600 / 24 + "天前";
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 + "个月前";
		} else if (time >= 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 / 12 + "年前";
		} else {
			return "刚刚";
		}
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，(多少分钟)
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String timeStampToFormat(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = curTime - timeStamp;
		return time / 60 + "";
	}

}
