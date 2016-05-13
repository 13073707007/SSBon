package com.loongme.activity.utils;

import org.apache.http.client.CookieStore;

import android.content.Context;

import com.loopj.android.http.PersistentCookieStore;

public class CookieUtil {
	
	private static CookieStore store;

	public static CookieStore getCookieStore(Context context) {
		if (null == store) {
			store = new PersistentCookieStore(context);
		}
		return store;
	}
}
