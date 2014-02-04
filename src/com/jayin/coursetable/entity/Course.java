package com.jayin.coursetable.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;


/**
 * 一周课程的实体类<br>
 * add in version 2.1
 * 
 * @author Jayin Ton
 * 
 */
public class Course implements Serializable {
	private List<List<List<Lesson>>> courseList;

	public Course(List<List<List<Lesson>>> course) {
		this.courseList = course;
	}

	/**
	 * 获得星期whichDay(从0开始)的课程列表
	 * 
	 * @param whichDay
	 *            星期几(从0开始)
	 * @return
	 */
	public List<List<Lesson>> getDailyLesson(int whichDay) {
		return this.courseList.get(whichDay);
	}

	/**
	 * 获得星期whichDay(从0开始),第time(从0开始)课室上的课程列表
	 * 
	 * @param whichDay
	 *            星期几 
	 * @param time
	 *            第几节课时 (从0开始)
	 * @return
	 */
	public List<Lesson> getDailyLesson(int whichDay, int time) {
		return getDailyLesson(whichDay).get(time);
	}
	/**
	 * 把从api或去到的数据转换成一个Course
	 * @param map Map<Integer, Map<Integer, List<Lesson>>>
	 * @return
	 */
	public static  Course translateData(Map<Integer, Map<Integer, List<Lesson>>> map){
		List<List<List<Lesson>>> mmmlist = new ArrayList<List<List<Lesson>>>();
		List<List<Lesson>> mmlist = new ArrayList<List<Lesson>>();
		List<Lesson> mlist;
		for (int i = 1; i <= 7; i++) { // 周1-7
			Map<Integer, List<Lesson>> week = map.get(i);
			mmlist = new ArrayList<List<Lesson>>();
			for (int j = 1; j <= 5; j++) { // 第1-5节
				List<Lesson> list = week.get(j);
				mlist = new ArrayList<Lesson>();
				for (Lesson l : list) {
					l.week = i;
					l.classtime = j;
					mlist.add(l);
				}
				mmlist.add(mlist);
			}
			mmmlist.add(mmlist);
		}
		return new Course(mmmlist);
	}

	public List<List<List<Lesson>>> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<List<List<Lesson>>> courseList) {
		this.courseList = courseList;
	}

	@Override
	public String toString() {
		return "Course [courseList=" + courseList.toString() + "]";
	}

}
