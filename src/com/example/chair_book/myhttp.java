package com.example.chair_book;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class myhttp {

	private DefaultHttpClient httpclient;
	private Cookie cookie;

	private String username;
	private String password;


	/**
	 * 初始化
	 */
	public myhttp(String name, String pw) {
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 100);
		ConnManagerParams.setTimeout(params, 1000);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_0);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		httpclient = new DefaultHttpClient(cm, params);
		httpclient = new DefaultHttpClient();

		this.username = name;
		this.password = pw;

	}

	public String doget(String url, Header[] headers, Map<String, String> param) throws Exception {
		HttpGet httpget = new HttpGet(url); // 初始化Post
		BasicHttpParams httpparam = new BasicHttpParams();

		if (param != null) {
			Set<String> set = param.keySet();
			for (String string : set) {
				httpparam.setParameter(string, param.get(string));
			}

			httpget.setParams(httpparam);
		}
		httpget.setHeaders(headers);

		HttpResponse response = httpclient.execute(httpget); // 运行action
		HttpEntity entity = response.getEntity(); // 获得实体
		InputStream in = entity.getContent(); // 获得实体的内容
		String html = toStr(in);

		return html;
	}

	public String dopost(String url, Header[] headers, Map<String, String> param) throws Exception {
		HttpPost httpost = new HttpPost(url); // 初始化Post

		List<NameValuePair> nvps = new ArrayList<NameValuePair>(); // 构建参数

		Set<String> set = param.keySet();
		for (String string : set) {
			nvps.add(new BasicNameValuePair(string, param.get(string)));
		}

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		httpost.setHeaders(headers);

		HttpResponse response = httpclient.execute(httpost); // 运行post
		HttpEntity entity = response.getEntity(); // 获得实体
		InputStream in = entity.getContent(); // 获得实体的内容
		String html = toStr(in);

		if (entity != null) {
			entity.consumeContent();
		}

		return html;

	}	

	private String toStr(InputStream in) throws Exception {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n,"GB2312"));
		}
		String str = out.toString();
		return str;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DefaultHttpClient getHttpclient() {
		return httpclient;
	}

	public Cookie getCookie() {
		// 取出页面上返回的LtpaToken Cookie值。
				// String regEx = "(\"LtpaToken=(.+)\")";
				// String ltpaToken = null;
				// Pattern p = Pattern.compile(regEx);
				// Matcher m = p.matcher(html);
				// while (m.find()) {
				// ltpaToken = m.group(1);
				// if (ltpaToken.length() > 11) {
				// ltpaToken = ltpaToken.substring(11);
				// ltpaToken = ltpaToken.substring(0, ltpaToken.lastIndexOf("\""));
				// }
				// }
				// BasicClientCookie cookie = new BasicClientCookie("LtpaToken",
				// ltpaToken);
				// cookie.setDomain(".tj.unicom.local");
				// cookie.setPath("/");
				// CookieStore cookies = httpclient.getCookieStore();
				// cookies.addCookie(cookie);
				// httpclient.setCookieStore(cookies);
		return cookie;
	}

	public void setHttpclient(DefaultHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public void setCookie(Cookie cookie) {
		this.cookie = cookie;
	}
}
