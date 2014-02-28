package com.jayin.coursetable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.jayin.coursetable.entity.Course;
import com.jayin.coursetable.phone.net.SubSystemAPI;
import com.jayin.coursetable.utils.CourseTableInfo;
import com.jayin.coursetable.utils.DataPool;
import com.jayin.coursetable.utils.ZWatchContants;

/**
 * Main Page
 * 
 * @author Jayin Ton
 * 
 */
public class Main extends BaseActivity {
	private String userID = "", userPsw = "";// 子系统帐密
	private View btn_getCourse, btn_about;
	private Course course;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		course = CourseTableInfo.getCourse(getContext());
	}

	@Override
	protected void initLayout() {
		btn_getCourse = _getView(R.id.btn_getCourse);
		btn_about = _getView(R.id.btn_about);

		btn_getCourse.setOnClickListener(this);
		btn_about.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_getCourse:
			getCourse();
			break;
		case R.id.btn_about:
			openActivity(About.class);
			break;
		default:
			break;
		}
	}

	private void getCourse() {
		final int faild = 1;
		final int ok = 2;
		final int start = 3;
		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case start:
					toast("start to get course !");
					break;
				case faild:
					toast((String) msg.obj);
					break;
				case ok:
					toast("ok!");
					CourseTableInfo.setCourse(getContext(), course);
					break;
				default:
					break;
				}
			}
		};
		if (course == null) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					h.sendEmptyMessage(start);
					SubSystemAPI api = new SubSystemAPI(userID, userPsw);
					Message msg = h.obtainMessage();
					msg.what = faild;
					try {
						if (api.login()) {
							course = Course.translateData(api.getLessons());
							msg.what = ok;
							h.sendMessage(msg);
						} else {
							msg.obj = "登录失败";
							h.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						msg.obj = "连接异常";
						h.sendMessage(msg);
					}

				}
			}).start();
		} else {
			toast("已有课表");
		}
	}

}
