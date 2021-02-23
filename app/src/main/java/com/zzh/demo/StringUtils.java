package com.zzh.demo;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {

    //饿汉式单例 节省内存
    public static Gson getInstance() {
        Gson gson = null;
        if (gson == null) {
            synchronized (Gson.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    public static Map<String, Object> transJsonToMap(String jsonStr) {
        try {
            if (null != jsonStr && !"".equals(jsonStr) && !"{}".equals(jsonStr) && !"null".equals(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Iterator<String> keyItems = jsonObject.keys();
                Map<String, Object> map = new HashMap<>();
                String key, value;
                while (keyItems.hasNext()) {
                    key = keyItems.next();
                    value = jsonObject.getString(key);
                    map.put(key, value);
                }
                return map;
            }
        } catch (JSONException e) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("list", jsonStr);
                return map;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }
        return null;
    }
    public static Map<String, Object> transResultJsonToMap(String jsonStr) {
        try {
            if (null != jsonStr && !"".equals(jsonStr) && !"{}".equals(jsonStr) && !"null".equals(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Iterator<String> keyItems = jsonObject.keys();
                Map<String, Object> map = new HashMap<>();
                String key, value;
                while (keyItems.hasNext()) {
                    key = keyItems.next();
                    value = jsonObject.getString(key);
                    map.put(key, value);
                }
                return map;
            }
        } catch (JSONException e) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("randomCode", jsonStr);//返回json是乱码
                return map;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }
        return null;
    }
    //转换成有序map
    public static Map<String, Object> transJsonToTreeMap(String jsonStr) {
        try {
            if (null != jsonStr && !"".equals(jsonStr) && !"{}".equals(jsonStr) && !"null".equals(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Iterator<String> keyItems = jsonObject.keys();
                Map<String, Object> map = new TreeMap<>();
                String key, value;
                while (keyItems.hasNext()) {
                    key = keyItems.next();
                    value = jsonObject.getString(key);
                    map.put(key, value);
                }
                return map;
            }
        } catch (JSONException e) {
            try {
                Map<String, Object> map = new TreeMap<>();
                map.put("list", jsonStr);
                return map;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }
        return null;
    }

    public static Object convertMapToList(String reqResultStr, TypeToken typeToken) {
        Gson gson = getInstance();
        Type type = typeToken.getType();
        if (null == reqResultStr || "".equals(reqResultStr) || "{}".equals(reqResultStr)) {
            return null;
        } else {
            return gson.fromJson(reqResultStr, type);
        }
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isStrEmpty(String str) {
        if (!TextUtils.isEmpty(str) && !TextUtils.equals("null", str)) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 判断字符串是否为某一状态
     * @param str  要比较的字符串
     * @param type  要比较的类型
     * @return
     */
    public static boolean getTypeState(String str, String type) {
        return !StringUtils.isStrEmpty(str) && TextUtils.equals(str, type);
    }
    /**
     * JSON对象转java对象
     * @param jSonObjectStr
     * @param tClass
     * @return
     */
    public static <T> T convertJSonObjectStrToObject(String jSonObjectStr, Class<T> tClass) {
        Gson gson = getInstance();
        return gson.fromJson(jSonObjectStr, tClass);
    }
}