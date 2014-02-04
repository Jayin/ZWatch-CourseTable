package com.jayin.coursetable.phone.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.jayin.coursetable.entity.Lesson;

/**
 * 子系统API  
 * @author Jayin Ton
 */
public class SubSystemAPI {
	private String userid = null;
	private String userpsw = null;
	private CookieStore mCookieStore = null;
	private String randomNumber = null;

	public SubSystemAPI(String userid, String userpsw) {
		this.userid = userid;
		this.userpsw = userpsw;
	}
    /**
     * 设置账号 密码
     * @param userid
     * @param userpsw
     */
	public void setUserData(String userid, String userpsw) {
		this.userid = userid;
		this.userpsw = userpsw;
	}

	/**
	 * 登录子系统
	 * 
	 * @return true if login successfully
	 */
	public boolean login() throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter("http.socket.timeout", 10000);
		HttpGet get = new HttpGet("http://jwc.wyu.edu.cn/student/rndnum.asp"); // get
																				// random
																				// code
		HttpPost post = new HttpPost("http://jwc.wyu.edu.cn/student/logon.asp");// login
		HttpResponse response = null;

		response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Header h = response.getFirstHeader("Set-Cookie");
			HeaderElement[] he = h.getElements();
			randomNumber = he[0].getValue();
			mCookieStore = client.getCookieStore();
			// login
			String UserCode = userid;
			String UserPwd = userpsw;
			String Validate = randomNumber;
			String Submit = "%CC%E1+%BD%BB";
			// 构建表头
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("UserCode", UserCode));
			parameters.add(new BasicNameValuePair("UserPwd", UserPwd));
			parameters.add(new BasicNameValuePair("Validate", Validate));
			parameters.add(new BasicNameValuePair("Submit", Submit));
			HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
			post.setEntity(entity);
			post.setHeader("Referer", "http://jwc.wyu.edu.cn/student/body.htm"); //12.9 学校坑了！
			client.getConnectionManager().shutdown();
			// 设置httpClient参数，不自动重定向
			HttpParams httpParams = new BasicHttpParams();
			HttpClientParams.setRedirecting(httpParams, false);
			client = new DefaultHttpClient(httpParams);
			client.setCookieStore(mCookieStore);

			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("login successfully!");
				client.getConnectionManager().shutdown();
				return true;

			} else {
				client.getConnectionManager().shutdown();
				System.out.println("code:--"+response.getStatusLine().getStatusCode());
			  //  System.out.println(EntityUtils.toString(response.getEntity()));
				System.out.println("login faild!");
				return false;
			}

		} else {
			client.getConnectionManager().shutdown();
			System.out.println("get random code faild!");
			return false;
		}

	}

	/**
	 * 获取课表
	 * 
	 * @return 课表列表
	 * @throws ETipsException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Map<Integer, Map<Integer, List<Lesson>>> getLessons()
			throws IllegalStateException, ClientProtocolException, IOException {
		Map<Integer, Map<Integer, List<Lesson>>> map = new HashMap<Integer, Map<Integer, List<Lesson>>>();
		if (mCookieStore == null)
			throw new IllegalStateException("you should login before call this method");

		DefaultHttpClient client = new DefaultHttpClient();
		client.setCookieStore(mCookieStore);
		HttpGet get = new HttpGet("http://jwc.wyu.edu.cn/student/f3.asp");
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String html = EntityUtils.toString(response.getEntity(), "gb2312");
			client.getConnectionManager().shutdown();
			return HtmlParser.parseHtmlForLesson(html, map);
		}
		client.getConnectionManager().shutdown();
		return map;
	}
}
