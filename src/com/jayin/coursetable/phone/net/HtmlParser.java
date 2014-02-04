package com.jayin.coursetable.phone.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayin.coursetable.entity.Lesson;

/**
 * HTML 解析工具类
 * 
 * @author Jayin Ton
 * 
 */
public class HtmlParser {

	/**
	 * 解析课程html Note: 同一节课，会有>=1个课程 为此用List<Lesson>来存贮
	 * 
	 * @param html
	 *            课程表html
	 * @param map
	 *            周1-7，课时1-5
	 * @return map[周数，对应当日的课程列表]
	 */
	public static Map<Integer, Map<Integer, List<Lesson>>> parseHtmlForLesson(
			String html, Map<Integer, Map<Integer, List<Lesson>>> map) {
		for (int i = 1; i <= 7; i++) {
			HashMap<Integer, List<Lesson>> hashmap = new HashMap<Integer, List<Lesson>>();
			for (int j = 1; j <= 7; j++)
				hashmap.put(j, new ArrayList<Lesson>());
			map.put(i, hashmap);
		}
		Pattern p = Pattern.compile("<td valign=top align=center>(.*)</td>");
		Matcher m = p.matcher(html);
		int day = 1, lessonTime = 1;
		while (m.find()) {
			List<Lesson> mlist = new ArrayList<Lesson>();
			String s = m.group(1).trim();
			s = s.replaceAll("<br>", "&nbsp;");
			String[] ms = s.split("&nbsp;");
			int j = 0;
			int count = 0;
			Lesson l = null;
			while (j < ms.length) {
				while (ms[j].equals(""))
					j++;
				switch (count) {
				case 0:
					l = new Lesson();
					l.LessonName = ms[j++].trim();
					count++;
					break;

				case 1:
					l.Time = ms[j++].trim();
					count++;
					break;
				case 2:
					l.address = ms[j++].trim();
					count++;
					break;

				case 3:
					l.Teacher = ms[j++].trim();
					mlist.add(l);
					count = 0;
					System.out.println(l.toString());
					break;
				}
			}
			if (ms.length == 0) {
				l = new Lesson();
				mlist.add(l);
			}
			(map.get(day)).put(lessonTime, mlist);
			if (day + 1 > 7) {
				day = 1;
				lessonTime++;
			} else {
				day++;
			}
		}
		return map;
	}

}
