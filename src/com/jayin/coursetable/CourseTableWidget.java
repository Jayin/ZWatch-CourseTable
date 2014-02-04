package com.jayin.coursetable;

import java.util.List;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.jayin.coursetable.entity.Course;
import com.jayin.coursetable.entity.Lesson;
import com.jayin.coursetable.utils.CalendarManager;
import com.jayin.coursetable.utils.CalendarUtils;
import com.jayin.coursetable.utils.CourseTableInfo;
import com.jayin.coursetable.utils.CourseUtils;
import com.jayin.coursetable.utils.ZWatchContants;

public class CourseTableWidget extends AppWidgetProvider {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		// 时间改变，课程改变，周数改变均需要更新
		if (action.equals(ZWatchContants.Action_Course_Change)
				|| action.equals(ZWatchContants.Action_CurrentWeekChange)
				|| action.equals("android.intent.action.TIME_SET")
				|| action.equals("android.intent.action.TIME_TICK")
				|| action.equals("android.intent.action.TIMEZONE_CHANGED")
				|| action.equals("android.appwidget.action.APPWIDGET_UPDATE")) {
			String time = CalendarUtils.getTimeFromat(
					System.currentTimeMillis(), CalendarUtils.TYPE_TWO);

			List<Lesson> _coursList1 = null, _coursList2 = null;
			Lesson l1 = null, l2 = null;
			String part = CalendarManager.getDayPart();
			Course course = CourseTableInfo.getCourse(context);

			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_coursetable);
			rv.setViewVisibility(R.id.linly_course, View.VISIBLE);
			rv.setViewVisibility(R.id.tv_nocourse, View.GONE);
			if (course != null) {
				if ("AM".equals(part)) {
					_coursList1 = course.getDailyLesson(
							CalendarManager.getWeekDay() - 1, 0);

					_coursList2 = course.getDailyLesson(
							CalendarManager.getWeekDay() - 1, 1);
				} else if ("PM".equals(part)) {
					_coursList1 = course.getDailyLesson(
							CalendarManager.getWeekDay() - 1, 2);

					_coursList2 = course.getDailyLesson(
							CalendarManager.getWeekDay() - 1, 3);
				} else {
					// Night
					_coursList1 = course.getDailyLesson(
							CalendarManager.getWeekDay() - 1, 4);
				}
				if (_coursList1 != null) {
					for (Lesson l : _coursList1) {
						if (CourseUtils.isLessonStart(context, l)) {
							l1 = l;
							break;
						}
					}
				}
				if (_coursList2 != null) {
					for (Lesson l : _coursList2) {
						if (CourseUtils.isLessonStart(context, l)) {
							l2 = l;
							break;
						}
					}
				}
				if (l2 == null) {
					if (l1 == null) {
						rv.setViewVisibility(R.id.linly_course, View.GONE);
						rv.setViewVisibility(R.id.tv_nocourse, View.VISIBLE);
					} else {
						rv.setViewVisibility(R.id.tv_address1, View.VISIBLE);
						rv.setViewVisibility(R.id.tv_coursename1, View.VISIBLE);
						rv.setViewVisibility(R.id.tv_address2, View.GONE);
						rv.setViewVisibility(R.id.tv_coursename2, View.GONE);

						rv.setTextViewText(R.id.tv_address1, l1.getAddress());
						rv.setTextViewText(R.id.tv_coursename1,
								l1.getLessonName());
					}
				} else {
					rv.setViewVisibility(R.id.tv_address1, View.VISIBLE);
					rv.setViewVisibility(R.id.tv_coursename1, View.VISIBLE);
					rv.setViewVisibility(R.id.tv_address2, View.VISIBLE);
					rv.setViewVisibility(R.id.tv_coursename2, View.VISIBLE);

					rv.setTextViewText(R.id.tv_address1, l1.getAddress());
					rv.setTextViewText(R.id.tv_coursename1, l1.getLessonName());
					rv.setTextViewText(R.id.tv_address2, l2.getAddress());
					rv.setTextViewText(R.id.tv_coursename2, l2.getLessonName());
				}

			} else {
				rv.setViewVisibility(R.id.linly_course, View.GONE);
				rv.setViewVisibility(R.id.tv_nocourse, View.VISIBLE);
				rv.setTextViewText(R.id.tv_nocourse, "尚未导入课表");// set day part
			}

			rv.setTextViewText(R.id.tv_time, time);// set time
			rv.setTextViewText(R.id.tv_dayPart, part);// set day part
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					CourseTableWidget.class);
			appWidgetManager.updateAppWidget(componentName, rv);
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_coursetable);
			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
		}
		context.sendBroadcast(new Intent(ZWatchContants.Action_Course_Change));
		Log.i("dubug", "onUpdate!");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
