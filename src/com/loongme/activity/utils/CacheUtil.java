package com.loongme.activity.utils;

import java.net.URI;

import org.json.JSONObject;

import com.loongme.activity.utils.StringUtils;

public class CacheUtil {
  /**
   * 读取缓存
   * 
   * @param uri
   * @param params
   * @param userName
   * @return
   */
  public static JSONObject readCache(URI uri, String params, String userName) {
    String cacheKey = StringUtils.generateCacheKey(uri.toString() + params);// uri
    // Trace.d("userName: "+userName+
    // " uri:"+uri.toString()+" params:"+params+ " key:"+key);
    // TOTEST
    ACache cache = ACache.getInstance(userName);// BaseApplication.getInstance().getCache();
    JSONObject response = cache.getAsJSONObject(cacheKey);
    return response;
  }

  /**
   * 放置缓存
   * 
   * @param uri
   * @param params
   * @param userName
   * @param response
   */
  public static void putCache(URI uri, String params, String userName, JSONObject response) {
    // Trace.d("userName: "+userName+
    // " uri:"+uri.toString()+" params:"+params+ " key:"+key);
    // TOTEST
    String key = StringUtils.generateCacheKey(uri.toString() + params);
    ACache cache = ACache.getInstance(userName);// BaseApplication.getInstance().getCache();
    if (null != cache) {
      cache.put(key, response.toString());
    }
  }
}
