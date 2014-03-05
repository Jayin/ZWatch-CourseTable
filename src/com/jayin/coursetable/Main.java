package com.jayin.coursetable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
	private Course course;
	private String filePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "data.properties";

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
		_getView(R.id.btn_getCourse).setOnClickListener(this);
		_getView(R.id.btn_about).setOnClickListener(this);
		_getView(R.id.btn_clear).setOnClickListener(this);
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
		case R.id.btn_clear:
			CourseTableInfo.removeAll(getContext());
			toast("已清除");
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
					toast("正在登录获取课表");
					break;
				case faild:
					toast((String) msg.obj);
					break;
				case ok:
					toast("课表获取成功！");
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
					Message msg = h.obtainMessage();
					msg.what = faild;
				
					Properties p = new Properties();
					try {
						p.load(new FileInputStream(new File(filePath)));
						userID = p.getProperty("id");
						userPsw = p.getProperty("password");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
						msg.obj = "没有配置文件";
						h.sendMessage(msg);
					} catch (IOException e1) {
						e1.printStackTrace();
						msg.obj = "数据传输出错";
						h.sendMessage(msg);
					}
					SubSystemAPI api = new SubSystemAPI(userID, userPsw);
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
