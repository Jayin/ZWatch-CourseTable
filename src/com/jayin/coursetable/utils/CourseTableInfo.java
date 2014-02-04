package com.jayin.coursetable.utils;

import android.content.Context;

import com.jayin.coursetable.entity.Course;

public class CourseTableInfo {

	public static void setCourse(Context context, Course course) {
		new DataPool(ZWatchContants.SP_Name_Course, context).put(
				ZWatchContants.SP_Key_Course, course);
	}

	public static Course getCourse(Context context) {
		return (Course) new DataPool(ZWatchContants.SP_Name_Course, context)
				.get(ZWatchContants.SP_Key_Course);
	}
}
