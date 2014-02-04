package com.jayin.coursetable.utils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

public class StringUtils {

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格(' ')、制表符('\t')、回车符'\r'、换行符'\n'组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input.trim())) {
			return true;
		}
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 解析课程的时间，用于判断什么时候上课
	 * 
	 * @param time
	 *            课程时间
	 * @return 要上课的周数集合set
	 */
	public static Set<Integer> parseTimeOfLesson(String time) {

		Set<Integer> set = new HashSet<Integer>();
		String[] afterSplit = time.replace("第", "").replace("周", "").trim()
				.split(",");
		for (String s : afterSplit) {

			if (s.indexOf("-") == -1) {
				set.add(Integer.parseInt(s));
			} else {
				String[] _s = s.split("-");
				int start = Integer.parseInt(_s[0]);
				int end = Integer.parseInt(_s[1]);
				for (int i = start; i <= end; i++) {
					set.add(i);
				}
			}
		}

		return set;
	}
	/**
	 * 生成时间字符串 格式： yy-mm-dd 星期X
	 * 
	 * @return
	 */
	public static String getTimeFormat() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		sb.append(c.get(Calendar.YEAR)).append("-")
				.append(c.get(Calendar.MONTH) + 1).append("-")
				.append(c.get(Calendar.DAY_OF_MONTH)).append(" 星期");
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) == 1 ? 7 : c
				.get(Calendar.DAY_OF_WEEK) - 1;
		sb.append(getChinese(day_of_week));
		return sb.toString();
	}
	/**
	 * 根据星期x获取中文<br>
	 * 星期1 ->星期一
	 * @param day_of_week 星期1 ->星期一
	 * @return
	 */
	public static String getChinese(int day_of_week){
		switch (day_of_week) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		default :
			return "日";
		}
	}

	/**
	 * 根据给出的时间 很格式 获取对应时间各个时期
	 * 
	 * @param milliseconds
	 * @param format
	 *            e.g.(yy-mm-dd) 有需要就在这里添加代码
	 * @return
	 */
	public static String getDateFormat(long milliseconds, String format) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds);
		StringBuilder sb = new StringBuilder();
		if (format.equals("yy-mm-dd")) {
			sb.append(c.get(Calendar.YEAR)).append("-")
					.append(c.get(Calendar.MONTH) + 1).append("-")
					.append(c.get(Calendar.DAY_OF_MONTH));
		} else if (format.equals("mm-dd")) {
			sb.append(c.get(Calendar.MONTH)).append("-")
					.append(c.get(Calendar.DAY_OF_MONTH));
		}

		return sb.toString();
	}
	

}
