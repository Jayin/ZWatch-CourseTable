package com.jayin.coursetable;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class CourseTableWidget extends AppWidgetProvider {
	public static final String Action_Course_Change ="Action_Course_Change";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action  = intent.getAction();
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
