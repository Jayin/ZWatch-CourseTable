package com.jayin.coursetable.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jayin.coursetable.entity.Course;

/**
 * 用户配置偏好信息,管理版本等信息<br>
 * ps:一般有默认值<brs> 一般都是isXXX() / getXXX() /setxxx()的方法
 * (准备分离，暂时保留)1.获取/设置是否第一次打开App<br>
 * 2.获取是否有课表<br>
 * 3.获取/设置当前周数<br>
 * 4.获取/设置是否有未读消息<br>
 * 5.获取/设置子系统账号、密码<br>
 * 6.获取/设置当前版本代号<br>
 * 
 * @author Jayin Ton
 * @version 2.2
 */
public class Preferences {

	public final static String SP_NAME_Preferences = "Preferences";

	/** to get SharedPreferences */
	public static SharedPreferences getSP(Context context) {
		return context.getSharedPreferences(SP_NAME_Preferences,
				Context.MODE_PRIVATE);
	}

	/** helper method like the set() in class SharedPreferenceHelper1 */
	public static boolean set(SharedPreferences sp, String key, String value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/** 是否有课表,这是触发式的，故省略设置是否存在 */
	public static boolean isCourseExist(Context context) {
		Course c = (Course) new DataPool(ZWatchContants.SP_Name_Course, context)
				.get(ZWatchContants.SP_Key_Course);
		return c == null ? false : true;
	}

	public static int getCurrentWeek(Context context) {
		SharedPreferences sp = getSP(context);
		int currentWeek = Integer.parseInt(sp.getString("currentWeek", "1"));
		int weekOfYear = Integer.parseInt(sp.getString("weekOfYear",
				CalendarManager.getWeekOfYeah() + ""));
		if (weekOfYear != CalendarManager.getWeekOfYeah()) {
			currentWeek++;
			setCurrentWeek(context, currentWeek);
		}
		return currentWeek;
	}

	/** 设置当前周数,并且发周数改变的广播 */
	public static void setCurrentWeek(Context context, int currentWeek) {
		if (currentWeek <= 0) {
			currentWeek = 1;
		}
		set(getSP(context), "currentWeek", currentWeek + "");
		set(getSP(context), "weekOfYear", CalendarManager.getWeekOfYeah() + "");
		// 发广播通知Home页面要课表，时间切换
		Intent intent = new Intent(ZWatchContants.Action_CurrentWeekChange);
		context.sendBroadcast(intent);
	}

	/** 设置版本代号获取 用equal()来判断版本 */
	public static String getAppVersion(Context context) {
		return getSP(context).getString("version_name", "");
	}

	/** 设置版本代号 */
	public static void setAppVersion(Context context, String versionName) {
		set(getSP(context), "version_name", versionName);
	}
}
